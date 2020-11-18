package com.honeyrock.opencv_test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
/*import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;*/
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.opencv.core.CvType.CV_8UC3;
import static org.opencv.core.CvType.CV_8UC4;


public class ImageActivity extends AppCompatActivity {

    private static final String TAG = "AndroidOpenCv";
    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private Bitmap mInputImage;
    private Bitmap mOriginalImage;
    private ImageView mImageView;
    private ImageView mEdgeImageView;
    private boolean mIsOpenCVReady = false;
    ArrayList<int[]> arrXY = new ArrayList<>();
    public native void detectEdgeJNI(long inputImage, long outputImage, int th1, int th2);
    public native void drawFillPoly(long outputImage,int[] arr, int[] arr2, int th1, int th2);


    static {
        System.loadLibrary("opencv_java4");
        System.loadLibrary("native-lib");
    }

    public void detectEdge() {
        Mat src = new Mat();
        Utils.bitmapToMat(mInputImage, src);
        Mat edge = new Mat();
        Imgproc.Canny(src, edge, 50, 150);
        Utils.matToBitmap(edge, mInputImage);
        src.release();
        edge.release();
        mEdgeImageView.setImageBitmap(mInputImage);
    }

    public void detectEdgeUsingJNI() {
        if (!mIsOpenCVReady) {
            return;
        }
        Mat src = new Mat();
        Utils.bitmapToMat(mInputImage, src);
        mImageView.setImageBitmap(mOriginalImage);
        Mat edge = new Mat();
        detectEdgeJNI(src.getNativeObjAddr(), edge.getNativeObjAddr(), 50, 150);
        Utils.matToBitmap(edge, mInputImage);
        mEdgeImageView.setImageBitmap(mInputImage);
    }

    public void drawFillPolyUsingJNI(){
        //drawFillPoly();
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image);
        //mView = new DrawTouchView(this);
        //setContentView(mView);
        mImageView = findViewById(R.id.origin_iv);
        mEdgeImageView = findViewById(R.id.edge_iv);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!hasPermissions(PERMISSIONS)) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            mIsOpenCVReady = true;
        }
    }
    public void onDestroy() {
        super.onDestroy();

        mInputImage.recycle();
        if (mInputImage != null) {
            mInputImage = null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    String path = getImagePathFromURI(data.getData());
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    mOriginalImage = BitmapFactory.decodeFile(path, options);
                    mInputImage = BitmapFactory.decodeFile(path, options);
                    if (mInputImage != null) {
                        detectEdgeUsingJNI();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void onButtonClicked(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

    public void onButtonClicked2(View view) {

        int[] arrX = new int[arrXY.size()];
        int[] arrY = new int[arrXY.size()];
        for (int i = 0; i < arrXY.size(); i++) {
            arrX[i] = arrXY.get(i)[0];
            arrY[i] = arrXY.get(i)[1];
        }
        if (!mIsOpenCVReady) {
            return;
        }
/*        Mat src = new Mat();
        Utils.bitmapToMat(mInputImage, src);
        mImageView.setImageBitmap(mOriginalImage);
        Mat edge = new Mat();
        detectEdgeJNI(src.getNativeObjAddr(), edge.getNativeObjAddr(), 50, 150);
        Utils.matToBitmap(edge, mInputImage);
        mEdgeImageView.setImageBitmap(mInputImage);
        */


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("getDefaultDisplay", width+":"+height);
        int[] i_arr = {width, height};
        Mat image = Mat.zeros(i_arr, CV_8UC4);
        Mat outputMat = new Mat();
        drawFillPoly(image.getNativeObjAddr(), arrX, arrY, width, height);
        Bitmap outputBitmap = Bitmap.createBitmap( image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        if (image != null){
            Utils.matToBitmap(image, outputBitmap);
            Log.i("Utils.matToBitmap" , "-=================");
        }

        if (outputBitmap != null){
            mEdgeImageView.setImageBitmap(outputBitmap);

            Log.i("setImageBitmap" , "-=================");
        }
        arrXY.clear();
    }

    // permission
    static final int PERMISSIONS_REQUEST_CODE = 1000;
    String[] PERMISSIONS = {"android.permission.READ_EXTERNAL_STORAGE"};


    private boolean hasPermissions(String[] permissions) {
        int result;
        for (String perms : permissions) {
            result = ContextCompat.checkSelfPermission(this, perms);
            if (result == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    public String getImagePathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String imgPath = cursor.getString(idx);
            cursor.close();
            return imgPath;
        }
    }

    // permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {

            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraPermissionAccepted = grantResults[0]
                            == PackageManager.PERMISSION_GRANTED;

                    if (!cameraPermissionAccepted)
                        showDialogForPermission("실행을 위해 권한 허가가 필요합니다.");
                }
                break;
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ImageActivity.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                requestPermissions(PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float curX = event.getX();
        float curY = event.getY();
        if (action == MotionEvent.ACTION_UP){

            Log.i("touchEvent", curX+":"+curY);
            arrXY.add(new int[]{(int)curX, (int)curY});
            addPoint();
        }

        return true;
    }
    private void addPoint(){

        int[] arrX = new int[arrXY.size()];
        int[] arrY = new int[arrXY.size()];
        for (int i = 0; i < arrXY.size(); i++) {
            arrX[i] = arrXY.get(i)[0];
            arrY[i] = arrXY.get(i)[1];
        }
        if (!mIsOpenCVReady) {
            return;
        }

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("getDefaultDisplay", width+":"+height);
        int[] i_arr = {width, height};
        Mat image = Mat.zeros(i_arr, CV_8UC4);
        Mat outputMat = new Mat();
        drawFillPoly(image.getNativeObjAddr(), arrX, arrY, width, height);
        Bitmap outputBitmap = Bitmap.createBitmap( image.cols(), image.rows(), Bitmap.Config.ARGB_8888);
        if (image != null){
            Utils.matToBitmap(image, outputBitmap);
            Log.i("Utils.matToBitmap" , "-=================");
        }

        if (outputBitmap != null){
            mEdgeImageView.setImageBitmap(outputBitmap);

            Log.i("setImageBitmap" , "-=================");
        }
        //arrXY.clear();
    }
}

#include <jni.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>
#include <android/log.h>

using namespace cv;
/*#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_honeyrock_opencv_1test_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject *//* this *//*) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}*/
extern "C"
JNIEXPORT void JNICALL
Java_learn_kotlin_com_androidopencv_CameraActivity_ConvertRGBtoGray(JNIEnv *env, jobject instance,
                                                                    jlong inputMat,
                                                                    jlong outputMat) {
    // 입력 RGBA 이미지를 GRAY 이미지로 변환
    Mat &inputImage = *(Mat *) inputMat;
    Mat &outputImage = *(Mat *) outputMat;

    cvtColor(inputImage, outputImage, COLOR_RGBA2GRAY);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_honeyrock_opencv_1test_ImageActivity_detectEdgeJNI(JNIEnv *env, jobject thiz,
                                                            jlong input_image, jlong output_image,
                                                            jint th1, jint th2) {
    // TODO: implement detectEdgeJNI()
    Mat &inputMat= *(Mat *) input_image;
    Mat &outputMat = *(Mat *) output_image;

    cvtColor(inputMat, outputMat, COLOR_RGB2GRAY);
    Canny(outputMat, outputMat, th1, th2);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_honeyrock_opencv_1test_ImageActivity_drawFillPoly(JNIEnv *env, jobject thiz,
                                                           jlong output_image, jintArray arrX, jintArray arrY, jint x, jint y) {
    int lineType = 8;
    Point pt[2][3];


    jintArray jintArray = arrX;
    jint *int_bufX;
    jint *int_bufY;
    int_bufX = env->GetIntArrayElements(arrX, NULL);
    int_bufY = env->GetIntArrayElements(arrY, NULL);
    jsize arrCnt = (env)->GetArrayLength(arrX);
    //int arrCnt = size;
    for (int i = 0; i < arrCnt; ++i) {
        __android_log_print(ANDROID_LOG_INFO, "int_bufX", "int_bufX is %d", int_bufX[i]);
        __android_log_print(ANDROID_LOG_INFO, "int_bufY", "int_bufY is %d", int_bufY[i]);
    }
    __android_log_print(ANDROID_LOG_INFO, "arrCnt", "arrCnt is %d", arrCnt);

    RNG rng( 0xFFFFFFFF );
    int w = x;
    /** Create some points */

    Mat &outputMat = *(Mat *) output_image;
    __android_log_print(ANDROID_LOG_INFO, "draw", "%s", "circle");
    if (arrCnt < 2){

        Point rook_points;
        rook_points.x = int_bufX[0];
        rook_points.y = int_bufY[0];
        circle(outputMat, rook_points, 30, Scalar(150, 150, 150, 0), -1, 8, 0);
        __android_log_print(ANDROID_LOG_INFO, "draw", "%s", "circle");
    } else if (arrCnt<3){
        Point rook_point1;
        rook_point1.x = int_bufX[0];
        rook_point1.y = int_bufY[0];
        Point rook_point2;
        rook_point2.x = int_bufX[1];
        rook_point2.y = int_bufY[1];
        circle(outputMat, rook_point1, 30, Scalar(150, 150, 150, 0), -1, 8, 0);
        circle(outputMat, rook_point2, 30, Scalar(150, 150, 150, 0), -1, 8, 0);
        __android_log_print(ANDROID_LOG_INFO, "draw", "%s", "circle");
        __android_log_print(ANDROID_LOG_INFO, "draw", "%s", "circle");
    } else{
        Point rook_points[1][arrCnt]; // 여기서 rook는 체스의 말의 이름입니다.
        for (int i = 0; i < arrCnt; ++i) {
            rook_points[0][i] = Point(int_bufX[i], int_bufY[i]);
        }
        const Point* ppt[1] = { rook_points[0] };
        int npt[] = { arrCnt };

        fillPoly( outputMat, ppt, npt, 1, Scalar(150, 150, 150, 255), lineType );
        //polylines( outputMat, ppt, npt, 1, true, Scalar(255, 255, 255, 0));
        //fillConvexPoly(outputMat,ppt,Scalar(50, 255, 255, 0),lineType,0);
        __android_log_print(ANDROID_LOG_INFO, "draw", "%s", "fillPoly");
    }

    //fillPoly( outputMat, ppt, npt, 1,(255, 0, 0), 8);
    env->ReleaseIntArrayElements(arrX, int_bufX, 0);
    env->ReleaseIntArrayElements(arrY, int_bufY, 0);

}
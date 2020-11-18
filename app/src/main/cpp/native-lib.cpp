#include <jni.h>
#include <opencv2/opencv.hpp>
#include <opencv2/imgproc.hpp>

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

    //TODO 클릭시 서클 추가
    //TODO if문 컨디션 문제 해결
/*    if (sizeof(jintArray)<3)
        circle()*/

    RNG rng( 0xFFFFFFFF );
    int w = x;
    /** Create some points */
    Point rook_points[1][sizeof(int_bufX)]; // 여기서 rook는 체스의 말의 이름입니다.
    for (int i = 0; i < sizeof(int_bufX); ++i) {
        rook_points[0][i] = Point(int_bufX[i], int_bufY[i]);
    }
/*    rook_points[0][0] = Point(w / 4.0, 7 * w / 8.0);
    rook_points[0][1] = Point(3 * w / 4.0, 7 * w / 8.0);
    rook_points[0][2] = Point(3 * w / 4.0, 13 * w / 16.0);
    rook_points[0][3] = Point(11 * w / 16.0, 13 * w / 16.0);
    rook_points[0][4] = Point(19 * w / 32.0, 3 * w / 8.0);
    rook_points[0][5] = Point(3 * w / 4.0, 3 * w / 8.0);
    rook_points[0][6] = Point(3 * w / 4.0, w / 8.0);
    rook_points[0][7] = Point(26 * w / 40.0, w / 8.0);
    rook_points[0][8] = Point(26 * w / 40.0, w / 4.0);
    rook_points[0][9] = Point(22 * w / 40.0, w / 4.0);
    rook_points[0][10] = Point(22 * w / 40.0, w / 8.0);
    rook_points[0][11] = Point(18 * w / 40.0, w / 8.0);
    rook_points[0][12] = Point(18 * w / 40.0, w / 4.0);
    rook_points[0][13] = Point(14 * w / 40.0, w / 4.0);
    rook_points[0][14] = Point(14 * w / 40.0, w / 8.0);
    rook_points[0][15] = Point(w / 4.0, w / 8.0);
    rook_points[0][16] = Point(w / 4.0, 3 * w / 8.0);
    rook_points[0][17] = Point(13 * w / 32.0, 3 * w / 8.0);
    rook_points[0][18] = Point(5 * w / 16.0, 13 * w / 16.0);
    rook_points[0][19] = Point(w / 4.0, 13 * w / 16.0);*/
    const Point* ppt[1] = { rook_points[0] };
    int npt[] = { sizeof(arrX) };

    Mat &outputMat = *(Mat *) output_image;
    //Mat image = Mat::zeros( 500, 500, CV_8UC3 );

    //const Point* ppt[2] = {pt[0], pt[1]};
    //int npt[] = {3, 3};
    fillPoly( outputMat, ppt, npt, 1, Scalar(255, 255, 255, 0), lineType );
    //fillPoly( outputMat, ppt, npt, 1,(255, 0, 0), 8);
    env->ReleaseIntArrayElements(arrX, int_bufX, 0);
    env->ReleaseIntArrayElements(arrY, int_bufY, 0);

}
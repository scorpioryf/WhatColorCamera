//
// Created by holyg on 2019/2/1.
//

#include <jni.h>
#include <iostream>
#include <android/log.h>
#include "include/opencv2/opencv.hpp"

#define  LOG_TAG    "Log info from jni =>"
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)
#define LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)
#define LOGD(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG, __VA_ARGS__)

using namespace std;
using namespace cv;

std::string jstring2str(JNIEnv* env, jstring jstr)
{
    char*   rtn   =   NULL;
    jclass   clsstring   =   env->FindClass("java/lang/String");
    jstring   strencode   =   env->NewStringUTF("GB2312");
    jmethodID   mid   =   env->GetMethodID(clsstring,   "getBytes",   "(Ljava/lang/String;)[B");
    jbyteArray   barr=   (jbyteArray)env->CallObjectMethod(jstr,mid,strencode);
    jsize   alen   =   env->GetArrayLength(barr);
    jbyte*   ba   =   env->GetByteArrayElements(barr,JNI_FALSE);
    if(alen   >   0)
    {
        rtn   =   (char*)malloc(alen+1);
        memcpy(rtn,ba,alen);
        rtn[alen]=0;
    }
    env->ReleaseByteArrayElements(barr,ba,0);
    std::string stemp(rtn);
    free(rtn);
    return   stemp;
}
//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_process_main_myapplication_MainActivity_stringFromJNI(JNIEnv* env,jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}

extern "C"
JNIEXPORT jintArray JNICALL
Java_com_testdemo_holyg_gittest_ImgProcess_opencvPicProcess(JNIEnv *env, jobject instance,jintArray buf,jint w,jint h,jdouble K,jdouble K0) {

    // TODO
    jint *cbuf;
    jboolean ptfalse = false;
    cbuf = env->GetIntArrayElements(buf,&ptfalse);
    if(cbuf == NULL){
        LOGE("cbuf load ERROR. Methord returned");
    }
    Mat imgData(h,w,CV_8UC4,(unsigned char*)cbuf);
    //the methord should be set at this positition
    LOGE("IF YOU CAN SEE THIS LINE THAT MEAN JNI WORKING");

    /*Code below is the operation methord*/

    vector<Mat> channels;

    /*RGB2HSVcvt*/
//    cvtColor(imgData,imgData,COLOR_RGBA2RGB,4);
    cvtColor(imgData,imgData,COLOR_RGB2HSV,4);
    split(imgData,channels);
    float k = K;//0.23
    float k0 = K0;//1.45
    for (int i = 0; i < h; ++i) {
        for (int j = 0; j <w; ++j) {
            float a = channels[1].at<uchar>(i,j);
            a = a * k + k0;
            if(a>255){
                a = 255;
            }
            channels[1].at<uchar>(i,j) = (unsigned char)a;
        }
    }
    merge(channels,imgData);
    cvtColor(imgData,imgData,COLOR_HSV2RGB,4);
//    cvtColor(imgData,imgData,COLOR_RGB2RGBA,4);

    /*vector<Mat> channels;
    cvtColor(imgData,imgData,COLOR_RGBA2RGB,4);
    cvtColor(imgData,imgData,COLOR_RGB2HSV,3);
    split(imgData,channels);
    merge(channels,imgData);
    cvtColor(imgData,imgData,COLOR_HSV2RGB,3);
    cvtColor(imgData,imgData,COLOR_RGB2RGBA,4);*/



    /*LMS*/

    /*split(imgData,channels);
    float K = 0.5;
    for (int i = 0; i < h; ++i) {
        for (int j = 0; j < w; ++j) {
            unsigned char Rpixel = channels[2].at<uchar>(i,j);
            unsigned char Gpixel = channels[1].at<uchar>(i,j);
            unsigned char Bpixel = channels[0].at<uchar>(i,j);
            float result_c = Bpixel + K * Gpixel - K * Rpixel;
            if(result_c<0){
                result_c = 0;
            }
            else if(result_c>255){
                result_c = 255;
            }
            channels[0].at<uchar>(i,j) = (unsigned char)result_c;
        }
    }
    merge(channels,imgData);*/



    int size = w*h;
    jintArray result = env->NewIntArray(size);
    env->SetIntArrayRegion(result,0,size,(jint*)imgData.data);
    env->ReleaseIntArrayElements(buf,cbuf,0);
    return result;

}
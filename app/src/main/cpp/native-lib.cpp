//
// Created by holyg on 2019/2/1.
//

#include <jni.h>
#include <string>

//extern "C"
//JNIEXPORT jstring JNICALL
//Java_com_process_main_myapplication_MainActivity_stringFromJNI(JNIEnv* env,jobject /* this */) {
//    std::string hello = "Hello from C++";
//    return env->NewStringUTF(hello.c_str());
//}
extern "C"
JNIEXPORT jstring JNICALL
Java_com_testdemo_holyg_gittest_picShowActivity_stringFromJNI(JNIEnv *env, jobject instance) {

    // TODO
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());

    //return env->NewStringUTF(returnValue);
}
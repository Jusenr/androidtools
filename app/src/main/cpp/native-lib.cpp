#include <jni.h>
#include <string>
#include <android/log.h>

#ifdef __cplusplus  //禁止编译器改函数名字
extern "C" {
#endif

//LOG定义
#define  LOG_TAG    "Native_Jni"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)


JNIEXPORT jstring JNICALL
Java_com_jusenr_tools_NativeLib_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    LOGI("%s", hello.c_str());
    return env->NewStringUTF(hello.c_str());
}

JNIEXPORT jstring JNICALL
Java_com_jusenr_tools_NativeLib_stringFJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "点我试试";
    LOGD("%s", hello.c_str());
    return env->NewStringUTF(hello.c_str());
}

#ifdef __cplusplus
}
#endif
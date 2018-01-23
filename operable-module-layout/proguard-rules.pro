# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\AndroidStudio\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep class com.andova.oml.OMLInitializer{*;}
-keep class com.andova.oml.OMLModuleOptions{*;}
-keep class com.andova.oml.OMLModuleProvider{*;}
-keep class com.andova.oml.dao.ModuleItem{*;}
-keep class com.andova.oml.Utils{*;}

-keepclasseswithmembers class com.andova.oml.OMLApplicationAgent{
    <init>(android.app.Application, com.andova.oml.OMLModuleOptions, com.andova.oml.OMLModuleProvider);
    public void onCreate();
    public void onTerminate();
}
-keepclasseswithmembers class com.andova.oml.edit.ModuleActivityAgent{
    <init>(android.support.v7.app.AppCompatActivity, int, int);
    public final static int OML_CODE_REQUEST;
    public final static int OML_CODE_RESULT;
}
-keepclasseswithmembers class com.andova.oml.dao.ModuleManager{
    public void updateModuleProvider(com.andova.oml.OMLModuleProvider);
}
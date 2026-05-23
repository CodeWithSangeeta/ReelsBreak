# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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


# Shrink and optimize application strings
-repackageclasses ''
-allowaccessmodification

# Obfuscate core internal logic path structures
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Protect the main Android system components from stripping while hiding internal sub-routines
-keep class * extends android.app.Activity
-keep class * extends android.app.Application
-keep class * extends android.app.Service

# Explicitly keep Hilt/Dagger initialization paths clean
-keep class * extends dagger.hilt.internal.GeneratedComponent
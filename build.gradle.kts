plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    //ksp plugin
    alias(libs.plugins.ksp) apply false

    //hit plugins
    alias(libs.plugins.hilt) apply false
}
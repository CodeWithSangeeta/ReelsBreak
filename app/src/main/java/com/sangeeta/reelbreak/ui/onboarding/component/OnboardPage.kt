package com.sangeeta.reelbreak.ui.onboarding.component

sealed class OnboardPage {
    object Welcome : OnboardPage()
    object Counter : OnboardPage()
    object Break : OnboardPage()
    object Permission : OnboardPage()
}

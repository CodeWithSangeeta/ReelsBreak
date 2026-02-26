package com.practice.reelbreak.ui.onboarding.component

import com.practice.reelbreak.R

sealed class OnboardPage(
    val imageRes: Int,
    val title: String,
    val description: String
) {
    object Welcome : OnboardPage(
        imageRes = R.drawable.page_tracker_img,
        title = "Scroll Mindfully",
        description = """
            Track your reel usage.
            Set daily limits.
            Stay focused with mindful reminders.
            Build better digital habits every day.
        """.trimIndent()
    )

    object Counter : OnboardPage(
        imageRes = R.drawable.page_focused_img,
        title = "Stay Aware",
        description = """
            A small floating counter keeps track
            of your reels in real time.
            No surprises. No over-scrolling.
        """.trimIndent()
    )

    object Break : OnboardPage(
        imageRes = R.drawable.page_tracker_img,
        title = "Mindful Breaks",
        description = """
            After a few reels, we gently pause the scroll.
            Take a breath.
            Reset your focus.
        """.trimIndent()
    )

    object Permission : OnboardPage(
        imageRes = R.drawable.page_tracker_img,
        title = "We Respect Privacy",
        description = """
            ReelsBreak requires minimal permissions
            to detect reel activity and display your counter.

            We never:
            • Read your messages
            • Capture your screen
            • Collect personal data
        """.trimIndent()
    )
}

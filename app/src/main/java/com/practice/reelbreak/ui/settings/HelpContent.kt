package com.practice.reelbreak.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.practice.reelbreak.ui.theme.LocalAppColors

@Composable
fun HelpContent() {
    val colors = LocalAppColors.current
    val faqs = listOf(
        "App is not blocking reels?" to
                "Make sure Accessibility Service is enabled. Go to Android Settings → Accessibility → Installed Services → ReelsBreak → Turn ON.",
        "Blocking stopped after phone restart?" to
                "Some phones kill background services on restart. Go to Battery Settings and disable 'Battery Optimization' for ReelsBreak, or add it to the Auto-Start list.",
        "Battery optimization killing the service?" to
                "Go to Settings → Battery → Battery Optimization → find ReelsBreak → select 'Don't optimize'. This keeps the accessibility service alive.",
        "How to re-enable after an app update?" to
                "After an update, Android sometimes disables accessibility services for security. Just go back to Accessibility Settings and re-enable ReelsBreak.",
        "What's the difference between Block and Limit mode?" to
                "Block mode (Strict) immediately redirects you away from any reel. Limit mode lets you watch a set number of reels, then auto-blocks for the rest of the day."
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        faqs.forEach { (question, answer) ->
            FaqItem(question = question, answer = answer)
        }
    }
}

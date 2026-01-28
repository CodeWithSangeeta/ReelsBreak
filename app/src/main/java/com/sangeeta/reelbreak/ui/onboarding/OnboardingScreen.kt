package com.practice.reelbreak.ui.onboarding

import android.content.Context
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practice.reelbreak.R
import com.practice.reelbreak.data.DataStoreManager
import com.practice.reelbreak.navigation.routes
import com.practice.reelbreak.ui.onboarding.component.ButtonGradient
import com.practice.reelbreak.ui.onboarding.component.FloatingImage
import com.practice.reelbreak.ui.onboarding.component.GradientColor
import com.practice.reelbreak.ui.onboarding.component.IndicatorRow
import com.practice.reelbreak.ui.onboarding.component.OnboardDescription
import com.practice.reelbreak.ui.onboarding.component.OnboardHeading
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    navController: NavController,
    dataStore : DataStoreManager
) {
    val coroutineScope = rememberCoroutineScope()
    val pageState = rememberPagerState(pageCount = {4})


    Column(modifier = Modifier.fillMaxSize()
        .background(GradientColor.background)
        .padding(16.dp)
    ) {
      //Skip button
        if(pageState.currentPage !=3) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    coroutineScope.launch {
                        dataStore.saveOnboardingCompleted()
                    }
                    navController.navigate(routes.HOME) {
                        popUpTo(routes.OnBoarding) { inclusive = true }
                    }
                }) {
                    Text(
                        text = "Skip",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFC2B5B5),

                        )
                }
            }
        }

    HorizontalPager(state = pageState,
        modifier = Modifier.weight(1f)){page ->
        when(page){
            0 -> OnboardPageWelcome()
            1 -> OnboardPageCounterVisible()
            2 -> OnboardPageBreak()
            3 -> OnboardPagePermissions()
        }
    }
      Spacer(modifier = Modifier.height(40.dp))
        IndicatorRow(
            currentPage = pageState.currentPage,
            totalPages = 4
        )
        Spacer(modifier = Modifier.height(32.dp))

        ButtonGradient(
            text = if(pageState.currentPage==3) "Grant Permission" else "Continue",
            onClick = {
                if(pageState.currentPage < 3){
                    coroutineScope.launch {
                        pageState.animateScrollToPage(pageState.currentPage+1)
                    }

                }
                else{
                    coroutineScope.launch {
                        dataStore.saveOnboardingCompleted()
                    }
                    navController.navigate(routes.HOME) {
                        popUpTo(routes.OnBoarding) { inclusive = true }
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(40.dp))


    }

}



@Composable
fun OnboardPageWelcome(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        FloatingImage(
            imageResId = R.drawable.page_tracker_img,
            floatingDistance = 80f,
            size = 300.dp
        )
        Spacer(modifier = Modifier.height(32.dp))
        OnboardHeading("Welcome to ReelsBreak")
        Spacer(modifier = Modifier.height(16.dp))
        OnboardDescription("Track your reel usage, Reduce endless scrolling, Set daily limits, and Stay focused with smart reminders—Boost your digital wellbeing every day.")
    }
}



@Composable
fun OnboardPageCounterVisible(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        FloatingImage(
            imageResId = R.drawable.page_focused_img,
            floatingDistance = 120f,
            size = 160.dp
        )
        Spacer(modifier = Modifier.height(150.dp))
        OnboardHeading("Your Reels Count, Always Visible")
        Spacer(modifier = Modifier.height(16.dp))
        OnboardDescription("A floating mini counter stays on your screen so you always know how much you've watched.")
    }
}

@Composable
fun OnboardPageBreak(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        FloatingImage(
            imageResId = R.drawable.page_tracker_img,
            floatingDistance = 80f,
            size = 300.dp
        )
        Spacer(modifier = Modifier.height(32.dp))
        OnboardHeading("Smart Break Alerts")
        Spacer(modifier = Modifier.height(16.dp))
        OnboardDescription("After every few reels, the screen dims and shows a mindful breathing animation or a motivational quote.")
    }
}

@Composable
fun OnboardPagePermissions(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        FloatingImage(
            imageResId = R.drawable.page_tracker_img,
            floatingDistance = 80f,
            size = 300.dp
        )
        Spacer(modifier = Modifier.height(32.dp))
        OnboardHeading("We Respect Your Privacy")
        Spacer(modifier = Modifier.height(16.dp))
        OnboardDescription("To track reels and show live counters, ReelsBreak needs Accessibility & Usage permissions. We only use what's required.")
    }
}




package com.sangeeta.reelbreak.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sangeeta.reelbreak.R
import com.sangeeta.reelbreak.data.DataStoreManager
import com.sangeeta.reelbreak.ui.navigation.Routes
import com.sangeeta.reelbreak.ui.onboarding.component.ButtonGradient
import com.sangeeta.reelbreak.ui.onboarding.component.FloatingImage
import com.sangeeta.reelbreak.ui.onboarding.component.GradientColor
import com.sangeeta.reelbreak.ui.onboarding.component.IndicatorRow
import com.sangeeta.reelbreak.ui.onboarding.component.OnboardDescription
import com.sangeeta.reelbreak.ui.onboarding.component.OnboardHeading
import com.sangeeta.reelbreak.ui.onboarding.component.OnboardPage
import com.sangeeta.reelbreak.viewmodel.MainViewModel
import kotlinx.coroutines.launch


@Composable
fun OnboardingScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val pages = listOf(
        OnboardPage.Welcome,
        OnboardPage.Counter,
        OnboardPage.Break,
        OnboardPage.Permission
    )

    val pageState = rememberPagerState(pageCount = { pages.size })



    Column(modifier = Modifier.fillMaxSize()
        .background(GradientColor.background)
        .padding(16.dp)
    ) {
      //Skip button
        if(pageState.currentPage !=3) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                TextButton(onClick = {
                    mainViewModel.completeOnboarding()
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
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

        HorizontalPager(
            state = pageState,
            modifier = Modifier.weight(1f)
        ) { index ->

            when (pages[index]) {
                is OnboardPage.Welcome -> OnboardPageWelcome()
                is OnboardPage.Counter -> OnboardPageCounterVisible()
                is OnboardPage.Break -> OnboardPageBreak()
                is OnboardPage.Permission -> OnboardPagePermissions()
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        IndicatorRow(
            currentPage = pageState.currentPage,
            totalPages = 4
        )
        Spacer(modifier = Modifier.height(32.dp))

        ButtonGradient(
            text ="Continue",
            onClick = {
                if(pageState.currentPage < pages.size-1){
                    coroutineScope.launch {
                        pageState.animateScrollToPage(pageState.currentPage+1)
                    }

                }
                else{
                    navController.navigate(Routes.PERMISSION) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
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
        OnboardDescription(
            "ReelsBreak needs limited permissions to detect reel scrolling and show live counters.\n\n" +
                    "• We do NOT read messages\n" +
                    "• We do NOT capture screens\n" +
                    "• We do NOT collect personal data"
        )
    }
}




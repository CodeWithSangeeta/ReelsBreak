package com.practice.reelbreak.ui.onboarding

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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practice.reelbreak.ui.navigation.Routes
import com.practice.reelbreak.ui.onboarding.component.ButtonGradient
import com.practice.reelbreak.ui.onboarding.component.FloatingImage
import com.practice.reelbreak.ui.component.GradientColor
import com.practice.reelbreak.ui.onboarding.component.IndicatorRow
import com.practice.reelbreak.ui.onboarding.component.OnboardDescription
import com.practice.reelbreak.ui.onboarding.component.OnboardHeading
import com.practice.reelbreak.ui.onboarding.component.OnboardPage
import com.practice.reelbreak.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import com.practice.reelbreak.R

@Composable
fun OnboardingScreen(
    navController: NavController,
    mainViewModel: MainViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    val pages = remember {
        listOf(
            OnboardPage.Welcome,
            OnboardPage.Counter,
            OnboardPage.Break,
            OnboardPage.Permission
        )
    }

    val pageState = rememberPagerState(initialPage = 0, pageCount = { pages.size })



    Column(modifier = Modifier.fillMaxSize()
        .background(GradientColor.background)
        .padding(16.dp)
    ) {

        if(pageState.currentPage !=pages.lastIndex) {
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
                OnboardPageContent(pages[index])
            }


        Spacer(modifier = Modifier.height(40.dp))
        IndicatorRow(
            currentPage = pageState.currentPage,
            totalPages = pages.size
        )
        Spacer(modifier = Modifier.height(32.dp))

        ButtonGradient(
            text = if (pageState.currentPage == pages.lastIndex) "Get Started" else "Continue",
            onClick = {
                if(pageState.currentPage < pages.size-1){
                    coroutineScope.launch {
                        pageState.animateScrollToPage(pageState.currentPage+1)
                    }

                }
                else{
                    mainViewModel.completeOnboarding()
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            }
        )
        Spacer(modifier = Modifier.height(40.dp))


    }

}



@Composable
fun OnboardPageContent(page: OnboardPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(18.dp),
    ) {
        FloatingImage(
            imageResId = page.imageRes,
            floatingDistance = 80f,
            size = 300.dp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OnboardHeading(page.title)

        Spacer(modifier = Modifier.height(16.dp))

        OnboardDescription(page.description)
    }
}




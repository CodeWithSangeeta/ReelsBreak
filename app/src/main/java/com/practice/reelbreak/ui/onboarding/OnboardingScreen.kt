package com.practice.reelbreak.ui.onboarding

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.practice.reelbreak.R
import com.practice.reelbreak.navigation.routes
import com.practice.reelbreak.ui.onboarding.component.ButtonGradient
import com.practice.reelbreak.ui.onboarding.component.GradientColor
import com.practice.reelbreak.ui.onboarding.component.IndicatorRow
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(navController: NavController) {
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
                    //  saveOnboardingDone()
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
            0 -> OnboardPageTrack()
            1 -> OnboardPageFocus()
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
            text = if(pageState.currentPage==3) "Get Started" else "Continue",
            onClick = {
                if(pageState.currentPage < 3){
                    coroutineScope.launch {
                        pageState.animateScrollToPage(pageState.currentPage+1)
                    }

                }
                else{
                  //  saveOnboardingDone()
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
fun OnboardPageTrack(modifier: Modifier = Modifier) {
    val currentPage by remember { mutableStateOf(0) }

    val infiniteTransition = rememberInfiniteTransition(label = "offsetY")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 80f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "offsetY"
    )


    Column(modifier = modifier
        .fillMaxSize()
        .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        ){
        Image(
        painter = painterResource(id = R.drawable.page_tracker_img),
            contentDescription = null,
            modifier = Modifier
                .size(300.dp)
                .offset(y = with(LocalDensity.current) { offsetY.toDp() })
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Track Your Reels Usage Effortlessly",
            fontSize = 33.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 40.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Monitor time spent across Instagram, YouTube Shorts, Snapchat, and Facebook Reels",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFAAA4A4),
            textAlign = TextAlign.Center
        )

    }
}



@Composable
fun OnboardPageFocus(modifier: Modifier = Modifier) {
    val currentPage by remember { mutableStateOf(1) }

    val infiniteTransition = rememberInfiniteTransition(label = "offsetY")

    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 120f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "offsetY"
    )


    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            painter = painterResource(id = R.drawable.page_focused_img),
            contentDescription = null,
            modifier = Modifier
                .size(160.dp)
                .offset(y = with(LocalDensity.current) { offsetY.toDp() })
        )
        Spacer(modifier = Modifier.height(150.dp))
        Text(
            text = "Stay Focused and Mindful Everyday",
            fontSize = 33.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.White,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 40.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Set daily limits. Start Foucus Mode. Reduce distracting apps and boost your wellbeing.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFFAAA4A4),
            textAlign = TextAlign.Center
        )

    }
}

@Composable
fun OnboardPageBreak(modifier: Modifier = Modifier) {
    
}

@Composable
fun OnboardPagePermissions(modifier: Modifier = Modifier) {
    
}




package com.practice.reelbreak.ui.onboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun IndicatorRow(currentPage:Int,totalPages:Int) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp,Alignment.CenterHorizontally)
    ){

    for(i in 0 until totalPages) {
        if (i == currentPage) {
            Box(
                modifier = Modifier
                    .height(10.dp)
                    .width(24.dp)
                    .background(
                         GradientColor.button,
                        shape = RoundedCornerShape(percent= 50)
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(
                        color = Color.Gray.copy(alpha = 0.35f),
                        shape = CircleShape
                    )
            )
        }
    }


    }
}
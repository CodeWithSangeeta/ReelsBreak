package com.sangeeta.reelbreak.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    var reelsCount by mutableIntStateOf(47)
        private set

    var timeSpent by mutableIntStateOf(142)
        private set
}

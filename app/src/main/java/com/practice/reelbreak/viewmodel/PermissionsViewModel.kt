package com.practice.reelbreak.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PermissionsViewModel(private val repository: PermissionRepository) : ViewModel() {

    // A single source of truth for the entire screen's state
    private val _viewState = MutableStateFlow(PermissionsViewState())
    val viewState = _viewState.asStateFlow()

    fun checkPermissions() {
        val accessibility = repository.isAccessibilityEnabled()
        val usage = repository.isUsageAccessEnabled()
        val overlay = repository.isOverlayEnabled()

        _viewState.update { currentState ->
            currentState.copy(
                isAccessibilityGranted = accessibility,
                isUsageGranted = usage,
                isOverlayGranted = overlay,
                // The continue button is only enabled if the core two are granted
                isContinueEnabled = accessibility && usage
            )
        }
    }
}

data class PermissionsViewState(
    val isAccessibilityGranted: Boolean = false,
    val isUsageGranted: Boolean = false,
    val isOverlayGranted: Boolean = false,
    val isContinueEnabled: Boolean = false
)
package com.practice.reelbreak.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.core.permission.OverlayPermissionChecker
import com.practice.reelbreak.core.permission.UsagePermissionChecker
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.PermissionState
import com.practice.reelbreak.ui.focused_mode.isAccessibilityServiceEnabled
import com.practice.reelbreak.ui.permission.PermissionSheetType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.practice.reelbreak.ui.permission.PermissionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PermissionSheetState(
    val isVisible: Boolean = false,
    val type: PermissionSheetType? = null,
)

@HiltViewModel
class PermissionsViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel(

) {

    private val _uiState = MutableStateFlow(PermissionUiState())
    val uiState = _uiState.asStateFlow()

//    private val _events = MutableSharedFlow<PermissionAction>()
//    val events = _events.asSharedFlow()

    // --- Sheet state ---
    private val _sheetState = MutableStateFlow(PermissionSheetState())
    val sheetState: StateFlow<PermissionSheetState> = _sheetState.asStateFlow()


    private var lastAccessibilityGranted: Boolean? = null

    var overlayToggleRequested: Boolean = false
        private set

    fun markOverlayToggleRequested() {
        overlayToggleRequested = true
    }

    fun clearOverlayToggleRequested() {
        overlayToggleRequested = false
    }


    fun refreshPermissionState(context: Context) {
        val newState = PermissionState(
            accessibilityGranted = AccessibilityPermissionChecker.isAccessibilityEnabled(context),
            usageStatsGranted = UsagePermissionChecker.isUsageAccessGranted(context),
            overlayGranted = OverlayPermissionChecker.isOverlayEnabled(context)
        )

        val prev = lastAccessibilityGranted
        lastAccessibilityGranted = newState.accessibilityGranted

        // Update uiState as you already do
        _uiState.update { it.copy(permissionState = newState) }

        // Auto-turn on strict when accessibility just became true
        if (prev == false && newState.accessibilityGranted) {
            viewModelScope.launch {
                userPreferencesRepository.setStrictMode(true)
            }
        }

        if (overlayToggleRequested && newState.overlayGranted) {
            overlayToggleRequested = false
            viewModelScope.launch {
                userPreferencesRepository.setOverlayEnabled(true)
            }
        }
    }

//        fun checkAndShowSheetIfNeeded(context: Context) {
//            val hasAccessibility = AccessibilityPermissionChecker.isAccessibilityEnabled(context)
//            val hasUsage = UsagePermissionChecker.isUsageAccessGranted(context)
//            when {
//                !hasAccessibility -> showSheet(PermissionSheetType.ACCESSIBILITY)
//                !hasUsage -> showSheet(PermissionSheetType.USAGE_ACCESS)
//                // both granted → do nothing
//            }
//        }

    fun checkAndShowSheetIfNeeded(context: Context) {
        val isGranted = isAccessibilityServiceEnabled(context)

        // Always update the state
        _uiState.update {
            it.copy(permissionState = it.permissionState.copy(accessibilityGranted = isGranted))
        }

        // ✅ Only open sheet if NOT granted — never open if already granted
        if (!isGranted && !_sheetState.value.isVisible) {
            showSheet(PermissionSheetType.ACCESSIBILITY)
        }
    }

        fun showSheet(type: PermissionSheetType) {
            _sheetState.value = PermissionSheetState(isVisible = true, type = type)
        }

        fun showOverlaySheet() {
            _sheetState.value =
                PermissionSheetState(isVisible = true, type = PermissionSheetType.OVERLAY)
        }

        fun dismissSheet() {
            _sheetState.value = PermissionSheetState(isVisible = false, type = null)
        }

        fun onPermissionSheetAgree(context: Context, type: PermissionSheetType) {
            dismissSheet()
            when (type) {
                is PermissionSheetType.ACCESSIBILITY -> AccessibilityPermissionChecker.openAccessibilitySettings(
                    context
                )

                is PermissionSheetType.USAGE_ACCESS -> UsagePermissionChecker.openUsageAccessSettings(
                    context
                )

                is PermissionSheetType.OVERLAY -> {
                    markOverlayToggleRequested()
                    OverlayPermissionChecker.openOverlaySettings(context)
                }
            }
        }



    fun updateAccessibilityGranted(granted: Boolean) {
        _uiState.update {
            it.copy(permissionState = it.permissionState.copy(accessibilityGranted = granted))
        }
    }

    }

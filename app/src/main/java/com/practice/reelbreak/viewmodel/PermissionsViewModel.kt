package com.practice.reelbreak.viewmodel

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.core.permission.OverlayPermissionChecker
import com.practice.reelbreak.core.permission.UsagePermissionChecker
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.PermissionState
import com.practice.reelbreak.ui.focusedmode.isAccessibilityServiceEnabled
import com.practice.reelbreak.ui.permission.BulletIconType
import com.practice.reelbreak.ui.permission.PermissionAction
import com.practice.reelbreak.ui.permission.PermissionSheetType
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import com.practice.reelbreak.ui.permission.PermissionStatus
import com.practice.reelbreak.ui.permission.PermissionType
import com.practice.reelbreak.ui.permission.PermissionUiModel
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

    private val _events = MutableSharedFlow<PermissionAction>()
    val events = _events.asSharedFlow()

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


        fun buildPermissionCards(state: PermissionState): List<PermissionUiModel> {
            return listOf(
                //Accessibility
                PermissionUiModel(
                    id = PermissionType.ACCESSIBILITY,
                    title = "Accessibility Access",
                    icon = Icons.Default.Visibility,
                    description = "Used only to detect when you scroll reels and trigger mindful breaks.",
                    status = if (state.accessibilityGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
                    buttonTextGranted = "Enabled",
                    buttonTextNotGranted = "Enable",
                    buttonColorGranted = Color(0xFF8E44AD),
                    buttonColorNotGranted = Color(0xFF8E44AD),
                    bulletPoints = listOf(
                        "Detect reel scrolling event",
                        "No reading messages",
                        "No screen recording",
                        "No personal content access"
                    ),
                    bulletIconMap = mapOf(
                        "Detect reel scrolling event" to BulletIconType.Check,
                        "No reading messages" to BulletIconType.Cross,
                        "No screen recording" to BulletIconType.Cross,
                        "No personal content access" to BulletIconType.Cross
                    )
                ),

                //Usage Access
                PermissionUiModel(
                    id = PermissionType.USAGE_ACCESS,
                    title = "Usage Access",
                    icon = Icons.Default.BarChart,
                    description = "Used to calculate daily time spent on Instagram, YouTube Shorts, TikTok, etc.",
                    status = if (state.usageStatsGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
                    buttonTextGranted = "Enabled",
                    buttonTextNotGranted = "Enable",
                    buttonColorGranted = Color(0xFF3498DB),
                    buttonColorNotGranted = Color(0xFF3498DB),
                    bulletPoints = listOf(
                        "Measure time spent per app",
                        "Daily usage summary",
                        "No browsing history collected"
                    ),
                    bulletIconMap = mapOf(
                        "Measure time spent per app" to BulletIconType.Check,
                        "Daily usage summary" to BulletIconType.Check,
                        "No browsing history collected" to BulletIconType.Cross
                    )
                ),
                PermissionUiModel(
                    id = PermissionType.OVERLAY,
                    title = "Overlay Permission",
                    icon = Icons.Default.Fullscreen,
                    description = "Optional. Helps show reminders above other apps.",
                    status = if (state.overlayGranted) PermissionStatus.Granted else PermissionStatus.NotGranted,
                    isOptional = true,
                    buttonTextGranted = "Enabled ✓",
                    buttonTextNotGranted = "Allow Overlay",
                    buttonColorGranted = Color(0xFF3498DB),
                    buttonColorNotGranted = Color(0xFF3498DB),
                    bulletPoints = listOf(
                        "Why is this useful?",
                        "Track reels in real time with small counter bubble",
                        "ReelsBreak does not record screen or collect data"
                    ),
                    bulletIconMap = mapOf(
                        "Why is this useful?" to BulletIconType.Question,
                        "Track reels in real time with small counter bubble" to BulletIconType.Check,
                        "ReelsBreak does not record screen or collect data" to BulletIconType.Check
                    )
                )
            )
        }

        fun onCardExpansionToggled(cardId: PermissionType) {
            _uiState.value = _uiState.value.copy(
                expandedCardId = if (_uiState.value.expandedCardId == cardId) null else cardId
            )
        }

        fun onPermissionEnableClicked(type: PermissionType) {
            viewModelScope.launch {
                when (type) {
                    PermissionType.ACCESSIBILITY ->
                        _events.emit(PermissionAction.OpenAccessibilitySettings)

                    PermissionType.USAGE_ACCESS ->
                        _events.emit(PermissionAction.OpenUsageAccessSettings)

                    PermissionType.OVERLAY ->
                        _events.emit(PermissionAction.OpenOverlaySettings)
                }
            }
        }



    fun updateAccessibilityGranted(granted: Boolean) {
        _uiState.update {
            it.copy(permissionState = it.permissionState.copy(accessibilityGranted = granted))
        }
    }

    }

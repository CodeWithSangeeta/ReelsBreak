package com.practice.reelbreak.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practice.reelbreak.core.permission.AccessibilityPermissionChecker
import com.practice.reelbreak.data.preferences.UserPreferencesRepository
import com.practice.reelbreak.domain.model.PermissionState
import com.practice.reelbreak.ui.focused_mode.isAccessibilityServiceEnabled
import com.practice.reelbreak.ui.permission.PermissionSheetType
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _sheetState = MutableStateFlow(PermissionSheetState())
    val sheetState: StateFlow<PermissionSheetState> = _sheetState.asStateFlow()


    private var lastAccessibilityGranted: Boolean? = null

    fun refreshPermissionState(context: Context) {
        val newState = PermissionState(
            accessibilityGranted = AccessibilityPermissionChecker.isAccessibilityEnabled(context),
        )

        val prev = lastAccessibilityGranted
        lastAccessibilityGranted = newState.accessibilityGranted
        _uiState.update { it.copy(permissionState = newState) }

        if (prev == false && newState.accessibilityGranted) {
            viewModelScope.launch {
                userPreferencesRepository.setStrictMode(true)
            }
        }
    }

    fun checkAndShowSheetIfNeeded(context: Context) {
        val isGranted = isAccessibilityServiceEnabled(context)

        _uiState.update {
            it.copy(permissionState = it.permissionState.copy(accessibilityGranted = isGranted))
        }

        if (!isGranted && !_sheetState.value.isVisible) {
            showSheet(PermissionSheetType.ACCESSIBILITY)
        }
    }

        fun showSheet(type: PermissionSheetType) {
            _sheetState.value = PermissionSheetState(isVisible = true, type = type)
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

            }
        }


    fun updateAccessibilityGranted(granted: Boolean) {
        _uiState.update {
            it.copy(permissionState = it.permissionState.copy(accessibilityGranted = granted))
        }
    }

    }

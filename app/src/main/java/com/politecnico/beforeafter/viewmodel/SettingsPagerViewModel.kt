package com.politecnico.beforeafter.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.politecnico.beforeafter.data.model.SettingsPagerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Settings Pager ViewModel used only for getting uiState

@RequiresApi(Build.VERSION_CODES.O)
class SettingsPagerViewModel : ViewModel() {
    private val _privateState = MutableStateFlow(SettingsPagerUiState())
    val publicState: StateFlow<SettingsPagerUiState> = _privateState.asStateFlow()
}
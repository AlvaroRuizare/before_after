package com.politecnico.beforeafter.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

// ViewModelFactory allows for the ViewModel to be global and survive configuration changes

class SettingsPagerViewModelFactory : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsPagerViewModel::class.java)) {
            return SettingsPagerViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
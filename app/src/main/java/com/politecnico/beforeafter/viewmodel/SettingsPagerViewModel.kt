package com.politecnico.beforeafter.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.politecnico.beforeafter.data.model.SettingsPagerUiState
import com.politecnico.beforeafter.services.BackgroundService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@RequiresApi(Build.VERSION_CODES.O)
class SettingsPagerViewModel : ViewModel() {
    private val _estadoPrivado = MutableStateFlow(SettingsPagerUiState())
    val estadoPublico: StateFlow<SettingsPagerUiState> = _estadoPrivado.asStateFlow()

    /**
     * Al iniciar el ViewModel...
     */
    init {
        // Actualizar algo
        _estadoPrivado.update { estadoActual ->
            estadoActual.copy(
                //listaTiposUsuario = tipoUsuarioDao.getAllTiposUsuario()
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startBackgroundService(context : Context){
        val intent = Intent(context, BackgroundService::class.java)
        context.startForegroundService(intent)
    }
}
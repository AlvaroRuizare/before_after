package com.politecnico.beforeafter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController

data class AjusteData(val titulo: String, val intentAjuste: String?)


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AjustesPager(navController: NavHostController, activity : Activity) {
    val context = LocalContext.current

    ContextCompat.startForegroundService(context, Intent(context, Servicio2Plano::class.java))

    // Iniciar servicio segundo plano
    val intent = Intent(context, Servicio2Plano::class.java)
    context.startForegroundService(intent)

    // Pager state contiene la información del HorizontalPager actualizada
    val pagerState = rememberPagerState(pageCount = { 4 })

    val ajustes = listOf(
        AjusteData("Acceso de uso", Settings.ACTION_USAGE_ACCESS_SETTINGS),
        AjusteData("Optimización batería", Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
        AjusteData("Notificaciones", Settings.ACTION_APP_NOTIFICATION_SETTINGS),
        AjusteData("Todo listo?", null)
    )

    HorizontalPager(state = pagerState) { page ->
        val ajuste = ajustes[page]
        AjusteScreen(ajuste, activity, navController)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AjusteScreen(ajuste: AjusteData, activity: Activity, navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(ajuste.titulo, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            if(ajuste.intentAjuste == null){
                navController.navigate(route = Pantallas.PantallaConfiguracion.route)
            } else {
                val intent = Intent(ajuste.intentAjuste)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.apply {
                    putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                try {
                    activity.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(activity, "No se pudo abrir el ajuste", Toast.LENGTH_SHORT).show()
                }
            }


        }) {
            Text("Ir a ajustes")
        }
    }
}
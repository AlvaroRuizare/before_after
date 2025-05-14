package com.politecnico.beforeafter

import android.annotation.SuppressLint
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


// Aquí es donde se usa la librería de navegación que metemos en el build.gradle
@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navegacion() {
    val navController = rememberNavController() // Creamos variable con el NavController por defecto para enviarsela al NavHost

    // NavHost permite crear flujos de navegación entre distintos Composables
    NavHost(navController = navController,
        startDestination = Pantallas.PantallaAjustesPager.route) // Se define la pantalla con la que iniciar por defecto
    {
        composable(route = Pantallas.PantallaAjustesPager.route) { // Indicamos ruta definida en Pantallas.kt
            // Enviamos instancia del ViewModel para que la View reciba funcionalidades del ViewModel
            val activity = LocalActivity.current

            activity?.let { // Si activity no es null...
                AjustesPager(navController, it) // Se usa valor de activity que no es null (it)
            }
        }

        composable(route = Pantallas.PantallaConfiguracion.route) { // Indicamos ruta definida en Pantallas.kt
            // Enviamos instancia del ViewModel para que la View reciba funcionalidades del ViewModel
            Configuracion()
        }
    }
}
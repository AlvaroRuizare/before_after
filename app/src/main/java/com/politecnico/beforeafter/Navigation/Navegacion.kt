package com.example.apppracticasjc.Navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.politecnico.beforeafter.Navigation.Pantallas
import com.politecnico.beforeafter.View.AjustesPager


// Aquí es donde se usa la librería de navegación que metemos en el build.gradle
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
            AjustesPager(navController) // Indicamos Composable
        }
    }
}
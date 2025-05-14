package com.politecnico.beforeafter

// Diferentes pantallas entre las que podemos navegar
sealed class Pantallas(val route : String) { // Clase que recibe parámetro ruta
    object PantallaAjustesPager : Pantallas("pantalla_ajustes_pager")
    object PantallaConfiguracion : Pantallas("pantalla_configuracion")
}

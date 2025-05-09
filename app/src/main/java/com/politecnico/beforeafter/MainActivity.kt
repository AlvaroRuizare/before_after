package com.politecnico.beforeafter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.apppracticasjc.Navigation.Navegacion
import com.politecnico.beforeafter.ui.theme.BeforeAfterTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeforeAfterTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    Navegacion()
                }
            }
        }
    }
}
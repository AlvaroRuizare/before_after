package com.politecnico.beforeafter.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.politecnico.beforeafter.navigation.AppScreens
import com.politecnico.beforeafter.ui.theme.DaydreamFont

// Screen template for each of the HorizontalPager screens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PagerScreen(ajuste: PagerContent, activity: Activity, navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Setting image
        ajuste.imagen?.let {
            Image(
                painter = painterResource(it),
                contentDescription = ""
            )
        }
        Spacer(modifier = Modifier.height(16.dp))


        // Setting text
        Text(
            text = ajuste.titulo,
            fontFamily = DaydreamFont,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(40.dp))


        // Button to go to setting (or continue if it's the last page)
        Button(
            modifier = Modifier.height(70.dp),
            border = BorderStroke(2.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Black,
            ),
            onClick = {
                // If it's the last page...
                if(ajuste.intentAjuste == null){
                    // Button redirects to Before & After settings
                    navController.navigate(route = AppScreens.BeforeAfterSettings.route)
                } else { // If it's one of the setting pages
                    // Prepare intent
                    val intent = Intent(ajuste.intentAjuste)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.apply { putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName) }

                    // Go to required setting
                    try {
                        activity.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(activity, "Couldn't open setting", Toast.LENGTH_SHORT).show()
                    }
                }
        }) {
            // Si es última página (no hay intent)
            if(ajuste.intentAjuste == null){
                Text(
                    text="Get started",
                    fontFamily = DaydreamFont,
                    color = Color.Black
                )
            } else { // Si es página de ajuste...
                Text(
                    text="Enable",
                    fontFamily = DaydreamFont,
                    color = Color.Black
                )
            }
        }
    }
}
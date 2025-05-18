package com.politecnico.beforeafter.view

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.politecnico.beforeafter.navigation.AppScreens

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PagerScreen(ajuste: PagerContent, activity: Activity, navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Pager screen text
        Text(ajuste.titulo, style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Pager screen button
        Button(onClick = {
            // If it's the last page...
            if(ajuste.intentAjuste == null){
                // Button redirects to Before & After settings
                navController.navigate(route = AppScreens.BeforeAfterSettings.route)
            } else {
                val intent = Intent(ajuste.intentAjuste)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.apply {
                    putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, context.packageName)
                }
                try {
                    activity.startActivity(intent) // Go to required setting
                } catch (e: Exception) {
                    Toast.makeText(activity, "Couldn't open setting", Toast.LENGTH_SHORT).show()
                }
            }
        }) {
            if(ajuste.intentAjuste == null){
                Text("Get started")
            } else {
                Text("Enable")
            }
        }
    }
}
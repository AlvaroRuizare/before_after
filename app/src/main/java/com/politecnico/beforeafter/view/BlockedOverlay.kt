package com.politecnico.beforeafter.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.politecnico.beforeafter.R
import com.politecnico.beforeafter.ui.theme.DaydreamFont
import com.politecnico.beforeafter.ui.theme.PixelOperatorFont

class BlockedOverlay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appPackageName = intent.getStringExtra("APP_PACKAGE_NAME") ?: "App is null"
        val prefs = applicationContext.getSharedPreferences("app_locks", Context.MODE_PRIVATE)
        val unlockTime = prefs.getLong("${appPackageName}_unlockAt", 0L)
        val currentTime = System.currentTimeMillis()

        val timeLeftMillis = unlockTime - currentTime


        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = Color.White
            ) {
                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Image(
                        painter = painterResource(R.drawable.blocked),
                        contentDescription = "Warning"
                    )
                    Spacer(Modifier.padding(16.dp))
                    Text(
                        text = "BLOCKED APP",
                        fontFamily = DaydreamFont,
                        color = Color.Black,
                        fontSize = 30.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.padding(16.dp))
                    Text(
                        text = "> The app is currently blocked.\n\n> Wait ${formatMillisToMinSec(timeLeftMillis)} for it to unlock",
                        fontFamily = PixelOperatorFont,
                        color = Color.Black,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.padding(16.dp))
                    OutlinedButton(
                        onClick = { finish() },
                        modifier = Modifier.height(70.dp).fillMaxWidth().padding(8.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledContentColor = Color.Black,
                        ),
                    ) {
                        Text(
                            text="Continue",
                            fontFamily = DaydreamFont
                        )
                    }
                }
            }
        }
    }

    fun formatMillisToMinSec(millis: Long): String {
        val totalSeconds = millis / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "${minutes}m ${seconds}s"
    }
}
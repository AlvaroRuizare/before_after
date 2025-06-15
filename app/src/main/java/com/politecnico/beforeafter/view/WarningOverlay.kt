package com.politecnico.beforeafter.view

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.politecnico.beforeafter.R
import com.politecnico.beforeafter.ui.theme.DaydreamFont
import com.politecnico.beforeafter.ui.theme.PixelOperatorFont
import kotlinx.coroutines.delay

class WarningOverlay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appPackageName = intent.getStringExtra("APP_PACKAGE_NAME") ?: "App is null"
        val beforeSecondsMs = intent.getLongExtra("beforeSecondsMs", 0L)

        setContent {

            val unlockTime = System.currentTimeMillis() + beforeSecondsMs
            val millisLeft = unlockTime - System.currentTimeMillis()
            val secondsLeft = millisLeft / 1000

            var countdown by remember { mutableStateOf(secondsLeft) }

            var isEnabled by remember { mutableStateOf(false) }

            // Countdown effect: updates every second
            LaunchedEffect(Unit) {
                while (countdown > 0) {
                    delay(1000)
                    countdown--
                }
                isEnabled = true
            }

            val buttonText = if (isEnabled) {
                "Continue"
            } else {
                "Wait ($countdown)"
            }

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
                        painter = painterResource(R.drawable.warning),
                        contentDescription = "Warning"
                    )
                    Spacer(Modifier.padding(16.dp))
                    Text(
                        text = "WARNING!",
                        fontFamily = DaydreamFont,
                        color = Color.Black,
                        fontSize = 40.sp
                    )
                    Spacer(Modifier.padding(16.dp))
                    Text(
                        text = "> You're about to open a limited app.\n\n> Are you sure?",
                        fontFamily = PixelOperatorFont,
                        color = Color.Black,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.padding(16.dp))
                    OutlinedButton(
                        onClick = { navigateToNow(appPackageName) },
                        enabled = isEnabled,
                        modifier = Modifier.height(70.dp).fillMaxWidth().padding(8.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            disabledContainerColor = Color.White,
                            disabledContentColor = Color.Black
                        ),
                    ) {
                        Text(
                            text = buttonText,
                            fontFamily = DaydreamFont
                        )
                    }
                }
            }
        }
    }

    private fun navigateToNow(appPackageName: String) {
        val intent = Intent(this, NowOverlay::class.java).apply {
            putExtra("APP_PACKAGE_NAME", appPackageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
    }
}
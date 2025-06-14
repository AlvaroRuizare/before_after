package com.politecnico.beforeafter.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.rememberPickerState
import com.example.apppracticasjc.Data.RoomDB.BeforeAfterDB
import com.politecnico.beforeafter.services.AppBlockingService
import com.politecnico.beforeafter.ui.theme.DaydreamFont
import com.politecnico.beforeafter.ui.theme.PixelOperatorFont
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NowOverlay : ComponentActivity() {
    var nowMinutesMs = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appPackageName = intent.getStringExtra("APP_PACKAGE_NAME") ?: "App is null"

        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize(),
                color = Color.White
            ) {
                // NOW values
                val valuesNow = remember { (1..60).map { it.toString() } }
                val valuesNowPickerState = rememberPickerState(
                    initialNumberOfOptions = 60
                )
                var selectedMinutes = 10

                Column (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ){
                    Spacer(Modifier.padding(16.dp))
                    Text(
                        text = "NOW",
                        fontFamily = DaydreamFont,
                        color = Color.Black,
                        fontSize = 40.sp
                    )
                    Spacer(Modifier.padding(16.dp))
                    Text(
                        text = "> How many minutes do you want to be using this app for?",
                        fontFamily = PixelOperatorFont,
                        color = Color.Black,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.padding(16.dp))
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    ) {
                        Picker(
                            state = valuesNowPickerState,
                            items = valuesNow,
                            visibleItemsCount = 3,
                            textModifier = Modifier.padding(8.dp),
                            textStyle = TextStyle(fontSize = 32.sp),
                            dividerColor = Color.Black,
                            startIndex = 9,
                            onValueSelected = { value ->
                                selectedMinutes = value.toInt()
                            }
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    OutlinedButton(
                        onClick = { startCountdownService(selectedMinutes, appPackageName) },
                        modifier = Modifier
                            .height(70.dp)
                            .fillMaxWidth()
                            .padding(8.dp),
                        border = BorderStroke(2.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black,
                            disabledContainerColor = Color.Transparent,
                            disabledContentColor = Color.Black,
                        ),
                    ) {
                        Text(
                            text="Continue",
                            fontFamily = DaydreamFont,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }

    private fun startCountdownService(nowMinutes : Int, appPackageName: String) {
        val context = this
        val db = BeforeAfterDB.getDatabase(context)
        val limitedAppsDao = db.limitedAppsDao()

        // Start countdown service and disable block on database
        CoroutineScope(Dispatchers.IO).launch {
            limitedAppsDao.updateLimited(appPackageName, false)
            finish()

            nowMinutesMs = nowMinutes.toLong() * 60000L

            // Get 'AFTER' duration
            val afterMinutesMs = db.settingsDao().getSettings()?.afterMinutes?.times(60000L)

            // start timer service
            val intent = Intent(context, AppBlockingService::class.java).apply {
                putExtra("appPackageName", appPackageName)
                putExtra("nowMinutesMs", nowMinutesMs)
                putExtra("afterMinutesMs", afterMinutesMs)
            }
            context.startService(intent)
        }
    }
}
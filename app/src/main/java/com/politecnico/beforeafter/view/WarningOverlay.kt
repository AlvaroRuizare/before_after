package com.politecnico.beforeafter.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.politecnico.beforeafter.R
import com.politecnico.beforeafter.ui.theme.DaydreamFont
import com.politecnico.beforeafter.ui.theme.PixelOperatorFont

class WarningOverlay : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optional: Auto-dismiss after X seconds
        // Handler(Looper.getMainLooper()).postDelayed({ finish() }, 5000)

        setContent {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red)
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
                        onClick = { finish() },
                        modifier = Modifier.height(70.dp).fillMaxWidth().padding(8.dp),
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
}
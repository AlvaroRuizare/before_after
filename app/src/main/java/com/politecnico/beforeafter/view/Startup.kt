package com.politecnico.beforeafter.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.politecnico.beforeafter.navigation.AppScreens
import com.politecnico.beforeafter.ui.theme.DaydreamFont

@Composable
fun Startup(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .wrapContentSize(Alignment.Center),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "B&A",
            fontSize = 75.sp,
            modifier = Modifier.padding(top = 35.dp),
            fontFamily = DaydreamFont,
            textAlign = TextAlign.Center
        )
        Text(
            text = "(before & after)",
            color = Color.Red,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(60.dp))
        Box(
            modifier = Modifier.clip(CutCornerShape(40.dp)).background(Color.Black).padding(20.dp)
        ) {
            Text(
                text="For the app to work properly, we need you to adjust some of your system settings.\n\nDon't worry, your data or phone usage won't be used for our own benefit.",
                fontFamily = DaydreamFont,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
        Spacer(Modifier.height(60.dp))
        OutlinedButton(
            onClick = {navController.navigate(AppScreens.SettingsPagerScreen.route)},
            modifier = Modifier.height(70.dp),
            border = BorderStroke(2.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.Black,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.Black,
            ),
        ) {
            Text(
                text="Setup settings",
                fontFamily = DaydreamFont,
                color = Color.Black
            )
        }
    }
}
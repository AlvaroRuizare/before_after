package com.politecnico.beforeafter.view

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.apppracticasjc.Data.RoomDB.BeforeAfterDB
import com.example.apppracticasjc.Data.RoomDB.LimitedAppEntity
import com.politecnico.beforeafter.navigation.AppScreens
import com.politecnico.beforeafter.ui.theme.DaydreamFont
import com.politecnico.beforeafter.viewmodel.AppSelectorViewModel
import com.politecnico.beforeafter.viewmodel.AppSelectorViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AppSelector(navController: NavController) {
    val context = LocalContext.current

    // Global ViewModel that survives configuration changes
    val appSelectorViewModel : AppSelectorViewModel = viewModel(
        factory = AppSelectorViewModelFactory(
            BeforeAfterDB.getDatabase(context).limitedAppsDao()
        )
    )

    val allApps = remember { getInstalledApps(context) } // Get all installed apps
    val checkedApps = remember { mutableStateListOf<ApplicationInfo>() } // Apps that are checked in the list
    val coroutineScope = rememberCoroutineScope()

    // Add apps saved in DB as checked
    var dbLimitedApps by remember { mutableStateOf<List<LimitedAppEntity>>(emptyList()) }
    LaunchedEffect(Unit) {
        // Get apps from database and include them in the checked apps list
        dbLimitedApps = appSelectorViewModel.getLimitedApps()
        val dbLimitedPackageNames = dbLimitedApps.map { it.packageName } // Get only packageName

        // For each app...
        val dbCheckedApps = allApps.filter {
            // Save app if the packageName is contained inside dbLimitedPackageNames
            app -> dbLimitedPackageNames.contains(app.packageName)
        }

        // Add checked DB apps to the checkedApps list
        checkedApps.addAll(dbCheckedApps)
    }


    Column(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding()
    ) {
        // Apps list
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            // For every app...
            items(allApps) { app ->
                val isSelected = checkedApps.contains(app) // It's selected if it's cointained in 'checkedApps'

                // Show app as row
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { // When app clicked...
                            if (isSelected) // If it's already selected...
                                checkedApps.remove(app) // Take off 'checkedApps' list
                            else // If it's not selected...
                                checkedApps.add(app) // Add to 'checkedApps' list
                        }
                        .padding(16.dp)
                ) {
                    Image(
                        painter = drawableToPainter(drawable = app.loadIcon(context.packageManager)),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Text(app.loadLabel(context.packageManager).toString())
                    Spacer(Modifier.weight(1f))
                    Checkbox(checked = isSelected, onCheckedChange = null)
                }
            }
        }

        // 'Save selected' button
        OutlinedButton(
            onClick = {
                // Convert 'checkedApps' into a LimitedAppEntity list called 'appsToSave'
                val appsToSave = checkedApps.map {
                    LimitedAppEntity(
                        packageName = it.packageName,
                        appName = it.loadLabel(context.packageManager).toString()
                    )
                }

                coroutineScope.launch {
                    appSelectorViewModel.saveApps(appsToSave) // Save checked apps
                    navController.navigate(AppScreens.BeforeAfterSettings.route) // Navigate back
                }
            },
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
                text="Save selected",
                fontFamily = DaydreamFont,
                color = Color.Black
            )
        }
    }
}


/**
 * Get apps installed in the device
 */
fun getInstalledApps(context: Context): List<ApplicationInfo> {
    val pm = context.packageManager
    return pm.getInstalledApplications(PackageManager.GET_META_DATA).filter {
        // Only apps that can be opened, not system apps or services
        pm.getLaunchIntentForPackage(it.packageName) != null
    }
}


/**
 * Convert drawable to painter
 */
@Composable
fun drawableToPainter(drawable: Drawable): Painter {
    return remember(drawable) {
        BitmapPainter(drawable.toBitmap().asImageBitmap())
    }
}
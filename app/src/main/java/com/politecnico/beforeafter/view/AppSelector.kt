package com.politecnico.beforeafter.view

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
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
import com.politecnico.beforeafter.viewmodel.AppSelectorViewModel
import com.politecnico.beforeafter.viewmodel.AppSelectorViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AppSelector(navController: NavController) {
    val context = LocalContext.current
    val appSelectorViewModel : AppSelectorViewModel = viewModel( // Global ViewModel that survives configuration changes
        factory = AppSelectorViewModelFactory(
            BeforeAfterDB.getDatabase(context).limitedAppsDao()
        )
    )

    val allApps = remember { getInstalledApps(context) }
    val selectedApps = remember { mutableStateListOf<ApplicationInfo>() }
    val coroutineScope = rememberCoroutineScope()

    var limitedApps by remember { mutableStateOf<List<LimitedAppEntity>>(emptyList()) }
    LaunchedEffect(Unit) {
        limitedApps = appSelectorViewModel.getLimitedApps()

        val limitedPackageNames = limitedApps.map { it.packageName }

        val selectedAppsFromDB = allApps.filter { app -> limitedPackageNames.contains(app.packageName) }
        selectedApps.addAll(selectedAppsFromDB)
    }


    Column(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(allApps) { app ->
                val isSelected = selectedApps.contains(app)
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isSelected)
                                selectedApps.remove(app)
                            else
                                selectedApps.add(app)
                        }
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberDrawablePainter(drawable = app.loadIcon(context.packageManager)),
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

        Button(
            onClick = {
                val appsToSave = selectedApps.map {
                    LimitedAppEntity(
                        packageName = it.packageName,
                        appName = it.loadLabel(context.packageManager).toString()
                    )
                }

                coroutineScope.launch {
                    appSelectorViewModel.saveApps(appsToSave)
                    navController.navigate(AppScreens.BeforeAfterSettings.route)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text("Guardar selección")
        }
    }


}

fun getInstalledApps(context: Context): List<ApplicationInfo> {
    val pm = context.packageManager
    return pm.getInstalledApplications(PackageManager.GET_META_DATA)
        .filter {
        // Solo apps que tienen un intent para ser abiertas (con ícono)
        pm.getLaunchIntentForPackage(it.packageName) != null
    }
}

@Composable
fun rememberDrawablePainter(drawable: Drawable): Painter {
    return remember(drawable) {
        BitmapPainter(drawable.toBitmap().asImageBitmap())
    }
}
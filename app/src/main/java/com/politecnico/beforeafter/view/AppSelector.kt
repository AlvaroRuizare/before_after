package com.politecnico.beforeafter.view

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.apppracticasjc.Data.RoomDB.LimitedAppEntity
import kotlinx.serialization.Serializable

@Composable
fun AppSelector() {
    val context = LocalContext.current
    val apps = remember { getInstalledApps(context) }
    val selectedApps = remember { mutableStateListOf<ApplicationInfo>() }

    LazyColumn {
        items(apps) { app ->
            val isSelected = selectedApps.contains(app)
            Row(
                Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isSelected) selectedApps.remove(app)
                        else selectedApps.add(app)
                    }
                    .padding(16.dp)
            ) {
                Icon(
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

            //viewmodel.guardarapps
            //onSaveSelection(appsToSave)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Guardar selección")
    }
}

fun getInstalledApps(context: Context): List<ApplicationInfo> {
    val pm = context.packageManager
    return pm.getInstalledApplications(PackageManager.GET_META_DATA)
        .filter { pm.getLaunchIntentForPackage(it.packageName) != null }
}

@Composable
fun rememberDrawablePainter(drawable: Drawable): Painter {
    return remember(drawable) {
        BitmapPainter(drawable.toBitmap().asImageBitmap())
    }
}
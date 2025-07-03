package ai.neurode.packageviztester

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import ai.neurode.packageviztester.ui.theme.PackageVizTesterTheme
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PackageVizTesterTheme {
                val context = LocalContext.current
                val pm = context.packageManager
                var resultText by remember { mutableStateOf("") }
                val resultLines = resultText.split("\n").filter { it.isNotBlank() }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                        // Buttons for each PackageManager API
                        Button(onClick = {
                            val pkgs = pm.getInstalledPackages(0)
                            resultText = "getInstalledPackages():\n" + pkgs.joinToString("\n") { it.packageName }
                        }) {
                            Text("Test getInstalledPackages()")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            val apps = pm.getInstalledApplications(0)
                            resultText = "getInstalledApplications():\n" + apps.joinToString("\n") { it.packageName }
                        }) {
                            Text("Test getInstalledApplications()")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            val pkgs = pm.getInstalledPackages(0)
                            val info = pkgs.firstOrNull()?.let {
                                try {
                                    pm.getPackageInfo(it.packageName, 0)
                                } catch (e: Exception) { null }
                            }
                            resultText = if (info != null) {
                                "getPackageInfo():\n${info.packageName} v${info.versionName}"
                            } else {
                                "getPackageInfo():\nNo package found"
                            }
                        }) {
                            Text("Test getPackageInfo()")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            val intent = Intent(Intent.ACTION_MAIN, null).apply {
                                addCategory(Intent.CATEGORY_LAUNCHER)
                            }
                            val acts = pm.queryIntentActivities(intent, 0)
                            resultText = "queryIntentActivities():\n" + acts.joinToString("\n") { it.activityInfo.packageName + ": " + it.activityInfo.name }
                        }) {
                            Text("Test queryIntentActivities()")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(resultLines) { line ->
                                Text(line)
                                Divider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PackageVizTesterTheme {
        Greeting("Android")
    }
}
package com.example.clientapplication.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.networkutils.core.client.sendMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AppClientScreen() {
    MaterialTheme {
        val ipAddress = remember { mutableStateOf(TextFieldValue("192.168.101.149")) }
        var port by remember { mutableStateOf("8000") }
        val textReceive: MutableState<String?> = remember { mutableStateOf("") }
        var checked by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = ipAddress.value,
                onValueChange = { ipAddress.value = it },
                label = { Text("Enter IP Address") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.padding(20.dp))
            TextField(
                value = port,
                onValueChange = { port = it },
                label = { Text("Enter Port") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.padding(20.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Switch(checked = checked, onCheckedChange = {
                    checked = it
                    port = if (!checked) "8000" else "8080"
                })
                Text(if (checked) "SSL ON" else "SSL OFF")
            }
            Spacer(Modifier.padding(20.dp))
            val functionName = listOf("Check Connection", "Gretting to Server", "Tell My IP")
            val routeCommand = listOf("/", "/Greeting", "/GetIp")
            val functionRouteMap = functionName.zip(routeCommand).toMap()
            var selectedValue by remember { mutableStateOf(routeCommand[0]) }

            Column {
                var i = 0
                functionName.forEach { _functionName ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = (functionRouteMap[_functionName] == selectedValue),
                            onClick = { selectedValue = functionRouteMap[_functionName] ?: "/" })
                        Text(_functionName)
                    }
                    i++
                }
            }
            Spacer(Modifier.padding(20.dp))
            Text("ServerSaid : ${textReceive.value}")
            Spacer(Modifier.padding(20.dp))
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Button(onClick = {
                    scope.launch {
                        withContext(Dispatchers.IO) {
                            textReceive.value = sendMessage(
                                routeCommand = selectedValue,
                                ipAddress = ipAddress.value.text,
                                port = port.toInt(),
                                context = context,
                                SslControler = !checked
                            )
                        }
                    }
                }) {
                    Text("Send Message")
                }
            }


        }
    }
}
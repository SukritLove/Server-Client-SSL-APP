package com.example.serverapplication.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.networkutils.core.PlatformNetworkingUtils
import com.example.networkutils.core.server.startServer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun AppServerScreen(networkUtils: PlatformNetworkingUtils) {

    var deviceIpAddress by remember { mutableStateOf<String?>("") }
    var serverStarted by remember { mutableStateOf(false) }
    var serverStatus by remember { mutableStateOf("") }
    //val message = ClientMessage.messages.message

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        deviceIpAddress = networkUtils.getCurrentWifiIpAddress()
    }
    LaunchedEffect(serverStarted) {
        println("Server Status:: $serverStatus")
        launch(Dispatchers.IO) {
            if (serverStarted) {
                startServer(deviceIpAddress.orEmpty(), context)
            }
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(40.dp))
        deviceIpAddress?.let {
            Text(
                text = "Server is listening on $it :: 8000"
            )
        }


        Spacer(modifier = Modifier.padding(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(Color.White)
                .border(5.dp, Color.Black)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                item {
/*                    Text(
                        text = message,
                        style = TextStyle(color = Color.Black, fontSize = 30.sp),
                        modifier = Modifier.padding(10.dp)
                    )*/
                }
            }

        }

        Button(onClick = {
            serverStarted = true
        },
            enabled = !serverStarted
        ) {
            Text(
                "Start Server"
            )
        }
    }
}
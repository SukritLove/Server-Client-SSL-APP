package com.example.networkutils.core.client

import android.content.Context
import com.example.networkutils.data.MessageSerialData
import com.example.networkutils.settings.SslSettings
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.wss
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.network.tls.CIOCipherSuites
import io.ktor.network.tls.addKeyStore
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.serialization.json.Json


suspend fun sendMessage(
    routeCommand: String,
    ipAddress: String,
    port: Int,
    context: Context
): String {

    val cioClient = HttpClient(CIO) {
        install(WebSockets) {
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        engine {
            https {
                trustManager = SslSettings(context).getTrustManager()
                addKeyStore(SslSettings(context).getKeyStore(), "1q2w3e4r".toCharArray())
                cipherSuites = CIOCipherSuites.SupportedSuites
            }
        }
    }
    var receivedMessage = ""

    try {
        cioClient.webSocket(
            method = HttpMethod.Get,
            host = ipAddress,
            port = port,
            path = routeCommand,
            request = {
                url.protocol = URLProtocol.WSS
            }
        ) {
            receivedMessage = receiveDeserialized<MessageSerialData>().message
            println("ServerSaid : $receivedMessage")
        }
    } catch (e: Exception) {
        println(e.stackTrace)
        receivedMessage = "The servers are not open yet."
        println(receivedMessage)
    } finally {
        cioClient.close()
    }
    return receivedMessage
}


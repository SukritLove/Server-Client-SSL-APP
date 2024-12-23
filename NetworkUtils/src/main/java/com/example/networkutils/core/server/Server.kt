package com.example.networkutils.core.server

import android.content.Context
import com.example.networkutils.plugin.configureSerialization
import com.example.networkutils.settings.SslSettings
import io.ktor.network.tls.certificates.buildKeyStore
import io.ktor.network.tls.certificates.saveToFile
import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.engine.sslConnector
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.origin
import io.ktor.server.routing.routing
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import kotlinx.serialization.Serializable
import java.net.InetAddress
import java.net.NetworkInterface


@Serializable
data class Message(val message: String)

fun startServer(ipAddress: String, context: Context) {

    val keyStoreAlias = "examplealias"
    val keyStorePassword = "1q2w3e4r"

//    val keyStore = SslSettings(context).getKeyStore()
//    println(keyStore.getCertificate("examplealias"))

        /*val keyStore = buildKeyStore {
            certificate(keyStoreAlias) {
                ipAddresses = listOf(InetAddress.getByName("192.168.101.51"), InetAddress.getByName("192.168.101.149"))
                password = keyStorePassword
                domains = listOf("192.168.101.51","127.0.0.1", "0.0.0.0", "localhost")
            }
        }
        keyStore.saveToFile(keyStoreFile, keyStorePassword)
        println(keyStore.getCertificate("exampleAlias"))*/

    val environment = applicationEngineEnvironment {
        module { module() }
        connector {
            host = ipAddress
            port = 8000
        }
//        sslConnector(
//            keyStore = keyStore,
//            keyAlias = keyStoreAlias,
//            keyStorePassword = { keyStorePassword.toCharArray() },
//            privateKeyPassword = { keyStorePassword.toCharArray() })
//        {
//            host = ipAddress
//            port = 8080
//            println(type.name)
//        }
    }
    embeddedServer(Netty, environment)
        .start(wait = true)
}

fun Application.module() {
    val interfaces =  NetworkInterface.getNetworkInterfaces()

    configureSerialization()
    routing {
        var host = ""
        while (interfaces.hasMoreElements()) {
            val iface = interfaces.nextElement()
            val addresses = iface.inetAddresses
            while (addresses.hasMoreElements()) {
                val address = addresses.nextElement()
                if (!address.isLoopbackAddress) {
                    host = address.hostAddress.toString();
                }
            }
        }
        webSocket("/") {
            sendSerialized(Message(message = "The Server is OPENED!"))
            println("Request From ${call.request.origin.remoteHost}")
            println("Sending : The Server is OPENED!")
        }
        webSocket("/Greeting") {
            sendSerialized(Message(message = "Hello there, This is SERVER! sent from " + host))
            println("Request From ${call.request.origin.remoteHost}")
            println("Sending : Hello there, This is SERVER!")
        }
        webSocket("/GetIp") {
            val remoteHostIP = call.request.origin.remoteHost
            sendSerialized(Message(message = "Your ip : $remoteHostIP"))
            println("Request From ${call.request.origin.remoteHost}")
            println("Sending : Your ip : $remoteHostIP")
        }

    }
}

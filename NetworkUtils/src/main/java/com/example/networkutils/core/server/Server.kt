package com.example.networkutils.core.server

import android.content.Context
import com.example.networkutils.plugin.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import io.ktor.server.websocket.sendSerialized
import io.ktor.server.websocket.webSocket
import kotlinx.serialization.Serializable
import org.slf4j.LoggerFactory


@Serializable
data class Message(val message: String)

fun startServer(ipAddress: String, context: Context) {
    /*val keyStoreFile = File("/Users/sukrit_love/IdeaProjects/ktor-sample-Socket-S/src/main/resources/raw/keystore.jks")
    val keyStoreAlias = "smart-linkpos"
    val keyStorePassword = "smartlinkpos!!@@"
    val keyStore = buildKeyStore {
        certificate(keyStoreAlias) {
            ipAddresses = listOf(InetAddress.getByName("192.168.101.51"))
            password = keyStorePassword
            domains = listOf("192.168.101.51","127.0.0.1", "0.0.0.0", "localhost")
        }
    }
    keyStore.saveToFile(keyStoreFile, keyStorePassword)
    println(keyStore.getCertificate("smart-linkpos"))*/

    val environment = applicationEngineEnvironment {
        log = LoggerFactory.getLogger("ktor.application")
        connector {
            host = ipAddress
            port = 8080
            module(Application::module)
        }
        /*sslConnector(
            keyStore = keyStore,
            keyAlias = keyStoreAlias,
            keyStorePassword = { keyStorePassword.toCharArray() },
            privateKeyPassword = { keyStorePassword.toCharArray() })
        {
            host = "192.168.101.51"
            port = 4043
            keyStorePath = keyStoreFile

        }*/
    }
    embeddedServer(Netty, environment)
        .start(wait = true)
}
fun Application.module() {
    configureSerialization()
    routing {
        webSocket("/Greeting") {
            sendSerialized(Message(message = "Hello there WHO ARE YOUUUU!!!!!"))
        }
    }
}

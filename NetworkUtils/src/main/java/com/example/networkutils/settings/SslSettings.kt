package com.example.networkutils.settings

import android.content.Context
import com.example.networkutils.R
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class SslSettings(private val context: Context) {
     fun getKeyStore(): KeyStore {
        val keyStorePassword = "1q2w3e4r"
        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType()).apply {
            val keyStoreInputStream = context.assets.open("keystore.bks")
            keyStoreInputStream.use { fis ->
                load(fis, keyStorePassword.toCharArray())
            }
        }
        return keyStore
    }
    private fun getTrustManagerFactory(): TrustManagerFactory? {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(getKeyStore())
        return trustManagerFactory
    }

    fun getTrustManager(): X509TrustManager {
        return getTrustManagerFactory()?.trustManagers?.first { it is X509TrustManager } as X509TrustManager
    }
}
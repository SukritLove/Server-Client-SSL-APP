val ktor_version = "2.1.0"

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
}

android {
    namespace = "com.example.networkutils"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }
    signingConfigs {
        getByName("debug") {
//            storeFile =
//                file("/Users/sukrit_love/AndroidStudioProjects/ServerClientSSL/NetworkUtils/src/main/assets/keystore.bks")
            storePassword = "1q2w3e4r"
            keyPassword = "1q2w3e4r"
            keyAlias = "examplealias"
            storeType = "BKS"
        }
        create("release") {
//            storeFile =
//                file("/Users/sukrit_love/AndroidStudioProjects/ServerClientSSL/NetworkUtils/src/main/assets/keystore.bks")
            storePassword = "1q2w3e4r"
            keyPassword = "1q2w3e4r"
            keyAlias = "examplealias"
            storeType = "BKS"
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


    //                      -Server-
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("io.ktor:ktor-server-websockets:$ktor_version")
    //                      -Client-
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-websockets:$ktor_version")
    //                      -Certificate-
    implementation("io.ktor:ktor-network-tls-certificates:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    //                      -BKS-
    implementation("org.bouncycastle:bcprov-jdk18on:1.77")

    implementation("io.netty:netty-tcnative-boringssl-static:2.0.61.Final")

}
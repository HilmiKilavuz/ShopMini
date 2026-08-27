
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    //
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

    //Firebase

    id("com.google.gms.google-services")
    // Add the Crashlytics Gradle plugin
    id("com.google.firebase.crashlytics")
}
fun localProp(key: String): String =
    rootProject.file("local.properties")
        .readLines()
        .firstOrNull { it.startsWith("$key=") }
        ?.substringAfter("=")
        ?.trim() ?: ""

android {
    namespace = "com.example.shopmini"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.example.shopmini"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // local.properties'ten oku, koda göm

        buildConfigField("String", "SUPABASE_URL", "\"${localProp("SUPABASE_URL")}\"")
        buildConfigField("String", "SUPABASE_ANON_KEY", "\"${localProp("SUPABASE_ANON_KEY")}\"")

    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.material3)
    //versions
    val nav_version = "2.9.8"
    val room_version = "2.8.4"
    //
    //implementation
    // 1. Ekranlar Arası Geçiş (Navigation)
    implementation("androidx.navigation:navigation-compose:$nav_version")
    // Views/Fragments integration
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    // Feature module support for Fragments
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
    // Testing Navigation
    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")
    // JSON serialization library, works with the Kotlin serialization plugin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")


    // 2. İnternetten Veri Çekme (Retrofit & OkHttp)
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // 3. Gelen JSON verisini Kotlin Objesine Çevirme (Serialization)
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")
    // 4. İnternetteki Resimleri Gösterme (Coil)
    implementation("io.coil-kt:coil-compose:2.6.0")
    // 5. Asenkron (Coroutines)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")
    // Firebase Task'larını coroutine ile kullanmak için (.await() desteği)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.11.0")
    // 6. Dependency Injection (Hilt)
    implementation("com.google.dagger:hilt-android:2.59.2")
    ksp("com.google.dagger:hilt-android-compiler:2.59.2")
    // 7. Local DB (Room)
    implementation("androidx.room:room-runtime:${room_version}")
    ksp("androidx.room:room-compiler:$room_version")

    // 8. Hilt + Navigation Compose Entegrasyonu
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // 9. Flow Testleri (Turbine)
    testImplementation("app.cash.turbine:turbine:1.1.0")
    // Material Icons kütüphanesi
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.16.0"))
    implementation("com.google.firebase:firebase-analytics")
    // Add the dependencies for the Crashlytics and Analytics libraries
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging")


    // Supabase BOM (Bill of Materials — versiyon yönetimi)
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.4"))

    // Auth modülü (giriş/kayıt işlemleri)
    implementation("io.github.jan-tennert.supabase:auth-kt")
    // PostgREST modülü (profiles tablosu için)
    implementation("io.github.jan-tennert.supabase:postgrest-kt")

    // Supabase'in HTTP istekleri için Ktor motoru
    implementation("io.ktor:ktor-client-android:3.1.3")

    //Compose ile grafik çizmek için Vico kütüphanesi
    implementation("com.patrykandpatrick.vico:compose:1.15.0")
    implementation("com.patrykandpatrick.vico:compose-m3:1.15.0")
    implementation("com.patrykandpatrick.vico:core:1.15.0")



    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

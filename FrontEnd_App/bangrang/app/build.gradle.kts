import java.util.Properties
import java.io.FileInputStream

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
}

val properties = Properties()
properties.load(FileInputStream(project.rootProject.file("local.properties")))

android {
    namespace = "com.ssafyb109.bangrang"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ssafyb109.bangrang"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // 카카오 키 저장
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"${properties.getProperty("KAKAO_NATIVE_APP_KEY")}\"")
        manifestPlaceholders["kakaoKey"] = "kakao${properties.getProperty("KAKAO_NATIVE_APP_KEY")}"

        // 구글 지도
        buildConfigField("String", "GOOGLE_MAP_APP_KEY", "\"${properties.getProperty("GOOGLE_MAP_APP_KEY")}\"")
        manifestPlaceholders["googleMapScheme"] = properties.getProperty("GOOGLE_MAP_APP_KEY")

        //네이버 지도 ID 저장

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.4")
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.5.4")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
    // 권한
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")
    // Hilt 의존성
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    // Hilt ViewModel
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
    // 코루틴
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    // 그래프 라이브러리
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    // 카카오 로그인
    implementation ("com.kakao.sdk:v2-user:2.17.0")
    // 구글 로그인
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    // GPS // 네이버 compose 지도가 16.0.0과 호환중
    implementation ("com.google.android.gms:play-services-location:16.0.0")
    // 네이버 지도
    implementation("com.naver.maps:map-sdk:3.17.0")
    implementation ("io.github.fornewid:naver-map-compose:1.4.0")
    // coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    // FCM
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-messaging-ktx")


    // Room
    val room_version = "2.6.0"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    // Room KTX 라이브러리 추가
    implementation ("androidx.room:room-ktx:$room_version")



    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

kapt {
    correctErrorTypes = true
}
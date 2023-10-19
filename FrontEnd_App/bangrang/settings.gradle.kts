pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven("https://naver.jfrog.io/artifactory/maven/")
        gradlePluginPortal()
    }
    plugins {
        id("dagger.hilt.android.plugin") version "2.44"
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
        maven("https://naver.jfrog.io/artifactory/maven/")
    }
}

rootProject.name = "bangrang"
include(":app")

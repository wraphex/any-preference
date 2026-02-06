plugins {
    id("com.android.library")
    id("maven-publish")
}

android {
    namespace = "wraphex.anypreference"
    compileSdk = 36

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
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
}

dependencies {
    implementation(libs.gson)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mmkv)
}

fun latestGitTag(): String {
    val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0").start()
    return process.inputStream.bufferedReader().use { bufferedReader ->
        bufferedReader.readText().trim()
    }
}

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "com.github.wraphex"
            artifactId = "any-preference"
            version = latestGitTag().ifEmpty { "SNAPSHOT" }

            afterEvaluate {
                from(components.findByName("release"))
            }
        }
    }
}
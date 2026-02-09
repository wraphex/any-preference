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

    publishing {
        singleVariant("release")
    }
}

val pubGroup = project.findProperty("group")?.toString() ?: "com.github.wraphex"
val pubVersion = project.findProperty("version")?.toString() ?: "SNAPSHOT"

publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = pubGroup
            artifactId = "any-preference"
            version = pubVersion

            afterEvaluate {
                from(components["release"])
            }
        }
    }
}

dependencies {
    implementation(libs.gson)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.mmkv)
}

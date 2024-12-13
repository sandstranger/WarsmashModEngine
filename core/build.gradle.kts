import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id  ("com.android.library")
    id ("org.jetbrains.kotlin.android")
}

android {
    namespace = "net.warsmash.core"
    compileSdk = 34

    sourceSets {
        named("main") {
            java.srcDir("src")
            manifest.srcFile("AndroidManifest.xml")
        }
    }

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.freeCompilerArgs += "-Xvalue-classes"
}

dependencies {
    implementation ("org.lwjgl.lwjgl:lwjgl:2.9.3")
    implementation  ("com.badlogicgames.gdx:gdx:1.13.0")
    implementation  ("com.badlogicgames.gdx:gdx-box2d:1.13.0")
    implementation  ("com.badlogicgames.gdx:gdx-freetype:1.13.0")
    implementation  ("com.google.guava:guava:33.0.0-jre")
    implementation  ("org.apache.commons:commons-compress:1.21")
    implementation  ("net.nikr:dds:1.0.0")
    implementation ("com.google.code.gson:gson:2.11.0")
    implementation(project(":fdfparser"))
    implementation(project(":jassparser"))
    implementation(project(":shared"))
    implementation(files("libs/commons-io-2.18.0.jar"))
//    api files(fileTree(dir:'../jars', includes: ['*.jar']))
}
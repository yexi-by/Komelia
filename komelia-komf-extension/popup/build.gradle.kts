@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.wasm.binaryen.BinaryenEnvSpec

plugins {
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinSerialization)
}

val localBinaryenDir = rootProject.file(".gradle/binaryen/binaryen-version_123")

kotlin {
    wasmJs {
        outputModuleName = "popup"
        binaries.executable()
        browser()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-attach-js-exception")

        }
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)
            implementation(libs.kotlinx.browser)
            implementation(libs.kotlinx.serialization.json)
            implementation(project(":komelia-komf-extension:shared"))
        }
    }
}

afterEvaluate {
    extensions.configure<BinaryenEnvSpec> {
        if (localBinaryenDir.exists()) {
            download.set(false)
            installationDirectory.fileValue(localBinaryenDir)
            command.set(localBinaryenDir.resolve("bin/wasm-opt.exe").path.replace('\\', '/'))
        }
    }
}


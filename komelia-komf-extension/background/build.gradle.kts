@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.wasm.binaryen.BinaryenEnvSpec

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

val localBinaryenDir = rootProject.file(".gradle/binaryen/binaryen-version_123")

kotlin {
    wasmJs {
        outputModuleName = "background"
        binaries.executable()
        browser()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-attach-js-exception")
        }
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
            implementation(libs.kotlinx.coroutines.core)
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


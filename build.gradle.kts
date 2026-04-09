import groovy.json.JsonSlurper
import org.apache.tools.ant.taskdefs.condition.Os
import org.gradle.api.file.DuplicatesStrategy.EXCLUDE
import java.util.regex.Pattern

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAtomicfu) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlinSerialization) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.parcelize) apply false
}

// https://youtrack.jetbrains.com/issue/CMP-5831
allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlinx" && requested.name == "atomicfu") {
                useVersion(libs.versions.kotlinx.atomicfu.get())
            }
        }
    }
}

val linuxBuildDir = "$projectDir/cmake/build"
val windowsBuildDir = "$projectDir/cmake/build-w64"
val androidArm64BuildDir = "$projectDir/cmake/build-android-aarch64"
val androidArmv7aBuildDir = "$projectDir/cmake/build-android-armv7a"
val androidx8664BuildDir = "$projectDir/cmake/build-android-x86_64"
val androidx86BuildDir = "$projectDir/cmake/build-android-x86"

val resourcesDir = "$projectDir/komelia-infra/jni/src/jvmMain/resources/"
val androidJniLibsDir = "$projectDir/komelia-infra/jni/src/androidMain/jniLibs"
val composeDistroResourcesDir = "$projectDir/komelia-app/desktopUnpackedResources"
val composeCommonResources = "$projectDir/komelia-ui/src/commonMain/composeResources/files"
val composeWebuiResources = "$composeCommonResources/webui"

val epubReader = "$rootDir/komelia-epub-reader"
val epubReaderKomga = "$epubReader/komga-webui"
val epubReaderTtsu = "$epubReader/ttu-ebook-reader"

val linuxCommonLibs = setOf(
    "libintl.so",
    "libbrotlicommon.so",
    "libbrotlidec.so",
    "libbrotlienc.so",
    "libde265.so",
    "libdav1d.so",
    "libexif.so",
    "libexpat.so",
    "libffi.so",
    "libgio-2.0.so",
    "libglib-2.0.so",
    "libgmodule-2.0.so",
    "libgobject-2.0.so",
    "libheif.so",
    "libhwy.so",
    "liblcms2.so",
    "libjpeg.so",
    "libjxl.so",
    "libjxl_cms.so",
    "libjxl_threads.so",
    "libsharpyuv.so",
    "libpng.so",
    "libtiff.so",
    "libturbojpeg.so",
    "libvips.so",
    "libwebp.so",
    "libwebpdecoder.so",
    "libwebpdemux.so",
    "libwebpmux.so",
    "libz.so",
    "libkomelia_vips.so",
    "libkomelia_onnxruntime.so",
)
val androidLibs = linuxCommonLibs + setOf(
    "libkomelia_android_bitmap.so",
    "libiconv.so",
    "libomp.so",
    "libpng16.so",
    "libonnxruntime.so",
    "libonnxruntime_providers_shared.so",
)
val androidAbiToTask = mapOf(
    "arm64-v8a" to "android-arm64_copyJniLibs",
    "armeabi-v7a" to "android-armv7a_copyJniLibs",
    "x86_64" to "android-x86_64_copyJniLibs",
    "x86" to "android-x86_copyJniLibs",
)
val requestedAndroidAbi = providers.gradleProperty("komelia.android.abi").orNull
val skipAndroidJniVerification = providers.gradleProperty("komelia.android.skipJniVerification")
    .orNull
    ?.toBooleanStrictOrNull()
    ?: false
val activeAndroidAbis = requestedAndroidAbi?.let { abi ->
    require(androidAbiToTask.containsKey(abi)) {
        "不支持的 Android ABI: $abi。支持的值：${androidAbiToTask.keys.joinToString()}"
    }
    listOf(abi)
} ?: androidAbiToTask.keys.toList()
val desktopLinuxLibs = linuxCommonLibs + setOf(
    "libkomelia_onnxruntime.so",
    "libkomelia_enumerate_devices_cuda.so",
    "libkomelia_enumerate_devices_rocm.so",
    "libkomelia_enumerate_devices_vulkan.so",
    "libkomelia_webview.so",
    "libkomelia_webkit_extension.so",
)
val desktopJniLibs = setOf(
    "libkomelia_vips.so",
    "libkomelia_onnxruntime.so",
    "libkomelia_enumerate_devices_cuda.so",
    "libkomelia_enumerate_devices_rocm.so",
    "libkomelia_enumerate_devices_vulkan.so",
    "libkomelia_skia.so",
    "libkomelia_webview.so",
    "libkomelia_webkit_extension.so",
)

val windowsLibs = setOf(
    "libbrotlicommon.dll",
    "libbrotlidec.dll",
    "libbrotlienc.dll",
    "libde265.dll",
    "libdav1d.dll",
    "libexif-12.dll",
    "libexpat-1.dll",
    "libffi-8.dll",
    "libgio-2.0-0.dll",
    "libglib-2.0-0.dll",
    "libgmodule-2.0-0.dll",
    "libgobject-2.0-0.dll",
    "libheif.dll",
    "libhwy.dll",
    "liblcms2-2.dll",
    "libintl-8.dll",
    "libjpeg-62.dll",
    "libjxl.dll",
    "libjxl_cms.dll",
    "libjxl_threads.dll",
    "libsharpyuv.dll",
    "libpng16.dll",
    "libtiff.dll",
    "libvips-42.dll",
    "libwebp.dll",
    "libwebpdecoder.dll",
    "libwebpdemux.dll",
    "libwebpmux.dll",
    "libz1.dll",
    "libstdc++-6.dll",
    "libwinpthread-1.dll",
    "libgcc_s_seh-1.dll",
    "libgomp-1.dll",
    "libkomelia_vips.dll",
    "libkomelia_onnxruntime.dll",
    "libkomelia_onnxruntime_dml.dll",
    "libkomelia_enumerate_devices_dxgi.dll",
    "libkomelia_enumerate_devices_cuda.dll",
    "libkomelia_webview.dll",
)

tasks.register<Sync>("linux-x86_64_copyJniLibs") {
    group = "jni"
    from("$linuxBuildDir/sysroot/lib/")
    into(resourcesDir)
    val dependencies = desktopLinuxLibs
    include { it.file.isFile && it.name in dependencies }
}


tasks.register<Sync>("android-aarch64_copyJniLibs") {
    group = "jni"
    dependsOn(":komelia-infra:database:sqlite:android-arm64-ExtractSqliteLib")

    from("$androidArm64BuildDir/sysroot/lib/")
    into("$androidJniLibsDir/arm64-v8a/")
    val dependencies = androidLibs
    include { it.file.isFile && it.name in dependencies }
}

tasks.register<Sync>("android-arm64_copyJniLibs") {
    group = "jni"
    dependsOn(":komelia-infra:database:sqlite:android-arm64-ExtractSqliteLib")

    from("$androidArm64BuildDir/sysroot/lib/")
    into("$androidJniLibsDir/arm64-v8a/")
    val dependencies = androidLibs
    include { it.file.isFile && it.name in dependencies }
}

tasks.register<Sync>("android-armv7a_copyJniLibs") {
    group = "jni"
    dependsOn(":komelia-infra:database:sqlite:android-armv7a-ExtractSqliteLib")

    from("$androidArmv7aBuildDir/sysroot/lib/")
    into("$androidJniLibsDir/armeabi-v7a/")
    val dependencies = androidLibs
    include { it.file.isFile && it.name in dependencies }
}

tasks.register<Sync>("android-x86_64_copyJniLibs") {
    group = "jni"
    dependsOn(":komelia-infra:database:sqlite:android-x86_64-ExtractSqliteLib")
    from("$androidx8664BuildDir/sysroot/lib/")
    into("$androidJniLibsDir/x86_64/")
    val dependencies = androidLibs
    include { it.file.isFile && it.name in dependencies }
}

tasks.register<Sync>("android-x86_copyJniLibs") {
    group = "jni"
    dependsOn(":komelia-infra:database:sqlite:android-x86-ExtractSqliteLib")
    from("$androidx86BuildDir/sysroot/lib/")
    into("$androidJniLibsDir/x86/")
    val dependencies = androidLibs
    include { it.file.isFile && it.name in dependencies }
}

tasks.register("verifyAndroidJniLibs") {
    group = "jni"
    description = "校验 Android 构建所需的原生库是否已经复制到 jniLibs，缺失时直接失败，避免产出损坏的 APK。"
    dependsOn(activeAndroidAbis.map { androidAbiToTask.getValue(it) })
    notCompatibleWithConfigurationCache("该任务会直接读取构建脚本中的 JNI 目录配置并进行文件系统校验。")

    doLast {
        val missing = buildList {
            activeAndroidAbis.forEach { abi ->
                androidLibs.forEach { libName ->
                    val libFile = file("$androidJniLibsDir/$abi/$libName")
                    if (!libFile.exists()) {
                        add("$abi/$libName")
                    }
                }
            }
        }

        if (missing.isNotEmpty()) {
            throw GradleException(
                """
                Android JNI 依赖不完整，已阻止继续构建损坏的 APK。
                缺失库：
                ${missing.joinToString(separator = "\n")}

                请先按 README 的 Android 原生依赖流程构建并复制 JNI 库：
                1. 使用 ./cmake/android.Dockerfile 构建 Android 原生依赖
                2. 运行 ./gradlew ${activeAndroidAbis.joinToString(" ") { androidAbiToTask.getValue(it) }}
                3. 再执行 APK 构建
                """.trimIndent()
            )
        }
    }
}

tasks.register<Delete>("cleanJni") {
    group = "jni"
    delete(linuxBuildDir)
    delete(windowsBuildDir)
    delete(fileTree(resourcesDir))
}

tasks.register<Sync>("windows-x86_64_copyJniLibs") {
    group = "jni"

    duplicatesStrategy = EXCLUDE
    from("$windowsBuildDir/sysroot/bin/")
    into(resourcesDir)
    val dependencies = windowsLibs
    include { it.name in windowsLibs }

    // include mingw dlls if compiled using system toolchain
    from("/usr/x86_64-w64-mingw32/bin/")
    include("libstdc++-6.dll")
    include("libwinpthread-1.dll")
    include("libgcc_s_seh-1.dll")
    include("libgomp-1.dll")
    into(resourcesDir)
}

tasks.register<Sync>("windows-x86_64_copyJniLibsComposeResources") {
    group = "jni"

    duplicatesStrategy = EXCLUDE
    from("$windowsBuildDir/sysroot/bin/")
    into("$composeDistroResourcesDir/windows")
    val dependencies = windowsLibs
    include { it.name in dependencies }

    // include mingw dlls if compiled using system toolchain
    from("/usr/x86_64-w64-mingw32/bin/")
    include("libstdc++-6.dll")
    include("libwinpthread-1.dll")
    include("libgcc_s_seh-1.dll")
    include("libgomp-1.dll")
    into("$composeDistroResourcesDir/windows")
}


tasks.register<Exec>("komgaNpmInstall") {
    group = "web"
    workingDir(epubReaderKomga)
    inputs.file("$epubReaderKomga/package.json")
    outputs.dir("$epubReaderKomga/node_modules")
    commandLine(
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            "npm.cmd"
        } else {
            "npm"
        },
        "install",
    )
}

tasks.register<Exec>("komgaNpmBuild") {
    group = "web"
    dependsOn("komgaNpmInstall")
    workingDir(epubReaderKomga)
    inputs.dir(epubReaderKomga)
    outputs.dir("$epubReaderKomga/dist")
    commandLine(
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            "npm.cmd"
        } else {
            "npm"
        },
        "run",
        "build",
    )
}

tasks.register<Exec>("ttsuNpmInstall") {
    group = "web"
    workingDir(epubReaderTtsu)
    inputs.file("$epubReaderTtsu/package.json")
    outputs.dir("$epubReaderTtsu/node_modules")
    commandLine(
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            "npm.cmd"
        } else {
            "npm"
        },
        "install",
    )
}

tasks.register<Exec>("ttsuNpmBuild") {
    group = "web"
    dependsOn("ttsuNpmInstall")
    workingDir(epubReaderTtsu)
    inputs.dir(epubReaderTtsu)
    outputs.dir("$epubReaderTtsu/dist")
    commandLine(
        if (Os.isFamily(Os.FAMILY_WINDOWS)) {
            "npm.cmd"
        } else {
            "npm"
        },
        "run",
        "build",
    )
}

tasks.register<Sync>("buildWebui") {
    group = "web"
    dependsOn("komgaNpmBuild")
    dependsOn("ttsuNpmBuild")

    from("$epubReaderKomga/dist/")
    from("$epubReaderTtsu/dist/")
    into(composeWebuiResources)
}

tasks.register<Exec>("cmakeSystemDepsConfigure") {
    group = "jni"
    delete("$projectDir/cmake-build")
    inputs.file("$projectDir/komelia-infra/image-decoder/vips/native/CMakeLists.txt")
    inputs.file("$projectDir/komelia-infra/webview/native/CMakeLists.txt")
    commandLine(
        "cmake",
        "-B", "cmake-build",
        "-G", "Ninja",
        "-DCMAKE_BUILD_TYPE=Release",
        "-DKOMELIA_SUPERBUILD=OFF"
    )
}

tasks.register<Exec>("cmakeSystemDepsBuild") {
    group = "jni"
    dependsOn("cmakeSystemDepsConfigure")
    inputs.dir("$projectDir/cmake-build")
    outputs.dir("$projectDir/cmake-build/komelia-infra/image-decoder/native")
    outputs.dir("$projectDir/cmake-build/komelia-infra/webview/native")
    commandLine(
        "cmake",
        "--build",
        "cmake-build",
        "--parallel"
    )
}

tasks.register<Sync>("cmakeSystemDepsCopyJniLibs") {
    group = "jni"
    dependsOn("cmakeSystemDepsBuild")
    inputs.dir("$projectDir/cmake-build/komelia-infra/webview/native")
    inputs.dir("$projectDir/cmake-build/komelia-infra/image-decoder/vips/native")
    inputs.dir("$projectDir/cmake-build/komelia-infra/onnxruntime/native")
    outputs.dir(resourcesDir)

    from(
        "$projectDir/cmake-build/komelia-infra/image-decoder/vips/native",
        "$projectDir/cmake-build/komelia-infra/webview/native",
        "$projectDir/cmake-build/komelia-infra/onnxruntime/native"
    )
    into(resourcesDir)
    val dependencies = desktopJniLibs
    include { it.name in dependencies }
}

tasks.register("komeliaBuildNonJvmDependencies") {
    group = "build"
    dependsOn("buildWebui")
    dependsOn("cmakeSystemDepsCopyJniLibs")
}

gradle.projectsEvaluated {
    project(":komelia-ui").tasks.matching {
        it.name == "copyNonXmlValueResourcesForCommonMain"
    }.configureEach {
        dependsOn(":buildWebui")
    }

    project(":komelia-app").tasks.named("preBuild") {
        if (!skipAndroidJniVerification) {
            dependsOn(":verifyAndroidJniLibs")
        }
    }

    val androidJniCopyTasks = activeAndroidAbis.map { abi ->
        ":${androidAbiToTask.getValue(abi)}"
    }
    project(":komelia-infra:jni").tasks.matching {
        it.name.contains("JniLibFolders")
    }.configureEach {
        dependsOn(androidJniCopyTasks)
    }
}

val uiStringsAuditRoots = listOf(
    "komelia-ui/src/commonMain/kotlin/snd/komelia/ui",
    "komelia-ui/src/androidMain/kotlin/snd/komelia/ui",
    "komelia-ui/src/jvmMain/kotlin/snd/komelia/ui",
    "komelia-komf-extension/content/src/wasmJsMain/kotlin",
    "komelia-komf-extension/popup/src/wasmJsMain/kotlin",
    "komelia-epub-reader/ttu-ebook-reader/src/lib/components",
    "komelia-epub-reader/komga-webui/src/components",
)

val kotlinAuditPatterns = listOf(
    "Text\\(\"[^\"]+\"",
    "label = \\{ Text\\(\"[^\"]+\"\\) \\}",
    "placeholder = \\{ Text\\(\"[^\"]+\"\\) \\}",
    "supportingText = \\{ Text\\(\"[^\"]+\"\\)",
    "tooltip = \\{ Text\\(\"[^\"]+\"\\)",
    "title = \"[^\"]+\"",
    "body = \"[^\"]+\"",
    "confirmText = \"[^\"]+\"",
    "message = \"[^\"]+\"",
    "\\.title = \"[^\"]+\"",
).map(Pattern::compile)

val svelteAuditPatterns = listOf(
    "title=\"[^\"]+\"",
    "placeholder=\"[^\"]+\"",
    ">\\s*[A-Za-z][^<{]*\\s*<",
).map(Pattern::compile)

val vueAuditPatterns = listOf(
    "title=\"[^\"]+\"",
    "placeholder=\"[^\"]+\"",
    ">\\s*[A-Za-z][^<{]*\\s*<",
).map(Pattern::compile)

val i18nBypassAuditRoots = listOf(
    "komelia-ui/src/commonMain/kotlin",
    "komelia-ui/src/androidMain/kotlin",
    "komelia-ui/src/jvmMain/kotlin",
    "komelia-app/src/commonMain/kotlin",
    "komelia-app/src/androidMain/kotlin",
    "komelia-app/src/jvmMain/kotlin",
    "komelia-app/src/wasmJsMain/kotlin",
    "komelia-komf-extension/content/src/wasmJsMain/kotlin",
    "komelia-komf-extension/popup/src/wasmJsMain/kotlin",
    "komelia-epub-reader/ttu-ebook-reader/src",
    "komelia-epub-reader/komga-webui/src",
)

val i18nBypassPatterns = listOf(
    "import\\s+snd\\.komelia\\.ui\\.strings\\.EnStrings",
    "import\\s+snd\\.komelia\\.ui\\.strings\\.ZhHansStrings",
    "import\\s+snd\\.komelia\\.strings\\.EnExtensionStrings",
    "import\\s+snd\\.komelia\\.strings\\.ZhHansExtensionStrings",
    "EnStrings\\.",
    "ZhHansStrings\\.",
    "EnExtensionStrings\\.",
    "ZhHansExtensionStrings\\.",
    "enEpubStrings",
    "zhHansEpubStrings",
    "locale:\\s*['\"]en['\"]",
    "fallbackLocale:\\s*['\"]en['\"]",
).map(Pattern::compile)

data class LocaleAssetScope(
    val name: String,
    val english: String,
    val simplifiedChinese: String,
)

val localeAssetScopes = listOf(
    LocaleAssetScope(
        name = "main-app",
        english = "komelia-ui/src/commonMain/composeResources/files/i18n/en.json",
        simplifiedChinese = "komelia-ui/src/commonMain/composeResources/files/i18n/zh-Hans.json",
    ),
    LocaleAssetScope(
        name = "extension",
        english = "komelia-komf-extension/app/src/wasmJsMain/resources/i18n/en.json",
        simplifiedChinese = "komelia-komf-extension/app/src/wasmJsMain/resources/i18n/zh-Hans.json",
    ),
    LocaleAssetScope(
        name = "ttu-ebook-reader",
        english = "komelia-epub-reader/ttu-ebook-reader/src/lib/i18n/locales/en.json",
        simplifiedChinese = "komelia-epub-reader/ttu-ebook-reader/src/lib/i18n/locales/zh-Hans.json",
    ),
)

fun loadUiStringsWhitelist(): Set<String> {
    val whitelistFile = rootProject.file("config/ui-strings-audit-whitelist.txt")
    if (!whitelistFile.exists()) return emptySet()
    return whitelistFile.readLines()
        .map(String::trim)
        .filter { it.isNotEmpty() && !it.startsWith("#") }
        .toSet()
}

fun collectUiStringsAuditMatches(): List<String> {
    val whitelist = loadUiStringsWhitelist()
    val matches = mutableListOf<String>()
    uiStringsAuditRoots
        .map(rootProject::file)
        .filter { it.exists() }
        .forEach { root ->
            root.walkTopDown()
                .filter { it.isFile }
                .filterNot { file ->
                    val relativePath = file.relativeTo(rootProject.projectDir).invariantSeparatorsPath
                    relativePath in whitelist || relativePath.contains("/strings/") || relativePath.contains("/i18n/")
                }
                .forEach { file ->
                    val patterns = when (file.extension) {
                        "kt" -> kotlinAuditPatterns
                        "svelte" -> svelteAuditPatterns
                        "vue" -> vueAuditPatterns
                        else -> emptyList()
                    }
                    if (patterns.isEmpty()) return@forEach

                    file.readLines().forEachIndexed { index, line ->
                        if (patterns.any { it.matcher(line).find() }) {
                            val relativePath = file.relativeTo(rootProject.projectDir).invariantSeparatorsPath
                            matches += "$relativePath:${index + 1}: ${line.trim()}"
                        }
                    }
                }
        }
    return matches
}

fun collectI18nBypassMatches(): List<String> {
    val matches = mutableListOf<String>()
    i18nBypassAuditRoots
        .map(rootProject::file)
        .filter { it.exists() }
        .forEach { root ->
            root.walkTopDown()
                .filter { it.isFile }
                .filterNot { file ->
                    val relativePath = file.relativeTo(rootProject.projectDir).invariantSeparatorsPath
                    relativePath.contains("/strings/") ||
                        relativePath.contains("/i18n/") ||
                        relativePath.endsWith("/i18n.ts")
                }
                .forEach { file ->
                    val extension = file.extension
                    if (extension !in setOf("kt", "ts", "svelte", "vue")) return@forEach

                    file.readLines().forEachIndexed { index, line ->
                        if (i18nBypassPatterns.any { it.matcher(line).find() }) {
                            val relativePath = file.relativeTo(rootProject.projectDir).invariantSeparatorsPath
                            matches += "$relativePath:${index + 1}: ${line.trim()}"
                        }
                    }
                }
        }
    return matches
}

fun flattenLocaleAsset(value: Any?, path: String = ""): Map<String, String> {
    return when (value) {
        is Map<*, *> -> value.entries
            .flatMap { (key, nestedValue) ->
                val segment = key?.toString() ?: error("Locale asset key must not be null")
                val nestedPath = if (path.isEmpty()) segment else "$path.$segment"
                flattenLocaleAsset(nestedValue, nestedPath).entries
            }
            .associate { it.toPair() }

        is List<*> -> value.withIndex()
            .flatMap { (index, nestedValue) ->
                val nestedPath = "$path[$index]"
                flattenLocaleAsset(nestedValue, nestedPath).entries
            }
            .associate { it.toPair() }

        is String -> mapOf(path to value)
        null -> mapOf(path to "null")
        else -> mapOf(path to value.toString())
    }
}

fun extractLocalePlaceholders(value: String): Set<String> {
    val placeholders = mutableSetOf<String>()
    Regex("\\{([A-Za-z0-9_]+)}")
        .findAll(value)
        .forEach { placeholders += it.groupValues[1] }
    return placeholders
}

fun collectLocaleAssetAuditMatches(): List<String> {
    val matches = mutableListOf<String>()
    val parser = JsonSlurper()

    localeAssetScopes.forEach { scope ->
        val englishFile = rootProject.file(scope.english)
        val simplifiedChineseFile = rootProject.file(scope.simplifiedChinese)

        if (!englishFile.exists()) {
            matches += "${scope.name}: missing ${scope.english}"
            return@forEach
        }
        if (!simplifiedChineseFile.exists()) {
            matches += "${scope.name}: missing ${scope.simplifiedChinese}"
            return@forEach
        }

        val englishMap = flattenLocaleAsset(parser.parse(englishFile))
        val simplifiedChineseMap = flattenLocaleAsset(parser.parse(simplifiedChineseFile))

        val englishKeys = englishMap.keys
        val simplifiedChineseKeys = simplifiedChineseMap.keys
        (englishKeys - simplifiedChineseKeys)
            .sorted()
            .forEach { matches += "${scope.name}: zh-Hans.json missing key '$it'" }
        (simplifiedChineseKeys - englishKeys)
            .sorted()
            .forEach { matches += "${scope.name}: en.json missing key '$it'" }

        (englishKeys intersect simplifiedChineseKeys)
            .sorted()
            .forEach { key ->
                val englishValue = englishMap.getValue(key)
                val simplifiedChineseValue = simplifiedChineseMap.getValue(key)
                if (Regex("(?<!%)%(s|d)").containsMatchIn(englishValue)) {
                    matches += "${scope.name}: en.json contains legacy positional placeholder for '$key'"
                }
                if (Regex("(?<!%)%(s|d)").containsMatchIn(simplifiedChineseValue)) {
                    matches += "${scope.name}: zh-Hans.json contains legacy positional placeholder for '$key'"
                }
                val englishPlaceholders = extractLocalePlaceholders(englishValue)
                val simplifiedChinesePlaceholders = extractLocalePlaceholders(simplifiedChineseValue)
                if (englishPlaceholders != simplifiedChinesePlaceholders) {
                    matches += buildString {
                        append("${scope.name}: placeholder mismatch for '$key' -> ")
                        append("en=")
                        append(englishPlaceholders.sorted())
                        append(", zh-Hans=")
                        append(simplifiedChinesePlaceholders.sorted())
                    }
                }
            }
    }

    return matches
}

tasks.register("auditUiStringsReport") {
    group = "verification"
    description = "列出运行时 UI 中疑似仍为硬编码的用户可见文案。"
    notCompatibleWithConfigurationCache("审计任务会直接读取脚本级扫描配置。")
    doLast {
        val matches = collectUiStringsAuditMatches()
        if (matches.isEmpty()) {
            logger.lifecycle("No hardcoded UI strings were found in scoped runtime UI sources.")
        } else {
            logger.lifecycle("Found ${matches.size} potential hardcoded UI strings:")
            matches.forEach { logger.lifecycle(it) }
        }
    }
}

tasks.register("auditUiStrings") {
    group = "verification"
    description = "校验运行时 UI 中不存在疑似硬编码的用户可见文案。"
    notCompatibleWithConfigurationCache("审计任务会直接读取脚本级扫描配置。")
    doLast {
        val matches = collectUiStringsAuditMatches()
        if (matches.isNotEmpty()) {
            error(
                buildString {
                    appendLine("Found hardcoded UI strings in scoped runtime UI sources.")
                    matches.forEach { appendLine(it) }
                }
            )
        }
    }
}

tasks.register("auditI18nBypassReport") {
    group = "verification"
    description = "列出运行时代码中疑似绕过 i18n provider 的调用点。"
    notCompatibleWithConfigurationCache("审计任务会直接读取脚本级扫描配置。")
    doLast {
        val matches = collectI18nBypassMatches()
        if (matches.isEmpty()) {
            logger.lifecycle("No i18n bypasses were found in scoped runtime sources.")
        } else {
            logger.lifecycle("Found ${matches.size} potential i18n bypasses:")
            matches.forEach { logger.lifecycle(it) }
        }
    }
}

tasks.register("auditI18nBypass") {
    group = "verification"
    description = "校验运行时代码中不存在疑似绕过 i18n provider 的调用点。"
    notCompatibleWithConfigurationCache("审计任务会直接读取脚本级扫描配置。")
    doLast {
        val matches = collectI18nBypassMatches()
        if (matches.isNotEmpty()) {
            error(
                buildString {
                    appendLine("Found i18n bypasses in scoped runtime sources.")
                    matches.forEach { appendLine(it) }
                }
            )
        }
    }
}

tasks.register("auditLocaleAssetsReport") {
    group = "verification"
    description = "列出 JSON 语言资产中结构或占位符不一致的问题。"
    notCompatibleWithConfigurationCache("审计任务会直接读取脚本级扫描配置。")
    doLast {
        val matches = collectLocaleAssetAuditMatches()
        if (matches.isEmpty()) {
            logger.lifecycle("All locale assets are structurally aligned.")
        } else {
            logger.lifecycle("Found ${matches.size} locale asset issues:")
            matches.forEach { logger.lifecycle(it) }
        }
    }
}

tasks.register("auditLocaleAssets") {
    group = "verification"
    description = "校验 JSON 语言资产的 key 路径和占位符集合完全一致。"
    notCompatibleWithConfigurationCache("审计任务会直接读取脚本级扫描配置。")
    doLast {
        val matches = collectLocaleAssetAuditMatches()
        if (matches.isNotEmpty()) {
            error(
                buildString {
                    appendLine("Found locale asset mismatches.")
                    matches.forEach { appendLine(it) }
                }
            )
        }
    }
}

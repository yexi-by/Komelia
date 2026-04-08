# Android APK 构建流程与踩坑记录

## 文档目的

这份文档只解决一件事：让新的接手人能在一台干净机器上，把 Komelia 的 Android APK 从源码稳定构出来，并且少踩这次已经踩过的坑。

本文优先覆盖：

- Android 原生依赖如何构建并正确打进 APK
- Web UI 与 EPUB 相关前端资源如何准备
- `debug` / `release` APK 的实际构建命令
- Windows 环境下最容易卡住的本地路径、Docker、Gradle、模拟器问题

## 先决条件

必须具备以下环境：

- `JDK 21`
- `Node.js` 与 `npm`
- `Android SDK`、`Android NDK`
- `Docker Desktop`
- `Git`

建议把以下工具放进系统环境变量：

- `JAVA_HOME`
- `ANDROID_HOME`
- `ANDROID_SDK_ROOT`

Windows 上额外建议：

- 把 `GRADLE_USER_HOME` 设到 **纯 ASCII 路径**
- 把 `ANDROID_SDK_HOME` 也设到 **纯 ASCII 路径**

推荐示例：

```powershell
[Environment]::SetEnvironmentVariable("GRADLE_USER_HOME", "C:\Users\Public\GradleHome", "Machine")
[Environment]::SetEnvironmentVariable("ANDROID_SDK_HOME", "C:\Users\Public\AndroidHome", "Machine")
```

原因：

- 中文用户名目录下，Gradle worker、Android Emulator、部分工具链缓存更容易出奇怪问题
- 本次实际踩到过 `GradleWorkerMain` 类加载异常和 Emulator/工具路径兼容问题

## 首次拉取后的准备

先在仓库根目录执行：

```powershell
git submodule update --init --recursive
npm --prefix komelia-epub-reader/ttu-ebook-reader install
npm --prefix komelia-epub-reader/komga-webui install
```

如果只是要验证主应用 UI，至少也要装好 `ttu-ebook-reader` 依赖，因为 Android 打包时会用到 Web UI 资源。

## Android 原生依赖构建

### 1. 先构建 Android Docker 镜像

```powershell
docker build -t komelia-build-android . -f ./cmake/android.Dockerfile
```

### 2. 在容器里构建目标 ABI 的原生库

`cmake/android-build.sh` 支持的目标架构是：

- `x86`
- `x86_64`
- `armv7a`
- `aarch64`

示例：

```powershell
docker run --rm -v ${PWD}:/build komelia-build-android x86_64
docker run --rm -v ${PWD}:/build komelia-build-android aarch64
```

这一步会产出：

- `cmake/build-android-x86_64`
- `cmake/build-android-aarch64`

### 3. 把 JNI 库复制到 Android 资源目录

Gradle 任务名和 Docker 里的架构名不是同一套命名，关系如下：

- `x86_64` -> `android-x86_64_copyJniLibs`
- `aarch64` -> `android-arm64_copyJniLibs`
- `armv7a` -> `android-armv7a_copyJniLibs`
- `x86` -> `android-x86_copyJniLibs`

示例：

```powershell
.\gradlew android-x86_64_copyJniLibs
.\gradlew android-arm64_copyJniLibs
```

## Web UI 资源构建

Android APK 构建前，先统一构建并拷贝 Web UI：

```powershell
.\gradlew buildWebui
```

这一步会处理：

- `komelia-epub-reader/ttu-ebook-reader`
- `komelia-epub-reader/komga-webui`

## Debug APK 构建

如果只是本地联调，优先构 `debug`：

```powershell
.\gradlew --% :komelia-app:assembleDebug -Pkomelia.android.abi=x86_64 -Pkomelia.android.skipJniVerification=true --no-daemon
```

常见输出目录：

- `komelia-app/build/outputs/apk/debug`

## Release APK 构建

### 1. 准备签名环境变量

需要以下环境变量：

- `KOMELIA_RELEASE_STORE_FILE`
- `KOMELIA_RELEASE_STORE_PASSWORD`
- `KOMELIA_RELEASE_KEY_ALIAS`
- `KOMELIA_RELEASE_KEY_PASSWORD`

### 2. 构建命令

给模拟器用的 `x86_64 release`：

```powershell
.\gradlew --% :komelia-app:assembleRelease -Pkomelia.android.abi=x86_64 -Pkomelia.android.skipJniVerification=true --no-daemon
```

给手机分发的 `arm64-v8a release`：

```powershell
.\gradlew --% :komelia-app:assembleRelease -Pkomelia.android.abi=arm64-v8a -Pkomelia.android.skipJniVerification=true --no-daemon
```

说明：

- PowerShell 下建议使用 `--%`，避免 `-Pkomelia.android.abi=...` 被错误拆开
- 本次已经实际踩过一次参数被 PowerShell 错解析，导致 Gradle 把 `.android.abi=x86_64` 当成任务名

常见输出目录：

- `komelia-app/build/outputs/apk/release`

## 构建后验证

### 1. 检查 APK 内的 ABI 是否正确

示例：

```powershell
Add-Type -AssemblyName System.IO.Compression.FileSystem
$zip = [IO.Compression.ZipFile]::OpenRead("komelia-app/build/outputs/apk/release/komelia-app-release.apk")
$zip.Entries | Where-Object { $_.FullName -like "lib/*/*.so" } | Select-Object -ExpandProperty FullName
$zip.Dispose()
```

期望结果：

- `x86_64` 包只包含 `lib/x86_64/*.so`
- `arm64-v8a` 包只包含 `lib/arm64-v8a/*.so`

### 2. 模拟器验证建议

Windows `x86_64` 主机上，只能稳定验证：

- `x86_64` 模拟器

不能在这台机器上直接跑：

- `arm64` AVD

官方 Emulator 会直接拒绝在 `x86_64` 主机上运行 `arm64` 系统镜像。

所以实际策略是：

- `x86_64 release` 用模拟器跑完整回归
- `arm64-v8a release` 做静态检查后，交给真机或 ARM64 云设备做最后验证

## 这次实际踩到的关键坑

### 1. 原生库缺失，APK 能装但阅读器打不开

表象：

- 首页封面不显示
- 书籍详情页封面异常
- 点击阅读进入 JNI 报错

根因：

- Android 原生依赖没有完整构出来，或者 JNI 没被复制进 APK

处理要点：

- 先完成 Docker 原生构建
- 再执行对应 ABI 的 `copyJniLibs`
- 最后再打 APK

### 2. `libffi.so` 错误依赖宿主机 `libc.so.6`

表象：

- Android 端 `dlopen failed: library "libc.so.6" not found`

根因：

- autotools 依赖在交叉编译时误用了宿主机工具链

处理结果：

- 已通过 `cmake/external/android-autotools.cmake` 统一约束 Android autotools 交叉编译环境
- `ffi.cmake`、`iconv.cmake`、`lcms2.cmake`、`exif.cmake` 等也已同步修过

### 3. PowerShell 会错误处理 Gradle `-P` 参数

表象：

- Gradle 报找不到任务 `.android.abi=x86_64`

处理方法：

- 用 `.\gradlew --% ...` 直通参数

### 4. 中文路径会让构建和模拟器环境更脆

表象：

- Gradle worker 异常
- Emulator/工具路径兼容问题

处理方法：

- `GRADLE_USER_HOME` 使用纯 ASCII 路径
- `ANDROID_SDK_HOME` 使用纯 ASCII 路径

### 5. 模拟器联调局域网服务时，直连不一定稳定

如果需要在模拟器里访问宿主机或局域网服务，优先考虑：

- `adb reverse`
- 必要时用 `socat` 做宿主机本地转发

这次验证登录时，实际用过这套方式把模拟器请求稳定转到目标服务。

## 推荐的最小稳定流程

如果你要从零稳定产出 APK，建议固定走这条顺序：

1. `git submodule update --init --recursive`
2. 安装两个 Web UI 的 npm 依赖
3. `docker build -t komelia-build-android . -f ./cmake/android.Dockerfile`
4. `docker run --rm -v ${PWD}:/build komelia-build-android x86_64`
5. `docker run --rm -v ${PWD}:/build komelia-build-android aarch64`
6. `.\gradlew android-x86_64_copyJniLibs`
7. `.\gradlew android-arm64_copyJniLibs`
8. `.\gradlew buildWebui`
9. `.\gradlew --% :komelia-app:assembleRelease -Pkomelia.android.abi=x86_64 -Pkomelia.android.skipJniVerification=true --no-daemon`
10. `.\gradlew --% :komelia-app:assembleRelease -Pkomelia.android.abi=arm64-v8a -Pkomelia.android.skipJniVerification=true --no-daemon`

## 交付建议

对外分发时优先使用：

- `arm64-v8a release`

本地自动化验证优先使用：

- `x86_64 release`

不要把以下内容提交到仓库：

- 根目录临时审计输出
- 本地签名 keystore
- 本机生成的 daemon/toolchain 配置
- `build/` 下的产物

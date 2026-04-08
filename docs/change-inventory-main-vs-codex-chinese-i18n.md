# `main` 与 `codex/chinese-i18n-android-build` 差异清单

## 对比基线

- 基线提交：`d69342979fc8a520484bd98d441b30d15ef37e67`
- 当前提交：`ea658c169b8ab1940034f41e44bba3d304750144`
- 变更总文件数：`362`
- 新增文件：`102`
- 删除文件：`1`
- 修改文件：`259`

## 总体改动方向

- 主应用 UI 文案从零散硬编码迁移到统一的 strings 契约。
- 新增中文语言包，并实现“跟随系统语言 + 手动切换 + 持久化”。
- EPUB 两套前端和 Komf 扩展补齐独立的 i18n 入口。
- Android 原生依赖与 APK 构建链路被补齐，避免产出缺库 APK。
- 新增 UI 硬编码审计和 i18n 旁路审计。
- 补了一份 Android APK 构建说明文档，并把不该提交的本地文件加入 `.gitignore`。

## 新增文件

### 1. 构建与审计辅助

- `cmake/external/android-autotools.cmake`
- `cmake/external/patches/apply_vips_patch.py`
- `config/ui-strings-audit-whitelist.txt`
- `docs/android-apk-build-playbook.md`

这一组的作用：

- 给 Android autotools 依赖提供统一的交叉编译环境。
- 用脚本方式稳定给 `vips` 打 patch，避免 `git apply` 在构建容器里不稳定。
- 增加 UI 硬编码审计白名单。
- 把 Android APK 的完整构建流程、依赖要求和踩坑记录写成文档。

### 2. 主应用系统语言解析

- `komelia-app/src/androidMain/kotlin/snd/komelia/SystemLanguage.android.kt`
- `komelia-app/src/commonMain/kotlin/snd/komelia/SystemLanguage.kt`
- `komelia-app/src/jvmMain/kotlin/snd/komelia/SystemLanguage.jvm.kt`
- `komelia-app/src/wasmJsMain/kotlin/snd/komelia/SystemLanguage.wasmJs.kt`

这一组的作用：

- 把“读取当前运行机器/浏览器语言”的能力抽成平台接口。
- Android/JVM/Wasm 各自给出系统语言标签实现。
- 让应用在 `SYSTEM` 模式下能自动解析到最终 UI 语言。

### 3. 语言模式模型

- `komelia-domain/core/src/commonMain/kotlin/snd/komelia/settings/model/AppLanguageMode.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/AppLanguage.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/AppLanguageMode.kt`

这一组的作用：

- 增加 `SYSTEM / ENGLISH / SIMPLIFIED_CHINESE` 的用户偏好模型。
- 区分“用户选择的语言模式”和“最终解析后的实际语言”。

### 4. 主应用 strings 基础设施

- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/CommonStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/DialogStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/EnExtraStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/MenuStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/RuntimeAppStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/ScreenStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/StringsProvider.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/StringsResolver.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/ToastStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/ValidationStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/ZhHansStrings.kt`

这一组的作用：

- 把原来分散的文案模型按功能域拆开。
- 增加英文补充 strings 和完整中文 strings。
- 增加运行时 strings 解析、注入和全局状态入口。

### 5. 首页筛选显示修复

- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/HomeFilterLabels.kt`

这一组的作用：

- 把首页默认筛选器的显示名称从“直接使用持久化 label”改为“优先按默认筛选类型实时本地化”。
- 这样旧数据库里存着英文标签时，中文界面也能即时显示中文。

### 6. EPUB / Web UI i18n

- `komelia-epub-reader/komga-webui/src/i18n.ts`
- `komelia-epub-reader/komga-webui/src/locales/zh-Hans.json`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/i18n/strings.ts`

这一组的作用：

- `komga-webui` 增加 `vue-i18n` 初始化入口和简体中文词条。
- `ttu-ebook-reader` 增加独立强类型文案对象和语言选择逻辑。

### 7. SQLite 持久化与迁移

- `komelia-infra/database/sqlite/src/commonMain/composeResources/files/migrations/app/V13__app_language_mode.sql`

这一组的作用：

- 给本地数据库增加 `app_language_mode` 迁移，确保语言偏好可持久化。

### 8. Android SQLite JNI 依赖

- `komelia-infra/database/sqlite/src/androidMain/jniLibs/arm64-v8a/libsqlitejdbc.so`
- `komelia-infra/database/sqlite/src/androidMain/jniLibs/armeabi-v7a/libsqlitejdbc.so`
- `komelia-infra/database/sqlite/src/androidMain/jniLibs/x86/libsqlitejdbc.so`
- `komelia-infra/database/sqlite/src/androidMain/jniLibs/x86_64/libsqlitejdbc.so`

这一组的作用：

- 把 Android 侧 SQLite JNI 库纳入源码树，避免 APK 缺失 `libsqlitejdbc.so`。

### 9. Android 原生图像/JNI 依赖

- `komelia-infra/jni/src/androidMain/jniLibs/arm64-v8a/`
- `komelia-infra/jni/src/androidMain/jniLibs/x86_64/`

这两个目录下新增的原生库包括：

- `libbrotlicommon.so`
- `libbrotlidec.so`
- `libbrotlienc.so`
- `libdav1d.so`
- `libde265.so`
- `libexif.so`
- `libexpat.so`
- `libffi.so`
- `libgio-2.0.so`
- `libglib-2.0.so`
- `libgmodule-2.0.so`
- `libgobject-2.0.so`
- `libheif.so`
- `libhwy.so`
- `libiconv.so`
- `libintl.so`
- `libjpeg.so`
- `libjxl.so`
- `libjxl_cms.so`
- `libjxl_threads.so`
- `libkomelia_android_bitmap.so`
- `libkomelia_vips.so`
- `liblcms2.so`
- `libomp.so`
- `libpng16.so`
- `libsharpyuv.so`
- `libtiff.so`
- `libturbojpeg.so`
- `libvips.so`
- `libwebp.so`
- `libwebpdecoder.so`
- `libwebpdemux.so`
- `libwebpmux.so`
- `libz.so`

这一组的作用：

- 把 Android 阅读器与缩略图链路真正依赖的原生库纳入 APK 打包输入。
- 修复“首页封面不显示、点击阅读 JNI 崩溃”的缺库问题。

### 10. Komf 扩展 strings

- `komelia-komf-extension/shared/src/wasmJsMain/kotlin/snd/komelia/strings/ExtensionStrings.kt`

这一组的作用：

- 给扩展补齐独立 strings/provider，不和主应用直接共用类型。

### 11. 内嵌 Web 资源

- `komelia-ui/src/commonMain/composeResources/files/komga.html`
- `komelia-ui/src/commonMain/composeResources/files/ttsu.html`

这一组的作用：

- 把构建后的 EPUB Web UI 资源带进 Compose resources。
- Android/JVM 主应用可直接加载内嵌阅读器页面。

## 删除文件

- `komelia-domain/offline/src/wasmJsMain/kotlin/snd/komelia/offline/OfflineModule.wasmJs.kt`

删除原因：

- 这部分 `wasmJs actual` 实现与当前结构冲突，删除后由新的离线/平台实现链路接管，避免错误的旧入口继续参与构建。

## 修改文件

### 1. 根目录与构建系统

修改文件：

- `.gitignore`
- `README.md`
- `build.gradle.kts`
- `cmake/android-build.sh`
- `cmake/external/exif.cmake`
- `cmake/external/ffi.cmake`
- `cmake/external/iconv.cmake`
- `cmake/external/lcms2.cmake`
- `cmake/external/patches/vips_thumbnail_resampling_kernel.patch`
- `cmake/external/vips.cmake`
- `gradle.properties`
- `komelia-app/build.gradle.kts`

这一组改动内容：

- 增加 UI 硬编码审计和 i18n 旁路审计任务。
- 增加 Android JNI 完整性校验，避免继续打出损坏 APK。
- 支持通过 `-Pkomelia.android.abi` 按 ABI 构建 APK。
- 修正 Android 原生依赖复制逻辑，只复制真实文件。
- Android Docker 构建脚本增加可选 target 参数。
- autotools 依赖显式使用 Android 交叉编译工具链，避免误用宿主机 `gcc`。
- `vips` patch 处理方式改成脚本化。
- `komelia-app/build.gradle.kts` 里接入 release 签名环境变量和 Android 包相关配置。
- `.gitignore` 增加本地报告、签名 keystore、daemon 文件等忽略项。
- `README.md` 增加 Android 构建说明文档入口。

### 2. 主应用运行时注入与语言解析

修改文件：

- `komelia-app/src/commonMain/kotlin/snd/komelia/AppModule.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/CompositionLocals.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/DependencyContainer.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/MainScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/MainView.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/ViewModelFactory.kt`

这一组改动内容：

- 把 `DependencyContainer.appStrings` 真正接到运行时 UI 注入链路。
- `LocalStrings` 不再只是默认英文占位，而是由 provider 驱动。
- 主应用启动时会根据系统语言和用户偏好解析实际语言。
- ViewModel 层能拿到语言相关仓储和 strings 能力。

### 3. 领域模型与设置持久化

修改文件：

- `komelia-domain/core/src/commonMain/kotlin/snd/komelia/homefilters/HomeFilters.kt`
- `komelia-domain/core/src/commonMain/kotlin/snd/komelia/settings/CommonSettingsRepository.kt`
- `komelia-infra/database/shared/src/commonMain/kotlin/snd/komelia/db/AppSettings.kt`
- `komelia-infra/database/shared/src/commonMain/kotlin/snd/komelia/db/repository/SettingsRepositoryWrapper.kt`
- `komelia-infra/database/sqlite/build.gradle.kts`
- `komelia-infra/database/sqlite/src/commonMain/kotlin/snd/komelia/db/migrations/AppMigrations.kt`
- `komelia-infra/database/sqlite/src/commonMain/kotlin/snd/komelia/db/settings/ExposedSettingsRepository.kt`
- `komelia-infra/database/sqlite/src/commonMain/kotlin/snd/komelia/db/tables/AppSettingsTable.kt`

这一组改动内容：

- `AppSettings` 新增语言模式持久化字段。
- 仓储接口增加语言模式读写能力。
- SQLite 表结构和迁移同步更新。
- 首页默认筛选器增加“默认类型识别”，为旧英文 label 的运行时本地化做准备。
- SQLite Android 构建脚本补充 JNI 处理。

### 4. Android 图像加载与原生库装载

修改文件：

- `komelia-infra/image-decoder/vips/src/androidMain/kotlin/snd/komelia/image/VipsSharedLibrariesLoader.kt`

这一组改动内容：

- 调整 Android 侧 `vips` 相关共享库加载顺序和依赖装载逻辑。
- 配合新增 JNI 库，避免运行时 `UnsatisfiedLinkError`。

### 5. EPUB 前端 `komga-webui`

修改文件：

- `komelia-epub-reader/komga-webui/src/components/EpubReader.vue`
- `komelia-epub-reader/komga-webui/src/external.ts`
- `komelia-epub-reader/komga-webui/src/locales/en.json`
- `komelia-epub-reader/komga-webui/src/main.ts`

这一组改动内容：

- Web UI 不再写死英文 locale。
- 从宿主或浏览器环境读取语言模式/语言标签。
- 英文词条补齐与中文词条契约对齐。
- 入口处初始化 i18n，并在阅读器组件里使用。

### 6. EPUB 前端 `ttu-ebook-reader`

修改文件：

- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/Reader.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/book-reader/book-reader-header.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/book-reader/book-reader-image-gallery/book-reader-image-gallery.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/book-reader/book-toc/book-toc.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/book-reader/reactive-elements.ts`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/settings/settings-content.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/settings/settings-custom-theme.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/settings/settings-header.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/settings/settings-user-font-add.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/components/settings/settings-user-font-dialog.svelte`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/data/store.ts`
- `komelia-epub-reader/ttu-ebook-reader/src/lib/external.ts`

这一组改动内容：

- 增加前端语言 store。
- 阅读器标题、TOC、图片画廊、设置页、用户字体对话框等改成从 i18n 取文案。
- 支持宿主注入 UI 语言，没有宿主时回退浏览器语言/本地存储。

### 7. Komf 扩展

修改文件：

- `komelia-komf-extension/app/build.gradle.kts`
- `komelia-komf-extension/background/build.gradle.kts`
- `komelia-komf-extension/content/build.gradle.kts`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/AppState.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/dialogs/IdentifyDialog.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/dialogs/SettingsDialog.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/kavita/KavitaComponent.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/kavita/KavitaLibraryActions.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/kavita/KavitaSeriesActions.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/komga/KomgaLibraryActions.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/komga/KomgaSeriesActions.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/settings/ConnectionTab.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/settings/JobsTab.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/settings/NotificationsTab.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/settings/ProcessingTab.kt`
- `komelia-komf-extension/content/src/wasmJsMain/kotlin/snd/komelia/settings/ProvidersTab.kt`
- `komelia-komf-extension/popup/build.gradle.kts`
- `komelia-komf-extension/popup/src/wasmJsMain/kotlin/snd/komelia/Main.kt`
- `komelia-komf-extension/popup/src/wasmJsMain/kotlin/snd/komelia/OriginSettings.kt`
- `komelia-komf-extension/shared/build.gradle.kts`

这一组改动内容：

- 扩展增加独立 strings/provider 与语言模式逻辑。
- popup、content settings、Kavita/Komga 动作菜单等文案迁入 strings。
- 浏览器存储中的语言偏好与主应用语义对齐。
- 构建脚本补齐共享模块依赖与打包链路。

### 8. 主应用 strings 契约与语言资源

修改文件：

- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/AppStrings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/EnStrings.kt`

这一组改动内容：

- `AppStrings` 从原来较粗的字符串对象扩展为分组后的 strings 契约根。
- 增加大量 formatter 方法与按功能域分组的字段。
- `EnStrings` 调整为接入新的分组结构，和 `EnExtraStrings`/`ZhHansStrings` 对齐。

### 9. 主应用 UI：书籍、集合、颜色、公共组件

修改文件：

- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/book/BookInfoContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/book/BookScreenContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/collection/CollectionContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/collection/CollectionScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/color/PresetsState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/color/view/ColorCorrectionContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/color/view/ColorCorrectionScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/color/view/CurvesContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/color/view/LevelsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/color/view/PresetsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/BookReadButton.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/TagList.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/cards/BookItemCard.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/cards/CollectionItemCard.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/cards/ReadListItemCard.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/cards/SeriesItemCard.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/cards/ThumbnailEditCard.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/components/DownloadDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/components/DropdownChoiceMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/components/ErrorContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/components/ExpandableText.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/components/Pagination.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/components/TextFields.kt`

这一组改动内容：

- 书籍信息页把 `pages left` 等硬编码迁入 strings。
- 集合、颜色校正、预设、缩略图编辑等页面接入 strings。
- 通用下拉菜单、错误页、文本输入、下载对话框等公共组件不再直接写英文。

### 10. 主应用 UI：菜单与批量操作

修改文件：

- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/BookActionsMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/CollectionActionsMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/LibraryActionsMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/OneshotActionsMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/ReadListActionsMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/SeriesActionsMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/bulk/BookBulkActions.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/bulk/BulkActions.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/bulk/CollectionBulkActions.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/bulk/ReadListBulkActions.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/common/menus/bulk/SeriesBulkActions.kt`

这一组改动内容：

- 把常用动作菜单和批量操作菜单里的按钮、确认文案、标签文案迁入 strings。
- 这部分是主应用去硬编码里改动最密集的一块之一。

### 11. 主应用 UI：对话框

修改文件：

- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/AppDialogs.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/ConfirmationDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/LinksEditContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/PosterTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/edit/AuthorsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/edit/BookEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/edit/GeneralTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/edit/LinksTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/edit/TagsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/editbulk/AuthorsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/editbulk/BookBulkEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/book/editbulk/TagsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/collectionadd/AddToCollectionDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/collectionedit/CollectionEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/collectionedit/CollectionEditDialogViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/collectionedit/GeneralTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/filebrowser/FileBrowserDialogContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/komf/identify/KomfIdentifyDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/komf/identify/KomfIdentifyDialogViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/komf/identify/KomfLibraryIdentifyViewmodel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/komf/reset/KomfResetMetadataDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/libraryedit/GeneralTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/libraryedit/LibraryEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/libraryedit/LibraryEditDialogViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/libraryedit/MetadataTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/libraryedit/OptionsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/libraryedit/ScannerTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/oneshot/OneshotEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/oneshot/OneshotGeneralTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/oneshot/OneshotTagsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/readlistadd/AddToReadListDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/readlistedit/GeneralTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/readlistedit/ReadListEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/readlistedit/ReadListEditDialogViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/edit/AlternativeTitlesTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/edit/GeneralTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/edit/LinksTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/edit/SeriesEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/edit/SharingTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/edit/TagsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/editbulk/GeneralTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/editbulk/SeriesBulkEditDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/editbulk/SharingTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/series/editbulk/TagsTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/tabs/DialogTab.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/tabs/TabDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/update/UpdateDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/update/UpdateProgressDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/user/PasswordChangeDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/user/UserAddDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/user/UserAddDialogViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/dialogs/user/UserEditDialog.kt`

这一组改动内容：

- 基本把主应用主要编辑/新增/确认对话框的硬编码文案都迁进 strings。
- 其中也包含 bulk edit、Komf 识别、用户管理、库编辑、文件浏览器等原来最零散的对话框区域。

### 12. 主应用 UI：首页、库、登录、阅读、搜索、系列、设置、顶部导航

修改文件：

- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/error/ErrorView.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/HomeContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/HomeScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/edit/BookFilterEditState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/edit/FilterEditScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/edit/SeriesFilterEditState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/edit/view/BookConditionsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/edit/view/CommonConditionsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/edit/view/FilterEditContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/home/edit/view/SeriesConditionsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/komf/KomfNavigationContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/library/LibraryScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/library/view/LibraryCollectionsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/library/view/LibraryReadListsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/login/LoginContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/login/LoginScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/login/LoginViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/login/offline/OfflineLoginContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/oneshot/OneshotScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/oneshot/OneshotScreenContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/TitleBarContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/epub/EpubReaderViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/epub/KomgaEpubReaderState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/epub/TtsuReaderState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/ReaderState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/ReaderViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/common/ReaderImageContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/common/ReaderNavigationHelpDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/continuous/ContinuousReaderContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/continuous/ContinuousReaderState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/paged/PagedReaderContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/paged/PagedReaderState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/panels/PanelsReaderContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/panels/PanelsReaderState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/settings/BottomSheetSettingsOverlay.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/settings/CommonImageSettings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/settings/SettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/reader/image/settings/SettingsSideMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/readlist/BookReadlistsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/readlist/ReadListContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/readlist/ReadListScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/search/SearchBar.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/search/SearchContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/search/SearchScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/series/SeriesScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/series/list/SeriesListContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/series/view/BooksContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/series/view/SeriesContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/series/view/SeriesDescriptionRow.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/MobileSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/account/AccountContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/account/AccountSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/analysis/MediaAnalysisContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/analysis/MediaAnalysisScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/announcements/AnnouncementsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/appearance/AppSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/appearance/AppSettingsViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/appearance/AppearanceSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/authactivity/AuthenticationActivityContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/authactivity/AuthenticationActivityScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/epub/EpubReaderSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/epub/EpubReaderSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/ImageReaderSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/ImageReaderSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/ImageReaderSettingsViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/onnxruntime/Common.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/onnxruntime/DownloadDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/onnxruntime/InstallDialog.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/onnxruntime/PanelDetectionSettings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/onnxruntime/SettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/imagereader/onnxruntime/UpcsalerSettings.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/LibraryTabs.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/TextFields.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/general/KavitaConnectionState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/general/KomfSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/general/KomfSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/general/KomgaConnectionState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/jobs/KomfJobsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/jobs/KomfJobsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/notifications/AppriseState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/notifications/DiscordState.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/notifications/KomfNotificationSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/notifications/view/AppriseContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/notifications/view/DiscordContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/notifications/view/KomfNotificationSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/notifications/view/NotificationContextContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/processing/KomfProcessingSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/processing/KomfProcessingSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/providers/KomfProvidersSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/komf/providers/KomfProvidersSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/navigation/SettingsNavigationMenu.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/offline/OfflineSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/offline/downloads/OfflineDownloadsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/offline/logs/OfflineLogsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/offline/users/OfflineSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/server/ServerManagementContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/server/ServerSettingsContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/server/ServerSettingsScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/server/ServerSettingsViewModel.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/updates/AppUpdatesContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/updates/AppUpdatesScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/users/UsersContent.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/settings/users/UsersScreen.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/topbar/AppBar.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/topbar/DownloadsPopupIcon.kt`
- `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/topbar/NavigationMenuContent.kt`
- `komelia-ui/src/jvmMain/kotlin/snd/komelia/ui/log/LogView.kt`

这一组改动内容：

- 把主应用高频页面和设置页的大部分硬编码文案迁移到 strings。
- 新增外观页的语言切换控件，并持久化用户语言模式。
- 首页默认筛选、详情页剩余页数、设置根页“Komf 设置”等残留英文被补齐。
- 阅读器帮助、设置、通知、错误、提示、登录页、导航栏等文案全部统一到 strings 契约。
- `Komf` 连接错误文本改成通过 strings formatter 生成。

## 你最该关注的“关键差异”

- 如果你只看“国际化底座”，最关键的是 `AppStrings`、`StringsProvider`、`StringsResolver`、`ZhHansStrings`、`SystemLanguage*`、数据库语言模式持久化这几块。
- 如果你只看“Android APK 构建稳定性”，最关键的是 `build.gradle.kts`、`komelia-app/build.gradle.kts`、`cmake/*`、`komelia-infra/jni/src/androidMain/jniLibs/*`、`komelia-infra/database/sqlite/src/androidMain/jniLibs/*`。
- 如果你只看“为什么 UI 现在能中文化”，最关键的是 `komelia-ui/src/commonMain/kotlin/snd/komelia/ui/strings/*` 这一整套新文件，以及大量 UI 页面从硬编码迁到 `LocalStrings` 的改动。

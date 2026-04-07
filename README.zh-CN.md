# Komelia 中文说明

## 仓库定位

本仓库是 `Komelia` 的独立维护分支，由 `yexi-by` 持续维护。

- 当前维护仓库：https://github.com/yexi-by/Komelia
- 上游仓库：https://github.com/Snd-R/Komelia

如果你准备直接使用本仓库的构建产物、更新功能和后续发布，请以本仓库为准。

## 更新与发布

- 应用内“检查更新”现在会指向本仓库的 GitHub Releases
- 本仓库发布页：https://github.com/yexi-by/Komelia/releases

说明：

- Google Play、F-Droid、AUR 仍然是上游渠道，不代表本仓库当前分支的最新构建
- 如果你从本仓库分发 APK，建议只使用本仓库 Release 中上传的 APK 资产

## 主要改动方向

当前这条维护线已经补上了以下内容：

- 主应用中文界面覆盖
- 跟随系统语言与手动语言切换
- 首页筛选器编辑器等残留英文清理
- Android APK 构建链修复与整理

## 文档入口

- 英文主说明：[README.md](README.md)
- Android APK 构建流程与踩坑记录：[docs/android-apk-build-playbook.md](docs/android-apk-build-playbook.md)
- 当前分支相对原始 `main` 的改动清单：
  [docs/change-inventory-main-vs-codex-chinese-i18n.md](docs/change-inventory-main-vs-codex-chinese-i18n.md)

## Android 构建

Android APK 的推荐构建说明已经整理在：

- [docs/android-apk-build-playbook.md](docs/android-apk-build-playbook.md)

如果只是想快速找到关键步骤，最少需要完成这些准备：

1. 初始化 submodule 与前端依赖
2. 用 Docker 构建 Android 原生依赖
3. 执行对应 ABI 的 `copyJniLibs`
4. 执行 `buildWebui`
5. 再构建 `debug` 或 `release` APK

## 上游说明

本仓库并不否认上游来源，后续维护会在以下原则下进行：

- 保留对上游来源的明确说明
- 按自身节奏维护汉化、构建与发布
- 需要时自行发布 APK 与更新说明

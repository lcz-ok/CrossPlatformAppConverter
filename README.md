<div align="center">
  <img src="assets/icon.svg" width="110" alt="POLYFORGE" />
  <h1>POLYFORGE · 跨平台应用转换器</h1>
  <p><b>Cross-Platform App Converter</b></p>
  <p>
    Windows ⇄ Android 双向转换 · 源码语言识别 · 二进制还原
  </p>
</div>

---

## 下载 / Download

| 平台 | 文件 |
| --- | --- |
| Windows 10/11 x64（免安装） | [POLYFORGE-1.0.0-Windows-x64.exe](../../releases/latest) |
| Android 7.0+ | [POLYFORGE-1.0.0-Android.apk](../../releases/latest) |

> 所有文件均在浏览器 / 设备本地解析，**不会上传任何项目文件**。

## 功能 / Features

- **源码语言识别**：上传文件或整个文件夹，基于扩展名矩阵、内容签名与二进制魔数三重判定，识别 30+ 种语言、18 类框架（WPF、WinForms、.NET、Android、Flutter、Electron、Tauri、Unity、Qt…），输出置信度、语言分布与逐文件依据。
- **应用双向转换**：桌面 → Android（Kotlin + Compose）、Android → 桌面（.NET 8 + WPF）、跨平台（Flutter）。生成可导入 IDE 的真实工程骨架，支持单文件下载与 ZIP 打包，并导出结构映射迁移清单。
- **二进制还原**：识别 PE/.NET、ELF、APK、DEX、Java Class、Mach-O、WASM，计算信息熵，提取 URL、路径、系统 API、注册表与错误信息，并按推断语言重建带注释的源码骨架。
- **关于 / About**：应用右上角可查看作者信息。

> 二进制无法 100% 还原原始源码；转换产出的是工程骨架与 API 映射清单，复杂业务逻辑仍需人工迁移。

## 项目结构 / Layout

```
.
├── index.html                 # 完整应用（单文件，浏览器直接打开）
├── desktop/                   # Electron 桌面壳（打包 Windows EXE）
│   ├── main.js
│   ├── package.json
│   └── sync.js
├── android/                   # Android WebView 壳（打包 APK）
│   └── app/src/main/java/com/liuchenzhi/polyforge/MainActivity.java
└── assets/icon.svg
```

## 本地运行 / Run

```bash
# 浏览器
open index.html

# Windows 桌面版（需 Node.js 18+）
cd desktop && npm install && npm start

# Windows 便携版 EXE
cd desktop && npm install && npm run dist:win

# Android APK（需 Android SDK 34 + JDK 17）
cd android && ./gradlew assembleRelease
```

## 关于 / About

**刘宸至一手创作！**

**Solely created by Liu Chenzhi.**

Version 1.0 · 2026

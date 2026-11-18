# Designer Rewrite Scaffold

现代化 Android 脚手架（Kotlin + Compose + Hilt）。与根目录旧工程并行，互不影响。

## 打开方式

用 Android Studio 打开 **`rewrite/`** 目录（不要打开仓库根目录）。

## 当前内容

- 作品流：Compose 卡片、下拉刷新、Paging 3 + Room，每页 10 条
- 全屏详情：左右滑动相邻作品
- 侧栏：设计狮、搜索、我的喜欢、分享、设置，以及本机登录和个人页
- 默认数据源是 Fake（Dribbble API v1 已关闭）

## 切换数据源

在 `app/build.gradle.kts` 中改：

```kotlin
buildConfigField("String", "DATA_SOURCE", "\"fake\"") // fake | remote | dribbble
```

并在 `DataModule` 中按 `BuildConfig.DATA_SOURCE` 提供不同 `ShotRepository` 实现。

详见 `../docs/modernization/ARCHITECTURE.md`。

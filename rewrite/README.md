# Designer Rewrite Scaffold

现代化 Android 脚手架（Kotlin + Compose + Hilt）。与根目录旧工程并行，互不影响。

## 打开方式

用 Android Studio 打开 **`rewrite/`** 目录（不要打开仓库根目录）。

## 当前内容

- Version Catalog：`gradle/libs.versions.toml`
- Feed：Compose + Paging 3 + Room 缓存（`CachedShotRepository`）
- 详情：点击进入 `ShotDetailScreen`
- DI：Hilt 按 `DATA_SOURCE` 绑定 Fake / API `ShotRemoteDataSource`

## 切换数据源

在 `app/build.gradle.kts` 中改：

```kotlin
buildConfigField("String", "DATA_SOURCE", "\"fake\"") // fake | remote | dribbble
```

并在 `DataModule` 中按 `BuildConfig.DATA_SOURCE` 提供不同 `ShotRepository` 实现。

详见 `../docs/modernization/ARCHITECTURE.md`。

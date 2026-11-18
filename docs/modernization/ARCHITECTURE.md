# Designer 现代化重写方案

旧工程（根目录 `app/`）保留不动。新实现放在 `rewrite/`，按 Compose + Clean Architecture 从零搭建。

## 两条数据源路线

### 路线 A：仍对接 Dribbble

- 前提：持有可用的 OAuth Client / API 权限（公开 API 对新应用已基本关闭）。
- `data` 层实现 `DribbbleShotRepository`，Base URL：`https://api.dribbble.com/v1/`。
- Auth：Authorization Code → Access Token，存 DataStore；OkHttp Interceptor 注入 Bearer。
- 能力：Feed、详情、当前用户、Like（与旧版对齐）。
- 风险：凭证过期或政策变更会导致功能不可用；上线前必须先验证 token 与配额。

### 路线 B：自建后端 / 聚合（推荐默认）

- 前提：自有 API（或 Mock），契约与领域模型稳定，不绑死 Dribbble。
- `data` 层：`ShotRemoteDataSource`（Fake / API）+ `CachedShotRepository`（Room 缓存 + Paging）。
- Auth：自有账号体系，或先做游客浏览 + 后期登录。
- 能力：同样的 Feed / 详情 / 喜欢；数据来源可换 Behance、CMS、爬取合规源等。
- 好处：产品与技术演进不受第三方 API 生死影响。

**脚手架默认走路线 B 的 Fake 实现**，用 `BuildConfig.DATA_SOURCE=fake|remote|dribbble` 切换。

## 技术选型

| 职责 | 选型 |
|------|------|
| 语言 / UI | Kotlin + Jetpack Compose + Material 3 |
| 架构 | 单 module 起步，包结构分层（presentation / domain / data） |
| 异步 | Coroutines + Flow / StateFlow |
| DI | Hilt |
| 网络 | Retrofit + OkHttp + kotlinx.serialization |
| 图片 | Coil |
| 本地 | Room（列表缓存）+ DataStore（token / 偏好） |
| 分页 | Paging 3 |
| 导航 | Navigation Compose |
| 构建 | Version Catalog + KSP |

## 包结构（单 module）

```
com.mathiasluo.designer
├── DesignerApp.kt
├── di/
├── navigation/
├── presentation/
│   ├── feed/
│   ├── detail/
│   ├── profile/
│   └── auth/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── data/
    ├── remote/
    ├── local/
    ├── fake/
    └── repository/
```

稳定后再拆 `core` / `feature-feed` 等 module。

## 落地顺序

1. Compose 壳 + 主题 + Navigation ✅  
2. Domain 模型 + Fake Repository + Feed / Detail UI ✅  
3. 接 Remote（路线 B）或 Dribbble（路线 A）— API 骨架已就绪  
4. Paging + Room 缓存 ✅（`CachedShotRepository`）  
5. 本机登录与喜欢（DataStore 保存用户名，喜欢保存在本次进程）✅  

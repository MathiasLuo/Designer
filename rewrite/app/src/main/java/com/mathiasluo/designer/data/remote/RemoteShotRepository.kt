package com.mathiasluo.designer.data.remote

import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 路线 B 实现骨架：接上真实 API 后替换 Fake 绑定即可。
 * 当前未在 DataModule 默认启用，避免无后端时启动失败。
 */
@Singleton
class RemoteShotRepository @Inject constructor(
    private val api: DesignerApi,
) : ShotRepository {

    private val cache = MutableStateFlow<List<Shot>>(emptyList())

    override fun observeFeed(): Flow<List<Shot>> = cache.asStateFlow()

    override suspend fun getShot(id: String): Shot? {
        cache.value.firstOrNull { it.id == id }?.let { return it }
        return runCatching { api.getShot(id).toDomain() }.getOrNull()
    }

    override suspend fun refreshFeed() {
        val remote = api.getShots(page = 1, perPage = 30).map { it.toDomain() }
        cache.value = remote
    }
}

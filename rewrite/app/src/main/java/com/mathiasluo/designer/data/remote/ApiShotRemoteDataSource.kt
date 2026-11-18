package com.mathiasluo.designer.data.remote

import com.mathiasluo.designer.data.ShotRemoteDataSource
import com.mathiasluo.designer.domain.model.Shot
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiShotRemoteDataSource @Inject constructor(
    private val api: DesignerApi,
) : ShotRemoteDataSource {
    override suspend fun fetchShots(page: Int, pageSize: Int): List<Shot> =
        api.getShots(page = page, perPage = pageSize).map { it.toDomain() }

    override suspend fun fetchShot(id: String): Shot? =
        runCatching { api.getShot(id).toDomain() }.getOrNull()
}

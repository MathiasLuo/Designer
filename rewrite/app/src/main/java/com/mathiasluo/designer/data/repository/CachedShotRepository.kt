package com.mathiasluo.designer.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mathiasluo.designer.data.ShotRemoteDataSource
import com.mathiasluo.designer.data.local.ShotDao
import com.mathiasluo.designer.data.local.toDomain
import com.mathiasluo.designer.data.local.toEntity
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CachedShotRepository @Inject constructor(
    private val shotDao: ShotDao,
    private val remote: ShotRemoteDataSource,
) : ShotRepository {

    override fun pagedFeed(): Flow<PagingData<Shot>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = 2,
        ),
        pagingSourceFactory = { shotDao.pagingSource() },
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override suspend fun getShot(id: String): Shot? {
        shotDao.getById(id)?.toDomain()?.let { return it }
        val remoteShot = remote.fetchShot(id) ?: return null
        shotDao.insertAll(listOf(remoteShot.toEntity(sortIndex = Int.MAX_VALUE)))
        return remoteShot
    }

    override suspend fun refreshFeed() {
        val remoteShots = remote.fetchShots(page = 1, pageSize = REFRESH_PAGE_SIZE)
        shotDao.replaceAll(remoteShots.mapIndexed { index, shot -> shot.toEntity(index) })
    }

    private companion object {
        const val PAGE_SIZE = 6
        const val REFRESH_PAGE_SIZE = 18
    }
}

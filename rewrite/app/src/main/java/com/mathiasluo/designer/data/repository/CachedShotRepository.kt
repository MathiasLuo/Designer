package com.mathiasluo.designer.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.mathiasluo.designer.data.ShotRemoteDataSource
import com.mathiasluo.designer.data.local.DesignerDatabase
import com.mathiasluo.designer.data.local.ShotDao
import com.mathiasluo.designer.data.local.toDomain
import com.mathiasluo.designer.data.local.toEntity
import com.mathiasluo.designer.domain.feed.SHOT_PAGE_SIZE
import com.mathiasluo.designer.domain.feed.filterShots
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
    private val database: DesignerDatabase,
) : ShotRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun pagedFeed(): Flow<PagingData<Shot>> = Pager(
        config = PagingConfig(
            pageSize = SHOT_PAGE_SIZE,
            initialLoadSize = SHOT_PAGE_SIZE,
            enablePlaceholders = false,
            prefetchDistance = 2,
        ),
        remoteMediator = ShotRemoteMediator(remote, database),
        pagingSourceFactory = { shotDao.pagingSource() },
    ).flow.map { pagingData ->
        pagingData.map { it.toDomain() }
    }

    override fun observeCached(): Flow<List<Shot>> =
        shotDao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun getShot(id: String): Shot? {
        shotDao.getById(id)?.toDomain()?.let { return it }
        val remoteShot = remote.fetchShot(id) ?: return null
        shotDao.insertAll(listOf(remoteShot.toEntity(sortIndex = Int.MAX_VALUE)))
        return remoteShot
    }

    override suspend fun refreshFeed() {
        val remoteShots = remote.fetchShots(page = 1, pageSize = SHOT_PAGE_SIZE)
        shotDao.replaceAll(remoteShots.mapIndexed { index, shot -> shot.toEntity(index) })
    }

    override suspend fun search(query: String): List<Shot> {
        if (query.isBlank()) return emptyList()
        return filterShots(remote.fetchShots(page = 1, pageSize = 50), query)
    }

    override suspend fun appendIfNeeded(loadedCount: Int) {
        if (loadedCount <= 0) return
        val page = (loadedCount / SHOT_PAGE_SIZE) + 1
        val shots = remote.fetchShots(page, SHOT_PAGE_SIZE)
        if (shots.isEmpty()) return
        val start = (page - 1) * SHOT_PAGE_SIZE
        shotDao.insertAll(shots.mapIndexed { index, shot -> shot.toEntity(start + index) })
    }
}

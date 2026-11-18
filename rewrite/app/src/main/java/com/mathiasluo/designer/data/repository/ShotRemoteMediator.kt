package com.mathiasluo.designer.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.mathiasluo.designer.data.ShotRemoteDataSource
import com.mathiasluo.designer.data.local.DesignerDatabase
import com.mathiasluo.designer.data.local.ShotEntity
import com.mathiasluo.designer.data.local.toEntity
import com.mathiasluo.designer.domain.feed.SHOT_PAGE_SIZE

@OptIn(ExperimentalPagingApi::class)
class ShotRemoteMediator(
    private val remote: ShotRemoteDataSource,
    private val database: DesignerDatabase,
) : RemoteMediator<Int, ShotEntity>() {

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ShotEntity>,
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val loaded = state.pages.sumOf { it.data.size }
                if (loaded == 0) 1 else (loaded / SHOT_PAGE_SIZE) + 1
            }
        }
        return try {
            val shots = remote.fetchShots(page, SHOT_PAGE_SIZE)
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.shotDao().clearAll()
                }
                if (shots.isNotEmpty()) {
                    val start = (page - 1) * SHOT_PAGE_SIZE
                    database.shotDao().insertAll(
                        shots.mapIndexed { index, shot -> shot.toEntity(start + index) },
                    )
                }
            }
            MediatorResult.Success(endOfPaginationReached = shots.size < SHOT_PAGE_SIZE)
        } catch (error: Exception) {
            MediatorResult.Error(error)
        }
    }
}

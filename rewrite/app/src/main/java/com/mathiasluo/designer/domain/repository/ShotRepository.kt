package com.mathiasluo.designer.domain.repository

import androidx.paging.PagingData
import com.mathiasluo.designer.domain.model.Shot
import kotlinx.coroutines.flow.Flow

interface ShotRepository {
    fun pagedFeed(): Flow<PagingData<Shot>>
    fun observeCached(): Flow<List<Shot>>
    suspend fun getShot(id: String): Shot?
    suspend fun refreshFeed()
    suspend fun search(query: String): List<Shot>
    suspend fun appendIfNeeded(loadedCount: Int)
}

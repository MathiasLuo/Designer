package com.mathiasluo.designer.domain.repository

import androidx.paging.PagingData
import com.mathiasluo.designer.domain.model.Shot
import kotlinx.coroutines.flow.Flow

interface ShotRepository {
    fun pagedFeed(): Flow<PagingData<Shot>>
    suspend fun getShot(id: String): Shot?
    suspend fun refreshFeed()
}

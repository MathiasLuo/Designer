package com.mathiasluo.designer.domain.repository

import com.mathiasluo.designer.domain.model.Shot
import kotlinx.coroutines.flow.Flow

interface ShotRepository {
    fun observeFeed(): Flow<List<Shot>>
    suspend fun getShot(id: String): Shot?
    suspend fun refreshFeed()
}

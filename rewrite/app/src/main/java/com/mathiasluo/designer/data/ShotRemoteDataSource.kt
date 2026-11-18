package com.mathiasluo.designer.data

import com.mathiasluo.designer.domain.model.Shot

interface ShotRemoteDataSource {
    suspend fun fetchShots(page: Int, pageSize: Int): List<Shot>
    suspend fun fetchShot(id: String): Shot?
}

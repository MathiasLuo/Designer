package com.mathiasluo.designer.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ShotDao {
    @Query("SELECT * FROM shots ORDER BY sortIndex ASC")
    fun pagingSource(): PagingSource<Int, ShotEntity>

    @Query("SELECT * FROM shots WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ShotEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(shots: List<ShotEntity>)

    @Query("DELETE FROM shots")
    suspend fun clearAll()

    @Transaction
    suspend fun replaceAll(shots: List<ShotEntity>) {
        clearAll()
        insertAll(shots)
    }
}

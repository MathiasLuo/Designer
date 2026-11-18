package com.mathiasluo.designer.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ShotEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class DesignerDatabase : RoomDatabase() {
    abstract fun shotDao(): ShotDao
}

package com.mathiasluo.designer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mathiasluo.designer.domain.model.Author
import com.mathiasluo.designer.domain.model.Shot

@Entity(tableName = "shots")
data class ShotEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val likesCount: Int,
    val authorId: String,
    val authorName: String,
    val authorAvatarUrl: String,
    val sortIndex: Int,
)

fun ShotEntity.toDomain(): Shot = Shot(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    likesCount = likesCount,
    author = Author(
        id = authorId,
        name = authorName,
        avatarUrl = authorAvatarUrl,
    ),
)

fun Shot.toEntity(sortIndex: Int): ShotEntity = ShotEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    likesCount = likesCount,
    authorId = author.id,
    authorName = author.name,
    authorAvatarUrl = author.avatarUrl,
    sortIndex = sortIndex,
)

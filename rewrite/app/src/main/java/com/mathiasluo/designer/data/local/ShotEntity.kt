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
    val commentsCount: Int,
    val width: Int,
    val height: Int,
    val htmlUrl: String,
    val authorId: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatarUrl: String,
    val authorHtmlUrl: String,
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
        username = authorUsername,
        htmlUrl = authorHtmlUrl,
    ),
    commentsCount = commentsCount,
    width = width,
    height = height,
    htmlUrl = htmlUrl,
)

fun Shot.toEntity(sortIndex: Int): ShotEntity = ShotEntity(
    id = id,
    title = title,
    description = description,
    imageUrl = imageUrl,
    likesCount = likesCount,
    commentsCount = commentsCount,
    width = width,
    height = height,
    htmlUrl = htmlUrl,
    authorId = author.id,
    authorName = author.name,
    authorUsername = author.username,
    authorAvatarUrl = author.avatarUrl,
    authorHtmlUrl = author.htmlUrl,
    sortIndex = sortIndex,
)

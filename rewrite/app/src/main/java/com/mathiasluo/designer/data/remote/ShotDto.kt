package com.mathiasluo.designer.data.remote

import com.mathiasluo.designer.domain.model.Author
import com.mathiasluo.designer.domain.model.Shot
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShotDto(
    val id: String,
    val title: String,
    val description: String? = null,
    @SerialName("image_url") val imageUrl: String,
    @SerialName("likes_count") val likesCount: Int = 0,
    val author: AuthorDto,
)

@Serializable
data class AuthorDto(
    val id: String,
    val name: String,
    @SerialName("avatar_url") val avatarUrl: String,
)

fun ShotDto.toDomain(): Shot = Shot(
    id = id,
    title = title,
    description = description.orEmpty(),
    imageUrl = imageUrl,
    likesCount = likesCount,
    author = Author(
        id = author.id,
        name = author.name,
        avatarUrl = author.avatarUrl,
    ),
)

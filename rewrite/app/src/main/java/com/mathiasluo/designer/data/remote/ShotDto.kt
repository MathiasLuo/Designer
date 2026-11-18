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
    @SerialName("comments_count") val commentsCount: Int = 0,
    val width: Int = 800,
    val height: Int = 600,
    @SerialName("html_url") val htmlUrl: String = "",
    val author: AuthorDto,
)

@Serializable
data class AuthorDto(
    val id: String,
    val name: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val username: String = "",
    @SerialName("html_url") val htmlUrl: String = "",
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
        username = author.username.ifBlank { author.name },
        htmlUrl = author.htmlUrl,
    ),
    commentsCount = commentsCount,
    width = width,
    height = height,
    htmlUrl = htmlUrl,
)

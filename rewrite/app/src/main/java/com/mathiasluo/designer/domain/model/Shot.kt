package com.mathiasluo.designer.domain.model

data class Shot(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val likesCount: Int,
    val author: Author,
    val commentsCount: Int = 0,
    val width: Int = 800,
    val height: Int = 600,
    val htmlUrl: String = "",
)

data class Author(
    val id: String,
    val name: String,
    val avatarUrl: String,
    val username: String = name,
    val htmlUrl: String = "",
)

data class DesignerProfile(
    val username: String,
    val htmlUrl: String,
    val avatarUrl: String,
)

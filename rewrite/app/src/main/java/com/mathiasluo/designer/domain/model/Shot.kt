package com.mathiasluo.designer.domain.model

data class Shot(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val likesCount: Int,
    val author: Author,
)

data class Author(
    val id: String,
    val name: String,
    val avatarUrl: String,
)

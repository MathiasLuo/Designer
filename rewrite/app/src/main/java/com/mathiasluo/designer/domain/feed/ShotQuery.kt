package com.mathiasluo.designer.domain.feed

import com.mathiasluo.designer.domain.model.Shot

const val SHOT_PAGE_SIZE = 10

fun pageShots(shots: List<Shot>, page: Int, pageSize: Int): List<Shot> {
    val safePage = if (page >= 1) page else 1
    val size = when {
        pageSize < 1 -> SHOT_PAGE_SIZE
        pageSize > 50 -> 50
        else -> pageSize
    }
    val start = (safePage - 1) * size
    if (start >= shots.size) return emptyList()
    return shots.subList(start, minOf(start + size, shots.size))
}

fun filterShots(shots: List<Shot>, query: String): List<Shot> {
    val needle = query.trim().lowercase()
    if (needle.isEmpty()) return shots
    return shots.filter { shot ->
        shot.title.lowercase().contains(needle) ||
            shot.author.username.lowercase().contains(needle) ||
            shot.author.name.lowercase().contains(needle)
    }
}

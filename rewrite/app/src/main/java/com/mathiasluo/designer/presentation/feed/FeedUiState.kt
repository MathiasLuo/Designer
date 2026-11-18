package com.mathiasluo.designer.presentation.feed

import com.mathiasluo.designer.domain.model.Shot

data class FeedUiState(
    val shots: List<Shot> = emptyList(),
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)

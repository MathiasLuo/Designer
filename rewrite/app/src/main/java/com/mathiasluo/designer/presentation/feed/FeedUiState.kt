package com.mathiasluo.designer.presentation.feed

data class FeedUiState(
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)

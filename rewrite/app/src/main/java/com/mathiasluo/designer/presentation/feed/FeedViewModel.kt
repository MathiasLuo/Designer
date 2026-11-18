package com.mathiasluo.designer.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathiasluo.designer.domain.usecase.ObserveFeedUseCase
import com.mathiasluo.designer.domain.usecase.RefreshFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    observeFeed: ObserveFeedUseCase,
    private val refreshFeed: RefreshFeedUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeFeed()
                .catch { e -> _uiState.update { it.copy(errorMessage = e.message) } }
                .collect { shots ->
                    _uiState.update { it.copy(shots = shots, errorMessage = null) }
                }
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true, errorMessage = null) }
            runCatching { refreshFeed() }
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
}

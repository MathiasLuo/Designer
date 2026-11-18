package com.mathiasluo.designer.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.usecase.ObservePagedFeedUseCase
import com.mathiasluo.designer.domain.usecase.RefreshFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    observePagedFeed: ObservePagedFeedUseCase,
    private val refreshFeed: RefreshFeedUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedUiState())
    val uiState: StateFlow<FeedUiState> = _uiState.asStateFlow()

    val pagingData: Flow<PagingData<Shot>> = observePagedFeed().cachedIn(viewModelScope)

    init {
        // Seed Room on first open so PagingSource has data immediately.
        onRefresh()
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

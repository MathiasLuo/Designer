package com.mathiasluo.designer.presentation.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.usecase.ObservePagedFeedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    observePagedFeed: ObservePagedFeedUseCase,
) : ViewModel() {
    val pagingData: Flow<PagingData<Shot>> = observePagedFeed().cachedIn(viewModelScope)
}

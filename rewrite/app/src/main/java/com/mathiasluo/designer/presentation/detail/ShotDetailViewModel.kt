package com.mathiasluo.designer.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShotDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: ShotRepository,
) : ViewModel() {
    val shotId: String = checkNotNull(savedStateHandle["shotId"])

    val shots: StateFlow<List<Shot>> = repository.observeCached()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun loadMore(loadedCount: Int) {
        viewModelScope.launch {
            repository.appendIfNeeded(loadedCount)
        }
    }
}

package com.mathiasluo.designer.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.usecase.GetShotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ShotDetailUiState(
    val shot: Shot? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

@HiltViewModel
class ShotDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getShot: GetShotUseCase,
) : ViewModel() {

    private val shotId: String = checkNotNull(savedStateHandle["shotId"])

    private val _uiState = MutableStateFlow(ShotDetailUiState())
    val uiState: StateFlow<ShotDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val shot = getShot(shotId)
            _uiState.value = if (shot == null) {
                ShotDetailUiState(isLoading = false, errorMessage = "Shot not found")
            } else {
                ShotDetailUiState(shot = shot, isLoading = false)
            }
        }
    }
}

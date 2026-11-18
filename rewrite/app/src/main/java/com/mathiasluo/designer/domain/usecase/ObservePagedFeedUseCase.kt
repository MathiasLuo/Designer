package com.mathiasluo.designer.domain.usecase

import androidx.paging.PagingData
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObservePagedFeedUseCase @Inject constructor(
    private val repository: ShotRepository,
) {
    operator fun invoke(): Flow<PagingData<Shot>> = repository.pagedFeed()
}

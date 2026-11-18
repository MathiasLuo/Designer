package com.mathiasluo.designer.domain.usecase

import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFeedUseCase @Inject constructor(
    private val repository: ShotRepository,
) {
    operator fun invoke(): Flow<List<Shot>> = repository.observeFeed()
}

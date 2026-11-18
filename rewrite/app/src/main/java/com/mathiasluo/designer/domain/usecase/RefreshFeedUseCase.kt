package com.mathiasluo.designer.domain.usecase

import com.mathiasluo.designer.domain.repository.ShotRepository
import javax.inject.Inject

class RefreshFeedUseCase @Inject constructor(
    private val repository: ShotRepository,
) {
    suspend operator fun invoke() = repository.refreshFeed()
}

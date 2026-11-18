package com.mathiasluo.designer.domain.usecase

import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import javax.inject.Inject

class GetShotUseCase @Inject constructor(
    private val repository: ShotRepository,
) {
    suspend operator fun invoke(id: String): Shot? = repository.getShot(id)
}

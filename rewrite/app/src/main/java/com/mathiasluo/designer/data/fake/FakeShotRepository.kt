package com.mathiasluo.designer.data.fake

import com.mathiasluo.designer.domain.model.Author
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeShotRepository @Inject constructor() : ShotRepository {

    private val shots = MutableStateFlow(sampleShots())

    override fun observeFeed(): Flow<List<Shot>> = shots.asStateFlow()

    override suspend fun getShot(id: String): Shot? = shots.value.firstOrNull { it.id == id }

    override suspend fun refreshFeed() {
        delay(400)
        shots.value = sampleShots().shuffled()
    }

    private fun sampleShots(): List<Shot> = listOf(
        Shot(
            id = "1",
            title = "Morning Brand System",
            description = "Identity exploration for a café brand — warm neutrals and custom type.",
            imageUrl = "https://picsum.photos/seed/designer1/800/600",
            likesCount = 128,
            author = Author("a1", "Ada Chen", "https://picsum.photos/seed/ada/200"),
        ),
        Shot(
            id = "2",
            title = "Transit App Screens",
            description = "High-contrast wayfinding UI for late-night riders.",
            imageUrl = "https://picsum.photos/seed/designer2/800/600",
            likesCount = 96,
            author = Author("a2", "Noah Park", "https://picsum.photos/seed/noah/200"),
        ),
        Shot(
            id = "3",
            title = "Poster: Open Studio",
            description = "Typographic poster series for a weekend open studio night.",
            imageUrl = "https://picsum.photos/seed/designer3/800/600",
            likesCount = 214,
            author = Author("a3", "Mila Orth", "https://picsum.photos/seed/mila/200"),
        ),
        Shot(
            id = "4",
            title = "Icon Set — Tools",
            description = "24px outline icons for a creative toolkit product.",
            imageUrl = "https://picsum.photos/seed/designer4/800/600",
            likesCount = 77,
            author = Author("a4", "Kai Rivera", "https://picsum.photos/seed/kai/200"),
        ),
    )
}

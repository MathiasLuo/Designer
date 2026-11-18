package com.mathiasluo.designer.data.fake

import com.mathiasluo.designer.data.ShotRemoteDataSource
import com.mathiasluo.designer.domain.model.Author
import com.mathiasluo.designer.domain.model.Shot
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeShotRemoteDataSource @Inject constructor() : ShotRemoteDataSource {

    override suspend fun fetchShots(page: Int, pageSize: Int): List<Shot> {
        delay(350)
        val from = (page - 1) * pageSize
        if (from >= SAMPLE.size) return emptyList()
        return SAMPLE.drop(from).take(pageSize)
    }

    override suspend fun fetchShot(id: String): Shot? {
        delay(150)
        return SAMPLE.firstOrNull { it.id == id }
    }

    companion object {
        val SAMPLE: List<Shot> = (1..18).map { index ->
            val author = AUTHORS[(index - 1) % AUTHORS.size]
            Shot(
                id = index.toString(),
                title = TITLES[(index - 1) % TITLES.size] + " #$index",
                description = "Sample shot $index for Paging + Room demo.",
                imageUrl = "https://picsum.photos/seed/designer$index/800/600",
                likesCount = 40 + index * 7,
                author = author,
            )
        }

        private val AUTHORS = listOf(
            Author("a1", "Ada Chen", "https://picsum.photos/seed/ada/200"),
            Author("a2", "Noah Park", "https://picsum.photos/seed/noah/200"),
            Author("a3", "Mila Orth", "https://picsum.photos/seed/mila/200"),
            Author("a4", "Kai Rivera", "https://picsum.photos/seed/kai/200"),
        )

        private val TITLES = listOf(
            "Morning Brand System",
            "Transit App Screens",
            "Poster: Open Studio",
            "Icon Set — Tools",
            "Editorial Spread",
            "Packaging Study",
        )
    }
}

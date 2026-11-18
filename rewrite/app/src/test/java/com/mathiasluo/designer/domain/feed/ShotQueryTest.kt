package com.mathiasluo.designer.domain.feed

import com.mathiasluo.designer.domain.model.Author
import com.mathiasluo.designer.domain.model.Shot
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ShotQueryTest {
    private val shots = List(24) { index ->
        shot(index.toString(), "Poster $index", "lina.studio".takeIf { index % 2 == 0 } ?: "pixel.jun")
    }

    @Test
    fun firstPageHasTenShots() {
        val page = pageShots(shots, page = 1, pageSize = 10)
        assertEquals(10, page.size)
        assertEquals("0", page.first().id)
    }

    @Test
    fun lastPageIsShort() {
        val page = pageShots(shots, page = 3, pageSize = 10)
        assertEquals(4, page.size)
        assertEquals("20", page.first().id)
    }

    @Test
    fun pagePastTheEndIsEmpty() {
        assertTrue(pageShots(shots, page = 9, pageSize = 10).isEmpty())
    }

    @Test
    fun invalidPageAndSizeFallBack() {
        assertEquals(10, pageShots(shots, page = 0, pageSize = 0).size)
        assertEquals(24, pageShots(shots, page = 1, pageSize = 100).size)
    }

    @Test
    fun filterMatchesTitleOrUsername() {
        val byTitle = filterShots(shots, "Poster 2")
        assertTrue(byTitle.any { it.id == "2" })
        assertTrue(filterShots(shots, "lina.studio").all { it.author.username == "lina.studio" })
        assertTrue(filterShots(shots, "zzzz-not-a-shot").isEmpty())
        assertEquals(shots.size, filterShots(shots, "   ").size)
    }

    private fun shot(id: String, title: String, username: String) = Shot(
        id = id,
        title = title,
        description = "",
        imageUrl = "",
        likesCount = 1,
        author = Author(id = id, name = username, avatarUrl = "", username = username),
    )
}

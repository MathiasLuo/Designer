package com.mathiasluo.designer.data.fake

import com.mathiasluo.designer.data.ShotRemoteDataSource
import com.mathiasluo.designer.domain.feed.pageShots
import com.mathiasluo.designer.domain.model.Author
import com.mathiasluo.designer.domain.model.Shot
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Dribbble API v1 no longer responds. This catalog keeps the same shot fields
 * so the native app can page, refresh, and open a shot without that API.
 */
@Singleton
class FakeShotRemoteDataSource @Inject constructor() : ShotRemoteDataSource {

    override suspend fun fetchShots(page: Int, pageSize: Int): List<Shot> {
        delay(250)
        return pageShots(SAMPLE, page, pageSize)
    }

    override suspend fun fetchShot(id: String): Shot? {
        delay(100)
        return SAMPLE.firstOrNull { it.id == id }
    }

    companion object {
        private val photos = listOf(
            "photo-1561070791-2526d30994b5",
            "photo-1558655146-d09347e92766",
            "photo-1626785774573-4b799315345d",
            "photo-1572044162444-ad60f128bdea",
            "photo-1541462608143-67571c6738dd",
            "photo-1609921212029-bb5a28e60960",
            "photo-1618005182384-a83a8bd57fbe",
            "photo-1545235617-9465d2a55698",
            "photo-1523726491678-bf852e717f6a",
            "photo-1498050108023-c5249f4df085",
            "photo-1507238691740-187a5b1d37b8",
            "photo-1460925895917-afdab827c52f",
            "photo-1512295767273-ac109ac3acfa",
            "photo-1586717791821-3f44a563fa4c",
            "photo-1576153192396-180ecef2a715",
            "photo-1618005198919-d3d4b5a92ead",
            "photo-1559028012-481c04fa702d",
            "photo-1542744094-24638eff58bb",
            "photo-1499951360447-b19be8fe80f5",
            "photo-1600132806370-bf17e65e942f",
            "photo-1634942537034-2531766767d1",
            "photo-1561070791-36c11767b26a",
            "photo-1613909207039-6b173b755cc1",
            "photo-1558655146-9f40138edfeb",
        )

        private val avatars = listOf(
            "photo-1494790108377-be9c29b29330",
            "photo-1500648767791-00dcc994a43e",
            "photo-1438761681033-6461ffad8d80",
            "photo-1507003211169-0a1dd7228f2d",
            "photo-1544005313-94ddf0286df2",
            "photo-1534528741775-53994a69daeb",
            "photo-1527980965255-d3b416303d12",
            "photo-1547425260-76bcadfb4f2c",
        )

        private val people = listOf(
            "Lina Chen" to "lina.studio",
            "Jun Park" to "pixel.jun",
            "Mara Ortiz" to "atelier.m",
            "Noah Berg" to "northwind",
            "Hana Ito" to "hoja",
            "Eli Grant" to "form.lab",
            "Solo Liu" to "solo.liu",
            "Vera Santos" to "press.room",
        )

        private val titles = listOf(
            "Morning Brand Kit",
            "Blue Ticket App",
            "Poster Study 04",
            "Dashboard Cards",
            "Cafe Identity",
            "Gradient Icons",
            "Abstract Cover",
            "Editorial Layout",
            "Workspace Notes",
            "Portfolio Hero",
            "Analytics Mobile",
            "Shape Poster",
            "Color Blocks",
            "Landing Wire",
            "Illustration Set",
            "Soft 3D Study",
            "Campaign Colors",
            "Team Workshop",
            "Night Desk",
            "App Onboarding",
            "UI Kit Preview",
            "Mark Sketch",
            "Festival Poster",
            "Packaging Flat",
        )

        private fun image(id: String, width: Int) =
            "https://images.unsplash.com/$id?auto=format&fit=crop&w=$width&q=80"

        val SAMPLE: List<Shot> = photos.mapIndexed { index, photo ->
            val (name, username) = people[index % people.size]
            val tall = index % 5 == 0
            val id = (48021001 + index).toString()
            Shot(
                id = id,
                title = titles[index],
                description = "${titles[index]} by $name.",
                imageUrl = image(photo, 800),
                likesCount = 80 + ((index * 37) % 900),
                author = Author(
                    id = "a${index % people.size}",
                    name = name,
                    avatarUrl = image(avatars[index % avatars.size], 200),
                    username = username,
                    htmlUrl = "https://dribbble.com/$username",
                ),
                commentsCount = 4 + ((index * 13) % 120),
                width = 800,
                height = if (tall) 1000 else 600,
                htmlUrl = "https://dribbble.com/shots/$id",
            )
        }
    }
}

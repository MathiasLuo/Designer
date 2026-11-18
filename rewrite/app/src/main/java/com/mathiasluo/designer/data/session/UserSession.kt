package com.mathiasluo.designer.data.session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.mathiasluo.designer.domain.model.DesignerProfile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.net.URLEncoder
import javax.inject.Inject
import javax.inject.Singleton

private val Context.sessionStore by preferencesDataStore(name = "designer_session")

@Singleton
class UserSession @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val store = context.sessionStore
    private val usernameKey = stringPreferencesKey("username")
    private val likedIds = MutableStateFlow<Set<String>>(emptySet())

    val profile: Flow<DesignerProfile?> = store.data.map { prefs ->
        prefs[usernameKey]?.let(::profileFor)
    }

    val liked: Flow<Set<String>> = likedIds

    suspend fun signIn(username: String) {
        val clean = username.trim().take(32)
        if (clean.isEmpty()) return
        store.edit { prefs -> prefs[usernameKey] = clean }
    }

    fun toggleLike(id: String) {
        likedIds.update { current ->
            if (id in current) current - id else current + id
        }
    }

    fun isLiked(id: String): Boolean = id in likedIds.value
}

fun profileFor(username: String): DesignerProfile {
    val encoded = URLEncoder.encode(username, Charsets.UTF_8.name())
    return DesignerProfile(
        username = username,
        htmlUrl = "https://dribbble.com/$encoded",
        avatarUrl = "https://api.dicebear.com/9.x/adventurer/png?seed=$encoded",
    )
}

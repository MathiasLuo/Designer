package com.mathiasluo.designer.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mathiasluo.designer.R
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.presentation.common.shareText
import com.mathiasluo.designer.presentation.theme.BrandPink

private val ViewerBackground = Color(0xFF111111)
private val ViewerText = Color(0xFFFDF6F6)

@Composable
fun ShotDetailScreen(
    onBack: () -> Unit,
    liked: Set<String>,
    onToggleLike: (String) -> Unit,
    viewModel: ShotDetailViewModel = hiltViewModel(),
) {
    val shots by viewModel.shots.collectAsStateWithLifecycle()
    if (shots.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ViewerBackground),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(color = Color.White)
        }
        return
    }

    val start = shots.indexOfFirst { it.id == viewModel.shotId }.coerceAtLeast(0)
    ShotPager(
        shots = shots,
        start = start,
        liked = liked,
        onBack = onBack,
        onToggleLike = onToggleLike,
        onNeedMore = { viewModel.loadMore(shots.size) },
    )
}

@Composable
private fun ShotPager(
    shots: List<Shot>,
    start: Int,
    liked: Set<String>,
    onBack: () -> Unit,
    onToggleLike: (String) -> Unit,
    onNeedMore: () -> Unit,
) {
    val pagerState = rememberPagerState(initialPage = start) { shots.size }
    LaunchedEffect(pagerState.currentPage, shots.size) {
        if (shots.isNotEmpty() && pagerState.currentPage >= shots.lastIndex) {
            onNeedMore()
        }
    }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(ViewerBackground),
    ) { page ->
        ViewerPage(
            shot = shots[page],
            liked = shots[page].id in liked,
            onBack = onBack,
            onToggleLike = { onToggleLike(shots[page].id) },
        )
    }
}

@Composable
private fun ViewerPage(
    shot: Shot,
    liked: Boolean,
    onBack: () -> Unit,
    onToggleLike: () -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ViewerBackground)
            .padding(bottom = 12.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.action_back), tint = ViewerText)
            }
            Text(
                text = shot.title,
                color = ViewerText,
                fontSize = 16.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            IconButton(onClick = { context.shareText(shot.htmlUrl.ifBlank { shot.title }) }) {
                Icon(Icons.Filled.Share, contentDescription = stringResource(R.string.action_share), tint = ViewerText)
            }
        }
        AsyncImage(
            model = shot.imageUrl,
            contentDescription = shot.title,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        )
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = shot.author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = shot.author.name,
                    color = ViewerText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.ChatBubbleOutline, contentDescription = null, tint = ViewerText, modifier = Modifier.size(18.dp))
                    Text(
                        text = shot.commentsCount.toString(),
                        color = ViewerText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(start = 6.dp, end = 4.dp),
                    )
                    IconButton(onClick = onToggleLike) {
                        Icon(
                            imageVector = if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = stringResource(if (liked) R.string.action_unlike else R.string.action_like),
                            tint = BrandPink,
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = ViewerText, modifier = Modifier.size(18.dp))
                Text(
                    text = stringResource(R.string.detail_likes_sentence, shot.likesCount),
                    color = ViewerText,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 8.dp),
                )
            }
        }
    }
}

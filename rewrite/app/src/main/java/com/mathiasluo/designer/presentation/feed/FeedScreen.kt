package com.mathiasluo.designer.presentation.feed

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.mathiasluo.designer.domain.model.Shot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    onShotClick: (String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("设计狮") })
        },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                viewModel.onRefresh()
                pagingItems.refresh()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            FeedList(
                pagingItems = pagingItems,
                errorMessage = state.errorMessage,
                onShotClick = onShotClick,
            )
        }
    }
}

@Composable
private fun FeedList(
    pagingItems: LazyPagingItems<Shot>,
    errorMessage: String?,
    onShotClick: (String) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        if (errorMessage != null) {
            item(key = "error") {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }

        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id },
        ) { index ->
            val shot = pagingItems[index] ?: return@items
            ShotFeedItem(shot = shot, onClick = { onShotClick(shot.id) })
        }

        when (val append = pagingItems.loadState.append) {
            is LoadState.Loading -> {
                item(key = "append-loading") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
            is LoadState.Error -> {
                item(key = "append-error") {
                    Text(
                        text = append.error.message ?: "Failed to load more",
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }
            else -> Unit
        }
    }
}

@Composable
private fun ShotFeedItem(
    shot: Shot,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        AsyncImage(
            model = shot.imageUrl,
            contentDescription = shot.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(4f / 3f),
        )
        Spacer(Modifier.height(10.dp))
        Text(shot.title, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = shot.author.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape),
            )
            Text(
                text = "${shot.author.name} · ${shot.likesCount} likes",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            )
        }
    }
}

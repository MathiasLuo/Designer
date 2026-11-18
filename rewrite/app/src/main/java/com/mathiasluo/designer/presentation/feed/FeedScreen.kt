package com.mathiasluo.designer.presentation.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.mathiasluo.designer.R
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.presentation.common.ShotCard
import com.mathiasluo.designer.presentation.common.toast
import com.mathiasluo.designer.presentation.theme.BrandBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedScreen(
    liked: Set<String>,
    onOpenShot: (String) -> Unit,
    onToggleLike: (String) -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
) {
    val pagingItems = viewModel.pagingData.collectAsLazyPagingItems()
    val context = LocalContext.current
    var userPulled by remember { mutableStateOf(false) }
    var previousFirstId by remember { mutableStateOf<String?>(null) }
    val latestMessage = stringResource(R.string.latest_data)
    val errorMessage = stringResource(R.string.network_error)
    val refreshState = pagingItems.loadState.refresh

    LaunchedEffect(refreshState, pagingItems.itemCount) {
        when (refreshState) {
            is LoadState.NotLoading -> {
                val firstId = if (pagingItems.itemCount > 0) pagingItems.peek(0)?.id else null
                if (userPulled && firstId != null && firstId == previousFirstId) {
                    context.toast(latestMessage)
                }
                if (firstId != null) previousFirstId = firstId
                userPulled = false
            }
            is LoadState.Error -> {
                context.toast(errorMessage)
                userPulled = false
            }
            is LoadState.Loading -> Unit
        }
    }

    PullToRefreshBox(
        isRefreshing = refreshState is LoadState.Loading,
        onRefresh = {
            userPulled = true
            pagingItems.refresh()
        },
        modifier = Modifier.fillMaxSize(),
    ) {
        FeedList(
            pagingItems = pagingItems,
            liked = liked,
            onOpenShot = onOpenShot,
            onToggleLike = onToggleLike,
            errorMessage = (pagingItems.loadState.append as? LoadState.Error)?.error?.let { errorMessage },
        )
    }
}

@Composable
private fun FeedList(
    pagingItems: LazyPagingItems<Shot>,
    liked: Set<String>,
    onOpenShot: (String) -> Unit,
    onToggleLike: (String) -> Unit,
    errorMessage: String?,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp),
    ) {
        if (pagingItems.itemCount == 0 && pagingItems.loadState.refresh is LoadState.Error) {
            item(key = "error") {
                Text(
                    text = stringResource(R.string.network_error),
                    modifier = Modifier.padding(24.dp),
                    color = BrandBlue,
                )
            }
        }
        items(
            count = pagingItems.itemCount,
            key = pagingItems.itemKey { it.id },
        ) { index ->
            val shot = pagingItems[index] ?: return@items
            ShotCard(
                shot = shot,
                liked = shot.id in liked,
                onOpen = { onOpenShot(shot.id) },
                onToggleLike = { onToggleLike(shot.id) },
            )
        }
        if (pagingItems.loadState.append is LoadState.Loading) {
            item(key = "append-loading") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(color = BrandBlue)
                }
            }
        }
        if (errorMessage != null) {
            item(key = "append-error") {
                Text(errorMessage, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

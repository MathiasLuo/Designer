package com.mathiasluo.designer.presentation.shell

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import com.mathiasluo.designer.BuildConfig
import com.mathiasluo.designer.R
import com.mathiasluo.designer.data.session.UserSession
import com.mathiasluo.designer.domain.model.DesignerProfile
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.domain.repository.ShotRepository
import com.mathiasluo.designer.presentation.common.ShotCard
import com.mathiasluo.designer.presentation.common.shareText
import com.mathiasluo.designer.presentation.feed.FeedScreen
import com.mathiasluo.designer.presentation.theme.BrandBlue
import com.mathiasluo.designer.presentation.theme.PageBackground
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

private enum class Destination {
    Feed,
    Search,
    Likes,
    Settings,
    Person,
}

@HiltViewModel
class SessionViewModel @Inject constructor(
    val session: UserSession,
) : ViewModel()

@OptIn(FlowPreview::class, kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: ShotRepository,
) : ViewModel() {
    private val query = MutableStateFlow("")
    val results = query
        .debounce(150)
        .flatMapLatest { value ->
            flow { emit(repository.search(value)) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    fun onQueryChange(value: String) {
        query.value = value
    }
}

@HiltViewModel
class LikesViewModel @Inject constructor(
    session: UserSession,
    repository: ShotRepository,
) : ViewModel() {
    val shots = kotlinx.coroutines.flow.combine(session.liked, repository.observeCached()) { ids, shots ->
        shots.filter { it.id in ids }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    openDrawerOnStart: Boolean,
    onDrawerOpened: () -> Unit,
    onOpenShot: (String) -> Unit,
    onLogin: () -> Unit,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    searchViewModel: SearchViewModel = hiltViewModel(),
    likesViewModel: LikesViewModel = hiltViewModel(),
) {
    val profile by sessionViewModel.session.profile.collectAsState(initial = null)
    val liked by sessionViewModel.session.liked.collectAsState(initial = emptySet())
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destination by remember { mutableStateOf(Destination.Feed) }
    var menuOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(openDrawerOnStart) {
        if (openDrawerOnStart) {
            drawerState.open()
            onDrawerOpened()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                profile = profile,
                destination = destination,
                onDestination = { next ->
                    destination = next
                    scope.launch { drawerState.close() }
                },
                onAvatar = {
                    scope.launch { drawerState.close() }
                    if (profile == null) onLogin() else destination = Destination.Person
                },
                onShare = {
                    scope.launch { drawerState.close() }
                    context.shareText(context.getString(R.string.app_name))
                },
            )
        },
    ) {
        Scaffold(
            containerColor = PageBackground,
            topBar = {
                val title = when (destination) {
                    Destination.Feed -> stringResource(R.string.app_name)
                    Destination.Search -> stringResource(R.string.nav_search)
                    Destination.Likes -> stringResource(R.string.nav_likes)
                    Destination.Settings -> stringResource(R.string.nav_settings)
                    Destination.Person -> profile?.username ?: stringResource(R.string.profile)
                }
                CenterAlignedTopAppBar(
                    title = { Text(title, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.open_drawer))
                        }
                    },
                    actions = {
                        IconButton(onClick = { destination = Destination.Search }) {
                            Icon(Icons.Filled.Search, contentDescription = stringResource(R.string.nav_search))
                        }
                        IconButton(onClick = { menuOpen = true }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = stringResource(R.string.action_more))
                        }
                        DropdownMenu(expanded = menuOpen, onDismissRequest = { menuOpen = false }) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.nav_settings)) },
                                onClick = {
                                    menuOpen = false
                                    destination = Destination.Settings
                                },
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = BrandBlue,
                        titleContentColor = Color.White,
                        navigationIconContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                )
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                when (destination) {
                    Destination.Feed -> FeedScreen(
                        liked = liked,
                        onOpenShot = onOpenShot,
                        onToggleLike = sessionViewModel.session::toggleLike,
                    )
                    Destination.Search -> SearchPage(
                        viewModel = searchViewModel,
                        liked = liked,
                        onOpenShot = onOpenShot,
                        onToggleLike = sessionViewModel.session::toggleLike,
                    )
                    Destination.Likes -> LikesPage(
                        viewModel = likesViewModel,
                        liked = liked,
                        onOpenShot = onOpenShot,
                        onToggleLike = sessionViewModel.session::toggleLike,
                    )
                    Destination.Settings -> SettingsPage()
                    Destination.Person -> PersonPage(profile)
                }
            }
        }
    }
}

@Composable
private fun AppDrawer(
    profile: DesignerProfile?,
    destination: Destination,
    onDestination: (Destination) -> Unit,
    onAvatar: () -> Unit,
    onShare: () -> Unit,
) {
    ModalDrawerSheet(modifier = Modifier.width(300.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(Color(0xFF9597F7), Color(0xFF4043E2), Color(0xFF2C30ED)),
                    ),
                )
                .padding(start = 16.dp, end = 16.dp, top = 28.dp, bottom = 16.dp),
        ) {
            IconButton(onClick = onAvatar, modifier = Modifier.size(66.dp)) {
                if (profile == null) {
                    Icon(
                        Icons.Filled.Person,
                        contentDescription = stringResource(R.string.login_hint),
                        tint = Color.White,
                        modifier = Modifier
                            .size(66.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.25f))
                            .padding(14.dp),
                    )
                } else {
                    AsyncImage(
                        model = profile.avatarUrl,
                        contentDescription = stringResource(R.string.profile),
                        modifier = Modifier
                            .size(66.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = profile?.username ?: stringResource(R.string.login_hint),
                color = Color.White,
                fontSize = 16.sp,
            )
            Text(
                text = profile?.htmlUrl ?: stringResource(R.string.login_subtitle),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.app_name)) },
            selected = destination == Destination.Feed,
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            onClick = { onDestination(Destination.Feed) },
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.nav_search)) },
            selected = destination == Destination.Search,
            icon = { Icon(Icons.Filled.Search, contentDescription = null) },
            onClick = { onDestination(Destination.Search) },
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.nav_likes)) },
            selected = destination == Destination.Likes,
            icon = { Icon(Icons.Filled.Bookmark, contentDescription = null) },
            onClick = { onDestination(Destination.Likes) },
        )
        Text(
            text = stringResource(R.string.nav_other),
            color = Color(0xFF888888),
            fontSize = 12.sp,
            modifier = Modifier.padding(start = 28.dp, top = 12.dp, bottom = 4.dp),
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.nav_share)) },
            selected = false,
            icon = { Icon(Icons.Filled.Share, contentDescription = null) },
            onClick = onShare,
        )
        NavigationDrawerItem(
            label = { Text(stringResource(R.string.nav_settings)) },
            selected = destination == Destination.Settings,
            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
            onClick = { onDestination(Destination.Settings) },
        )
    }
}

@Composable
private fun SearchPage(
    viewModel: SearchViewModel,
    liked: Set<String>,
    onOpenShot: (String) -> Unit,
    onToggleLike: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    val results by viewModel.results.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                viewModel.onQueryChange(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            singleLine = true,
            label = { Text(stringResource(R.string.nav_search)) },
        )
        if (text.isNotBlank() && results.isEmpty()) {
            Text(
                text = stringResource(R.string.empty_search),
                modifier = Modifier.padding(24.dp),
                color = Color(0xFF888888),
            )
        }
        LazyColumn(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
            items(results.size) { index ->
                val shot = results[index]
                ShotCard(
                    shot = shot,
                    liked = shot.id in liked,
                    onOpen = { onOpenShot(shot.id) },
                    onToggleLike = { onToggleLike(shot.id) },
                )
            }
        }
    }
}

@Composable
private fun LikesPage(
    viewModel: LikesViewModel,
    liked: Set<String>,
    onOpenShot: (String) -> Unit,
    onToggleLike: (String) -> Unit,
) {
    val shots by viewModel.shots.collectAsStateWithLifecycle()
    if (shots.isEmpty()) {
        Text(
            text = stringResource(R.string.empty_likes),
            modifier = Modifier.padding(24.dp),
            color = Color(0xFF888888),
        )
        return
    }
    LazyColumn(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
        items(shots.size) { index ->
            val shot = shots[index]
            ShotCard(
                shot = shot,
                liked = shot.id in liked,
                onOpen = { onOpenShot(shot.id) },
                onToggleLike = { onToggleLike(shot.id) },
            )
        }
    }
}

@Composable
private fun SettingsPage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(top = 8.dp),
    ) {
        SettingsRow(stringResource(R.string.settings_app), stringResource(R.string.app_name))
        SettingsRow(stringResource(R.string.settings_version), BuildConfig.VERSION_NAME)
    }
}

@Composable
private fun SettingsRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, modifier = Modifier.weight(1f))
        Text(value)
    }
    HorizontalDivider()
}

@Composable
private fun PersonPage(profile: DesignerProfile?) {
    if (profile == null) {
        Text(
            text = stringResource(R.string.login_hint),
            modifier = Modifier.padding(24.dp),
        )
        return
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)
            .padding(top = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = profile.avatarUrl,
            contentDescription = null,
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
        )
        Spacer(Modifier.height(12.dp))
        Text(profile.username, fontSize = 16.sp)
        Text(profile.htmlUrl, color = Color(0xFF777777), fontSize = 14.sp, modifier = Modifier.padding(top = 6.dp, start = 16.dp, end = 16.dp))
    }
}

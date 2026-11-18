package com.mathiasluo.designer.presentation.common

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mathiasluo.designer.R
import com.mathiasluo.designer.domain.model.Shot
import com.mathiasluo.designer.presentation.theme.BrandPink
import com.mathiasluo.designer.presentation.theme.DividerColor
import com.mathiasluo.designer.presentation.theme.IconGray
import com.mathiasluo.designer.presentation.theme.LinkBlue

@Composable
fun ShotCard(
    shot: Shot,
    liked: Boolean,
    onOpen: () -> Unit,
    onToggleLike: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = androidx.compose.ui.graphics.Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column {
            AsyncImage(
                model = shot.imageUrl,
                contentDescription = shot.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(shot.width / shot.height.toFloat())
                    .background(androidx.compose.ui.graphics.Color(0xFFF7F7F7))
                    .clickable(onClick = onOpen),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 4.dp, top = 6.dp, bottom = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AsyncImage(
                    model = shot.author.avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                )
                Text(
                    text = shot.author.username,
                    color = LinkBlue,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f),
                )
                IconButton(onClick = onToggleLike) {
                    Icon(
                        imageVector = if (liked) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = stringResource(if (liked) R.string.action_unlike else R.string.action_like),
                        tint = BrandPink,
                    )
                }
            }
            HorizontalDivider(color = DividerColor)
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Filled.Favorite, contentDescription = null, tint = IconGray, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Text(stringResource(R.string.likes_sentence, shot.likesCount), fontSize = 16.sp)
            }
            HorizontalDivider(color = DividerColor)
            Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    Icons.Filled.ChatBubbleOutline,
                    contentDescription = null,
                    tint = IconGray,
                    modifier = Modifier.size(20.dp),
                )
                Spacer(Modifier.width(10.dp))
                Text(shot.commentsCount.toString(), fontSize = 16.sp)
            }
            HorizontalDivider(color = DividerColor)
            Row(modifier = Modifier.fillMaxWidth()) {
                TextButton(
                    onClick = onOpen,
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Filled.Info, contentDescription = null, tint = IconGray, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.action_detail), color = androidx.compose.ui.graphics.Color(0xFF333333), fontSize = 16.sp)
                }
                TextButton(
                    onClick = { context.shareText(shot.htmlUrl.ifBlank { shot.title }) },
                    modifier = Modifier.weight(1f),
                ) {
                    Icon(Icons.Filled.Share, contentDescription = null, tint = IconGray, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(12.dp))
                    Text(stringResource(R.string.action_share), color = androidx.compose.ui.graphics.Color(0xFF333333), fontSize = 16.sp)
                }
            }
        }
    }
}

fun Context.shareText(text: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    val chooser = Intent.createChooser(intent, getString(R.string.action_share))
    runCatching { startActivity(chooser) }
        .onFailure { Toast.makeText(this, text, Toast.LENGTH_SHORT).show() }
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

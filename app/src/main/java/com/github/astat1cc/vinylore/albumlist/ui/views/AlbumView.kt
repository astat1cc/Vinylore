package com.github.astat1cc.vinylore.albumlist.ui.views

import android.net.Uri
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.github.astat1cc.vinylore.R
import com.github.astat1cc.vinylore.core.models.ui.ListingAlbumUi
import com.github.astat1cc.vinylore.player.ui.views.dpToSp

@Composable
fun AlbumView(
    album: ListingAlbumUi,
    onClick: (Uri) -> Unit,
    clickedAlbumUri: Uri?,
    screenWidth: Int
) {
//    MaterialTheme {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(album.uri)
            }
            .padding(vertical = 20.dp, horizontal = 36.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(width = 160.dp, height = 100.dp)
                .zIndex(1f)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = clickedAlbumUri != album.uri,
                exit = slideOutHorizontally(
                    targetOffsetX = { screenWidth },
                    animationSpec = tween(
                        durationMillis = 250,
                        easing = LinearEasing,
//                        delayMillis = 20
                    )
                )
            ) {
                Box(
                    contentAlignment = Alignment.CenterStart,
                ) {
//                        // second vinyl under mockup
//                        Image(
//                            painter = painterResource(R.drawable.vinyl_under_mockup),
//                            contentDescription = "",
//                            modifier = Modifier
//                                .padding(start = 40.dp)
//                                .height(100.dp),
//                            contentScale = ContentScale.FillHeight,
//                        )
//                        // first vinyl under mockup
//                        Image(
//                            painter = painterResource(R.drawable.vinyl_under_mockup),
//                            contentDescription = "",
//                            modifier = Modifier
//                                .padding(start = 32.dp)
//                                .height(100.dp),
//                            contentScale = ContentScale.FillHeight,
//                        )
//                        // mockup
//                        Image(
//                            painter = painterResource(R.drawable.album_mockup),
//                            contentDescription = "",
//                            modifier = Modifier.width(160.dp),
//                            contentScale = ContentScale.FillWidth,
//                        )
                    Image(
                        painter = painterResource(R.drawable.album_in_list),
                        contentDescription = null,
                        modifier = Modifier.width(160.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                    Text(
                        text = "${album.trackCount} track${if (album.trackCount > 1) "s" else ""}",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(end = 48.dp, start = 4.dp),
                        textAlign = TextAlign.Center,
//                            fontWeight = FontWeight.Bold,
                        fontSize = dpToSp(dp = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
//            Image(
//                painter = painterResource(R.drawable.album),
//                contentDescription = "",
//                modifier = Modifier
//                    .width(180.dp)
//                    .clipToBounds(),
//                contentScale = ContentScale.FillWidth,
//            )
            }
        }
        Text(
            text = album.name ?: stringResource(id = R.string.unknown_name),
            maxLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp),
            textAlign = TextAlign.Center,
            color = Color.White,
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            overflow = TextOverflow.Ellipsis
        )
    }
}
//}
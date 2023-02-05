package com.github.astat1cc.vinylore.albumlist.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.astat1cc.vinylore.R
import com.github.astat1cc.vinylore.core.models.ui.AlbumUi

@Composable
fun AlbumView(
    album: AlbumUi,
    onClick: (Int) -> Unit
) {
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    onClick(album.id)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(R.drawable.album),
                contentDescription = "",
                modifier = Modifier
                    .width(180.dp)
                    .clipToBounds(),
                contentScale = ContentScale.FillWidth,
            )
            Text(
                text = album.name ?: stringResource(id = R.string.unknown_name),
                minLines = 2,
                maxLines = 2,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .width(100.dp),
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }
    }
}
package com.github.astat1cc.vinylore.player.ui.vinyl.compose

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.astat1cc.vinylore.player.ui.vinyl.PlayerState

@Composable
fun VinylAnimation(
    modifier: Modifier = Modifier,
    playerState: PlayerState = PlayerState.STOPPED,
//    playerStartedListener: () -> Unit,
//    playerStoppedListener: () -> Unit,
    playerStateChangedListener: (PlayerState) -> Unit
) {
    // Allow resume on rotation
    var currentRotation by remember { mutableStateOf(0f) }

    val rotation = remember { Animatable(currentRotation) }

    LaunchedEffect(playerState) {
        when (playerState) {
            PlayerState.STARTED -> {
                rotation.animateTo(
                    targetValue = currentRotation + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(3000, easing = LinearEasing),
                        repeatMode = RepeatMode.Restart
                    )
                ) {
                    currentRotation = value
                }
            }
            PlayerState.STOPPING -> {
                if (currentRotation > 0f) {
                    rotation.animateTo(
                        targetValue = currentRotation + 50,
                        animationSpec = tween(
                            durationMillis = 1250,
                            easing = LinearOutSlowInEasing
                        )
                    ) {
                        if (value == targetValue) playerStateChangedListener(playerState)
//                            playerStoppedListener()
                        currentRotation = value
                    }
                }
            }
            PlayerState.STARTING -> {
                rotation.animateTo(
                    targetValue = currentRotation + 100,
                    animationSpec = tween(
//                        durationMillis = 1250,
                        durationMillis = 1250,
                        easing = FastOutLinearInEasing
                    )
                ) {
                    if (value == targetValue) playerStateChangedListener(playerState)
//                        playerStartedListener()
                    currentRotation = value
                }
            }
            PlayerState.STOPPED -> {}
        }
    }
    Vinyl(modifier = modifier.padding(24.dp), rotationDegrees = rotation.value)
}
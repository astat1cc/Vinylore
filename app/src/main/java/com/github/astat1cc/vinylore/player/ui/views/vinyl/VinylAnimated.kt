package com.github.astat1cc.vinylore.player.ui.views.vinyl

import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.github.astat1cc.vinylore.core.AppConst

@Composable
fun VinylAnimated(
    modifier: Modifier = Modifier,
    discState: VinylDiscState = VinylDiscState.STOPPED,
    playerStateTransitionFrom: (VinylDiscState) -> Unit,
    currentRotation: Float = 0f,
    changeRotation: (Float) -> Unit,
    albumCover: Bitmap?
) {
//    var currentRotation by remember { mutableStateOf(0f) }

    val rotationAnimatable = remember { Animatable(currentRotation) }

    LaunchedEffect(discState) {
        when (discState) {
            VinylDiscState.STARTED -> {
                rotationAnimatable.animateTo(
                    targetValue = currentRotation + 360f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(
                            durationMillis = 1333,
                            easing = LinearEasing,
                            delayMillis = 0
                        ),
                        repeatMode = RepeatMode.Restart
                    )
                ) {
                    changeRotation(value)
                }
            }
            VinylDiscState.STOPPING -> {
                rotationAnimatable.animateTo(
                    targetValue = currentRotation + 120f,
                    animationSpec = tween(durationMillis = 1333, easing = LinearOutSlowInEasing)
                ) {
                    changeRotation(value)
                    if (value == targetValue) playerStateTransitionFrom(discState)
                }
            }
            VinylDiscState.STARTING -> {
                rotationAnimatable.animateTo(
                    targetValue = currentRotation + 180f,
                    animationSpec = tween(
                        durationMillis = AppConst.VINYL_STARTING_ANIMATION_DURATION,
                        easing = FastOutLinearInEasing
                    )
                ) {
                    changeRotation(value)
                    if (value > targetValue - 15f) playerStateTransitionFrom(discState)
                }
            }
            VinylDiscState.STOPPED -> {
                if (currentRotation == 0f) rotationAnimatable.snapTo(0f)
            }
        }
    }
    VinylView(modifier = modifier, rotationDegrees = currentRotation, albumCover)
}

// 3000 and 1250
package org.slade.chase.ui

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate

enum class RotationDirection {
    Clockwise,
    CounterClockwise
}

@Composable
fun RotatingSurface(
    modifier: Modifier = Modifier,
    initialRotation: Float = 0f,
    sweepRotation: Float = 0f,
    rotate: Boolean = false,
    direction: RotationDirection = RotationDirection.Clockwise,
    animationSpec: AnimationSpec<Float> = tween(
        durationMillis = 300,
        easing = LinearOutSlowInEasing
    ),
    content: @Composable BoxScope.() -> Unit
) {

    val rotateBy by animateFloatAsState(
        targetValue = if(rotate) initialRotation + sweepRotation else initialRotation,
        animationSpec = animationSpec
    )

    Box(
        modifier = Modifier
            .rotate(
                when(direction) {
                    RotationDirection.CounterClockwise -> -rotateBy
                    else -> rotateBy
                }
            )
            .then(modifier)
    ) {
        content()
    }
}
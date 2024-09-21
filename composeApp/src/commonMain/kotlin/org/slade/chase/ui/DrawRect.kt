package org.slade.chase.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp

@Composable
fun DrawRect(

) {

    var rotate by remember { mutableStateOf(false) }

    val rotationState by animateFloatAsState(
        targetValue = if(rotate) 45f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Button(
        onClick = {
            rotate = !rotate
        }
    ) {
        Text(text = "Toggle Rotation")
    }

    Canvas(
        modifier = Modifier
            .size(300.dp)
            .border(5.dp, Color.Black, RoundedCornerShape(10.dp))
    ) {
        rotate(rotationState) {
            drawRect(
                color = Color(34, 56, 78),
                topLeft = Offset(75.dp.toPx(), 75.dp.toPx()),
                size = size / 2f
            )
        }
    }
}
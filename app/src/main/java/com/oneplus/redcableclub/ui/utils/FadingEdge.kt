package com.oneplus.redcableclub.ui.utils

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class FadingEdgeSide {
    Start, End
}

/**
 * Applies a gradient on the edges of a component with an in-and-out animation.
 *
 * @param side The side on which to apply the gradient (Start or End).
 * @param width The maximum width of the gradient when visible.
 * @param isVisible A boolean that determines whether the gradient should be visible, activating the animation.
 */
fun Modifier.fadingEdge(
    side: FadingEdgeSide,
    width: Dp = 24.dp,
    isVisible: Boolean
): Modifier = composed {
    val animatedWidth by animateDpAsState(
        targetValue = if(isVisible) width else 0.dp,
        animationSpec = tween(durationMillis = 200),
        label = "fadingEdgeAWidth"
    )

    if(animatedWidth == 0.dp)
        return@composed this

    this
        .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
        .drawWithContent {
            drawContent()

            val fadeWidthPx = animatedWidth.toPx()

            val brush = when(side) {
                FadingEdgeSide.Start -> Brush.horizontalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startX = 0f,
                    endX= fadeWidthPx
                )
                FadingEdgeSide.End -> Brush.horizontalGradient(
                    colors = listOf(Color.Black, Color.Transparent),
                    startX = size.width - fadeWidthPx,
                    endX = size.width
                )
            }

            drawRect(
                brush = brush,
                blendMode = BlendMode.DstIn
            )
        }

}

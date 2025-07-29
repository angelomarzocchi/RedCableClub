package com.oneplus.redcableclub.ui.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RotatingBackgroundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    shape: Shape,
    backgroundColor: Color,
    initialRotation: Float = 0f,
    targetRotationOnClick: Float = 360f,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 300),
) {
    var rotationAngle by remember { mutableFloatStateOf(initialRotation) }
    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = animationSpec,
        label = "customButtonRotation"
    )
    val interactionSource = remember { MutableInteractionSource() }


    FilledIconButton(
        onClick = {
            // Determine the next rotation angle
            // This allows for continuous rotation or toggling back
            rotationAngle += targetRotationOnClick // Or use your previous toggle logic if preferred
            // Example toggle:
            // rotationAngle = if (rotationAngle == targetRotationOnClick) initialRotation else targetRotationOnClick
            onClick()
        },
        modifier = modifier
            .graphicsLayer {
                rotationZ = animatedRotationAngle
            }
            .clip(shape)
            .background(backgroundColor)
            ,
    ) { icon() }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun RotatingBackgroundButtonPreview() {
    RotatingBackgroundButton(
        onClick = { },
        modifier = Modifier.size(56.dp),
        icon = {
            Icon(
                imageVector = Icons.Filled.Refresh,
                contentDescription = "Refresh Icon",
                tint = Color.White
            )
        },
        shape = MaterialShapes.Sunny.toShape(),
        backgroundColor = Color.Blue
    )
}
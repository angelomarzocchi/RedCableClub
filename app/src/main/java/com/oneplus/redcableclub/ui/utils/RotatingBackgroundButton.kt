package com.oneplus.redcableclub.ui.utils

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun RotatingBackgroundButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    shape: Shape,
    enabled: Boolean = true,
    backgroundColor: Color,
    initialRotation: Float = 0f,
    hapticPulses: Int = 3,
    hapticFeedbackType: HapticFeedbackType = HapticFeedbackType.TextHandleMove,
    targetRotationOnClick: Float = 360f,
    animationSpec: AnimationSpec<Float> = tween(durationMillis = 300),
) {
    var rotationAngleTarget by remember { mutableFloatStateOf(initialRotation) }
    val animatedRotationAngle by animateFloatAsState(
        targetValue = rotationAngleTarget,
        animationSpec = animationSpec,
        label = "customButtonRotation"
    )

    val hapticFeedback = LocalHapticFeedback.current
    var hapticJob by remember { mutableStateOf<Job?>(null) }

    // This LaunchedEffect will trigger when rotationAngleTarget changes (i.e., when a new rotation starts)
    LaunchedEffect(rotationAngleTarget, animationSpec, hapticPulses) {
        // Cancel any previous haptic job if a new click happens mid-animation
        hapticJob?.cancel()

        // Only start haptics if we are actually rotating from a click
        // (Avoids haptics on initial composition if initialRotation != 0)
        // And if there's an actual animation duration.
        val durationMillis = (animationSpec as? TweenSpec)?.durationMillis ?: 300 // Get duration

        if (durationMillis > 0 && hapticPulses > 0 && animatedRotationAngle != rotationAngleTarget) {
            hapticJob = launch {
                val delayPerPulse = durationMillis.toLong() / hapticPulses
                for (i in 1..hapticPulses) {
                    delay(delayPerPulse / 2)
                    if (!isActive) break
                    hapticFeedback.performHapticFeedback(hapticFeedbackType)
                    if (i < hapticPulses) {
                        delay(delayPerPulse / 2)
                    }
                }
            }
        }
    }


            FilledIconButton(
        enabled = enabled,
        onClick = {
            // Determine the next rotation angle
            // This allows for continuous rotation or toggling back
            rotationAngleTarget += targetRotationOnClick // Or use your previous toggle logic if preferred
            onClick()
        },
        modifier = modifier
            .graphicsLayer {
                rotationZ = animatedRotationAngle
            }
            .clip(shape)
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
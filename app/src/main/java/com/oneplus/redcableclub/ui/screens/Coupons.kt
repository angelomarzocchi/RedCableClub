package com.oneplus.redcableclub.ui.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.MembershipStatus
import com.oneplus.redcableclub.data.model.MembershipTier
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme










val explorerInsiderBackgroundColor = Color(0xFFD0D2D3)
val eliteSupremeBackgroundColor = Color(0xFF1C1C1C)

private const val CARD_ASPECT_RATIO = 1.66f
private val DOT_RADIUS_DP = 1.5.dp
private val IDEAL_DOT_SPACING_DP = 10.dp
private const val INITIAL_STEP_VALUE = 5
private const val MIN_STEP_VALUE = 1
private const val MAX_STEP_VALUE = 4
private const val DOT_ALPHA_DIVISOR = 20f

private fun getColorByMembershipTier(membershipTier: MembershipTier): Color {
    return when (membershipTier) {
        MembershipTier.EXPLORER -> explorerInsiderBackgroundColor
        MembershipTier.INSIDER -> explorerInsiderBackgroundColor
        MembershipTier.ELITE -> eliteSupremeBackgroundColor
        MembershipTier.SUPREME -> eliteSupremeBackgroundColor
    }
}



@Composable
fun MembershipTierCard(
    membershipTier: MembershipTier,
    membershipStatus: MembershipStatus,
    points: Int,
    modifier: Modifier = Modifier
) {

    val containerColor = getColorByMembershipTier(membershipTier = membershipTier)
    val dotColor = when(membershipTier) {
            MembershipTier.EXPLORER, MembershipTier.ELITE -> Color.Black
            MembershipTier.INSIDER, MembershipTier.SUPREME -> MaterialTheme.colorScheme.primary
        }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = when (membershipTier) {
                MembershipTier.EXPLORER, MembershipTier.INSIDER -> Color.Black
                else -> Color.White
            }
        ),
        modifier = modifier // This modifier is for the Card itself

    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .aspectRatio(CARD_ASPECT_RATIO)
                .padding(dimensionResource(R.dimen.padding_medium)),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.weight(1f)
                    .fillMaxHeight()
            ) {

                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = membershipTier.label,
                        style = MaterialTheme.typography.displaySmall,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            maxFontSize = MaterialTheme.typography.displaySmall.fontSize,
                        )
                    )

                    Text(
                        text = membershipStatus.label,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }


                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = stringResource(R.string.red_exp_points),
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = MaterialTheme.typography.titleMedium.fontSize,
                            maxFontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        )
                    )

                    Text(
                        text = when(membershipTier) {
                            MembershipTier.SUPREME -> "$points"
                            else ->"${membershipTier.minPoints} - ${membershipTier.maxPoints}"
                        },
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .padding(start = dimensionResource(R.dimen.padding_mini)) // Padding applied before drawWithCache
                    .drawWithCache {
                        val dotRadiusPx = DOT_RADIUS_DP.toPx()
                        val idealDotSpacingPx = IDEAL_DOT_SPACING_DP.toPx()

                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        val availableHeightForDots = canvasHeight - dotRadiusPx
                        val numRowsEstimate = (availableHeightForDots / idealDotSpacingPx).coerceAtLeast(1f).toInt()
                        val adjustedVerticalDotSpacing = if (numRowsEstimate > 0) availableHeightForDots / numRowsEstimate else idealDotSpacingPx

                        val availableWidthForDots = canvasWidth
                        val numColsEstimate = (availableWidthForDots / idealDotSpacingPx).coerceAtLeast(1f).toInt()
                        val adjustedHorizontalDotSpacing = if (numColsEstimate > 0) availableWidthForDots / numColsEstimate else idealDotSpacingPx

                        var currentDotPatternStep = INITIAL_STEP_VALUE

                        onDrawWithContent {

                            if (numColsEstimate > 0 && numRowsEstimate > 0) { // Ensure we can draw
                                for (i in 0..numColsEstimate) {
                                    currentDotPatternStep = (currentDotPatternStep - 1).coerceIn(MIN_STEP_VALUE, MAX_STEP_VALUE)
                                    for (j in 0..numRowsEstimate step currentDotPatternStep) {
                                        val xPos = i * adjustedHorizontalDotSpacing
                                        val yPos = j * adjustedVerticalDotSpacing + dotRadiusPx
                                        drawCircle(
                                            color = dotColor.copy(alpha = (i.toFloat() / DOT_ALPHA_DIVISOR).coerceIn(0f, 1f)),
                                            radius = dotRadiusPx,
                                            center = Offset(xPos, yPos)
                                        )
                                    }
                                }
                            }
                        }
                    },
                contentAlignment = Alignment.TopEnd
            ) {

            }

        }
    }
}


@Preview
@Composable
fun MembershipTierExplorerCardPreview() {
    RedCableClubTheme {
        Surface( ) {
        MembershipTierCard(
            membershipTier = MembershipTier.EXPLORER,
            membershipStatus = MembershipStatus.TO_ACHIEVE,
            points = 100,
            modifier =  Modifier.padding(16.dp)
        )
            }
    }
}

@Preview
@Composable
fun MembershipTierInsiderCardPreview() {
    RedCableClubTheme {
        Surface( ) {
            MembershipTierCard(
                membershipTier = MembershipTier.INSIDER,
                membershipStatus = MembershipStatus.ACHIEVED,
                points = 100,
                modifier =  Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun MembershipTierEliteCardPreview() {
    RedCableClubTheme {
        Surface( ) {
            MembershipTierCard(
                membershipTier = MembershipTier.ELITE,
                membershipStatus = MembershipStatus.CURRENT_TIER,
                points = 100,
                modifier =  Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun MembershipTierSupremeCardPreview() {
    RedCableClubTheme {
        Surface( ) {
            MembershipTierCard(
                membershipTier = MembershipTier.SUPREME,
                membershipStatus = MembershipStatus.ACHIEVED,
                points = 37429,
                modifier =  Modifier.padding(16.dp)
            )
        }
    }
}



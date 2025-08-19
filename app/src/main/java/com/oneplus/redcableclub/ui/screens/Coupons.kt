package com.oneplus.redcableclub.ui.screens


import android.content.ClipData
import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.graphics.shapes.Morph
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Coupon
import com.oneplus.redcableclub.data.model.MembershipStatus
import com.oneplus.redcableclub.data.model.MembershipTier
import com.oneplus.redcableclub.data.model.PercentageCoupon
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.utils.Carousel
import com.oneplus.redcableclub.ui.utils.MorphPolygonShape
import kotlinx.coroutines.launch


@Composable
fun Coupons(
    coupons: List<Coupon>,
    redExpPoints: Int,
    onCouponRedeemed: (Coupon) -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {

    val orientation = LocalConfiguration.current.orientation
    if(orientation == Configuration.ORIENTATION_PORTRAIT)
    Column(modifier = modifier) {
        MembershipTierCardCarousel(points = redExpPoints, modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        LazyColumn(
            modifier = Modifier.padding(
                top = dimensionResource(R.dimen.padding_medium),
                start = dimensionResource(R.dimen.padding_medium),
                end = dimensionResource(R.dimen.padding_medium)
            ),
        ) { items(coupons.size) { index ->
            CouponCard(
                coupon = coupons[index],
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.padding_small),
                    start = dimensionResource(R.dimen.padding_small),
                    end = dimensionResource(R.dimen.padding_small),
                    bottom =  dimensionResource(R.dimen.padding_small) +
                            if(index == coupons.size - 1)
                                paddingValues.calculateBottomPadding()
                            else 0.dp
                ),
                onCouponRedeemed = { onCouponRedeemed(coupons[index]) }
            )
        } }
    } else {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center
            ) {
                MembershipTierCardCarousel(points = redExpPoints)
            }
            Spacer(modifier =
                Modifier.width(dimensionResource(R.dimen.padding_medium))
            )

            LazyColumn(modifier = Modifier
                .weight(1f)) { items(coupons.size) { index ->
                CouponCard(
                    coupon = coupons[index],
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(R.dimen.padding_small) +
                                    if(index == 0)
                                        paddingValues.calculateTopPadding()
                                    else 0.dp,
                            start = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small),
                            bottom =  dimensionResource(R.dimen.padding_small) +
                                    if(index == coupons.size - 1)
                                        paddingValues.calculateBottomPadding()
                                    else 0.dp

                        ),
                    onCouponRedeemed = { onCouponRedeemed(coupons[index])}
                )
                }
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        }
    }

}





@Composable
fun MembershipTierCardCarousel(
    points: Int,
    modifier: Modifier = Modifier,
) {
    val membershipTiers = listOf(
        Pair(MembershipTier.EXPLORER, points),
        Pair(MembershipTier.INSIDER, points),
        Pair(MembershipTier.ELITE, points),
        Pair(MembershipTier.SUPREME, points),
    )

    Carousel(
        items = membershipTiers,
        showOnlyHeroItem = true,
        enableInfiniteScroll = false,
        itemContent = { (membershipTier, points), isHero ->
            MembershipTierCard(
                membershipTier = membershipTier,
                points = points,
                triggerAnimation = isHero,
                modifier = modifier
            )
        }
    )

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CouponCard(
    coupon: Coupon,
    onCouponRedeemed: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var isExpanded by remember { mutableStateOf(false) }

    var isRedeemed by remember { mutableStateOf(coupon.isRedeemed) }
    val clipboard = LocalClipboard.current
    val couponLabel = stringResource(R.string.coupon_code_label)

    val coroutineScope = rememberCoroutineScope()
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "CouponCardArrowRotation"
    )

    val morphProgress by animateFloatAsState(
        targetValue = if (isExpanded) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "CouponCardShapeMorph"
    )

    val couponIconBackground = MaterialTheme.colorScheme.secondaryContainer

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors() ,
        onClick = {isExpanded = !isExpanded}
    ) {
        Column(
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) { // Padding inside the card
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {

                val collapsedShape = MaterialShapes.Cookie4Sided
                val expandedShape = MaterialShapes.Cookie6Sided
                val morph = Morph(start = collapsedShape, end = expandedShape)

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.coupon_size))
                        .clip(MorphPolygonShape(morph = morph, percentage = morphProgress))
                        .size(dimensionResource(id = R.dimen.coupon_size))
                        /* .clip(MaterialShapes.Cookie4Sided.toShape())*/
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Icon(
                        painter = if (coupon is PercentageCoupon) {
                            painterResource(R.drawable.percent_icon)
                        } else {
                            painterResource(R.drawable.money_off_icon)
                        },
                        contentDescription = coupon.description,
                        tint = Color.Black,
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_small))
                    )
                }

                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))

                Text(
                    text = coupon.description,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )


                Icon(
                    imageVector = Icons.Filled.ArrowDownward,
                    contentDescription = if (isExpanded) stringResource(R.string.show_less) else stringResource(R.string.show_more),
                    modifier = Modifier.rotate(rotationAngle)
                )

            }



            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessHigh
                            ),
                            expandFrom = Alignment.Top
                        ),
                exit = shrinkVertically(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioLowBouncy,
                                stiffness = Spring.StiffnessHigh
                            ),
                            shrinkTowards = Alignment.Top
                        )
            ) {
                Column(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
                ) {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant // Subtle divider
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_medium)))

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        coupon.productCategories.forEach { category ->
                            Box(
                                modifier =
                                    Modifier
                                        .clip(MaterialTheme.shapes.small) // Slightly rounded corners for the chip
                                        .background(MaterialTheme.colorScheme.secondaryContainer) // Modern background
                                        .padding(
                                            horizontal = dimensionResource(R.dimen.padding_medium),
                                            vertical = dimensionResource(R.dimen.padding_small)
                                        ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(category.icon),
                                    contentDescription = category.name,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer, // Ensure icon contrast
                                    modifier = Modifier.size(20.dp) // Smaller icon for categories
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

                    AnimatedContent(
                        targetState = isRedeemed,
                        transitionSpec = {
                            if(targetState) {
                                slideInVertically { height -> height } + fadeIn() togetherWith
                                        slideOutVertically { height -> -height } + fadeOut()
                            } else {
                                slideInVertically { height -> -height } + fadeIn() togetherWith
                                        slideOutVertically { height -> height } + fadeOut()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = "RedeemButtonToCodeTransition"
                    ) {targetIsRedeemed ->
                        if(targetIsRedeemed) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.coupon_code, coupon.code),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        coroutineScope.launch {
                                            clipboard.setClipEntry(
                                                ClipEntry(
                                                    ClipData.newPlainText(
                                                        couponLabel,
                                                        coupon.code
                                                    )
                                                )
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentCopy,
                                        contentDescription = stringResource(R.string.copy_code)
                                    )
                                }
                            }
                        } else {
                            ElevatedButton(
                                onClick = {
                                    isRedeemed = true
                                    onCouponRedeemed()
                                          },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(text = stringResource(R.string.redeem_coupon))
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CouponElement(
    coupon: Coupon,
    modifier: Modifier = Modifier,
    iconSize: Dp = dimensionResource(R.dimen.coupon_size),

    ) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(iconSize * 1.5f)) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize)
                .clip(MaterialShapes.Cookie4Sided.toShape())
                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape) // Add a subtle background
        ) {
            Icon(
                painter = if (coupon is PercentageCoupon) {
                    painterResource(R.drawable.percent_icon)
                } else {
                    painterResource(R.drawable.money_off_icon)
                },
                contentDescription = coupon.description,
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
        Text(
            text = coupon.description,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.widthIn(max = iconSize * 1.5f),
            textAlign = TextAlign.Center // Center the text for better balance
        )
    }
}




val explorerInsiderBackgroundColor = Color(0xFFD0D2D3)
val eliteSupremeBackgroundColor = Color(0xFF1C1C1C)

private const val CARD_ASPECT_RATIO = 1.66f

private val FINAL_PLUS_SYMBOL_SIZE_DP = 5.dp
private val INITIAL_DOT_SIZE_DP = 5.dp // Initial size of the dots
private val PLUS_STROKE_WIDTH_DP = 3.dp
private val IDEAL_DOT_SPACING_DP = 22.5.dp
private const val INITIAL_STEP_VALUE = 5
private const val MIN_STEP_VALUE = 1
private const val MAX_STEP_VALUE = 4
private const val DOT_ALPHA_DIVISOR = 10f

private fun getBackgroundColorByMembershipTier(membershipTier: MembershipTier): Color {
    return when (membershipTier) {
        MembershipTier.EXPLORER -> explorerInsiderBackgroundColor
        MembershipTier.INSIDER -> explorerInsiderBackgroundColor
        MembershipTier.ELITE -> eliteSupremeBackgroundColor
        MembershipTier.SUPREME -> eliteSupremeBackgroundColor
    }
}

private fun getDotColorByMembershipTier(membershipTier: MembershipTier): Color {
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
    points: Int,
    triggerAnimation: Boolean, // New parameter to control animation
    modifier: Modifier = Modifier,
) {
    val membershipStatus = MembershipStatus.getMembershipStatusForPoints(membershipTier,points)
    val containerColor = getBackgroundColorByMembershipTier(membershipTier = membershipTier)
    val dotColor = when(membershipTier) {
            MembershipTier.EXPLORER, MembershipTier.ELITE -> Color.Black
            MembershipTier.INSIDER, MembershipTier.SUPREME -> MaterialTheme.colorScheme.primary
        }

    // Removed animationTriggered state and LaunchedEffect

    val animationProgress by animateFloatAsState(
        targetValue = if (triggerAnimation) 1f else 0f, // Use triggerAnimation parameter
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing), // Adjust duration as needed
        label = "SymbolAnimation"
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = when (membershipTier) {
                MembershipTier.EXPLORER, MembershipTier.INSIDER -> Color.Black
                else -> Color.White
            }
        ),
        modifier = modifier

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(CARD_ASPECT_RATIO)
                .padding(dimensionResource(R.dimen.padding_medium)),
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {

                Column(
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        text = membershipTier.label,
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            maxFontSize = MaterialTheme.typography.displaySmall.fontSize,
                        ),
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
                    .padding(all = FINAL_PLUS_SYMBOL_SIZE_DP) // Padding based on final symbol size
                    .drawWithCache {
                        val idealDotSpacingPx = IDEAL_DOT_SPACING_DP.toPx()
                        val strokeWidthPx = PLUS_STROKE_WIDTH_DP.toPx()
                        val finalPlusSymbolArmLengthPx = FINAL_PLUS_SYMBOL_SIZE_DP.toPx()
                        val initialDotRadiusPx = INITIAL_DOT_SIZE_DP.toPx()


                        val canvasWidth = size.width
                        // Calculate canvas height based on the final size of the plus symbol
                        // to ensure the grid is laid out correctly for the maximum symbol extent.
                        val canvasHeight = size.height + finalPlusSymbolArmLengthPx

                        val availableHeightForDots = canvasHeight - finalPlusSymbolArmLengthPx
                        val numRowsEstimate =
                            (availableHeightForDots / idealDotSpacingPx).coerceAtLeast(1f).toInt()
                        val adjustedVerticalDotSpacing =
                            if (numRowsEstimate > 0) availableHeightForDots / numRowsEstimate else idealDotSpacingPx

                        val availableWidthForDots = canvasWidth
                        val numColsEstimate =
                            (availableWidthForDots / idealDotSpacingPx).coerceAtLeast(1f).toInt()
                        val adjustedHorizontalDotSpacing =
                            if (numColsEstimate > 0) availableWidthForDots / numColsEstimate else idealDotSpacingPx



                        onDrawWithContent {
                            var currentDotPatternStep = INITIAL_STEP_VALUE
                            if (numColsEstimate > 0 && numRowsEstimate > 0) { // Ensure we can draw
                                for (i in 0..numColsEstimate) {
                                    currentDotPatternStep = (currentDotPatternStep - 1).coerceIn(
                                        MIN_STEP_VALUE,
                                        MAX_STEP_VALUE
                                    )
                                    val rowInitialValue =
                                        if (currentDotPatternStep == MIN_STEP_VALUE) 0
                                        else currentDotPatternStep % 2
                                    for (j in rowInitialValue..numRowsEstimate step currentDotPatternStep) {
                                        val xPos = i * adjustedHorizontalDotSpacing
                                        val yPos = j * adjustedVerticalDotSpacing

                                        val symbolColorWithAlpha = dotColor.copy(
                                            alpha = (i.toFloat() / DOT_ALPHA_DIVISOR).coerceIn(
                                                0f,
                                                1f
                                            )
                                        )

                                        // Calculate current sizes based on animation progress
                                        val currentDotRadiusPx =
                                            initialDotRadiusPx * (1f - animationProgress)
                                        val currentPlusArmLengthPx =
                                            finalPlusSymbolArmLengthPx * animationProgress

                                        // Draw dot if its size is still positive
                                        if (currentDotRadiusPx > 0f) {
                                            drawCircle(
                                                color = symbolColorWithAlpha,
                                                radius = currentDotRadiusPx,
                                                center = Offset(xPos, yPos)
                                            )
                                        }

                                        // Draw plus if its size is positive
                                        if (currentPlusArmLengthPx > 0f) {
                                            // Draw horizontal line of the "+"
                                            drawLine(
                                                color = symbolColorWithAlpha,
                                                start = Offset(xPos - currentPlusArmLengthPx, yPos),
                                                end = Offset(xPos + currentPlusArmLengthPx, yPos),
                                                strokeWidth = strokeWidthPx
                                            )

                                            // Draw vertical line of the "+"
                                            drawLine(
                                                color = symbolColorWithAlpha,
                                                start = Offset(xPos, yPos - currentPlusArmLengthPx),
                                                end = Offset(xPos, yPos + currentPlusArmLengthPx),
                                                strokeWidth = strokeWidthPx
                                            )
                                        }
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

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun CouponsLandscapePreview() {
    RedCableClubTheme {
        Surface {
            Coupons(
                coupons = RedCableClubApiServiceMock.userMock.wallet,
                redExpPoints = 3500,
                onCouponRedeemed = {}
            )
        }
    }
}

@Preview()
@Composable
fun CouponsPreview() {
    RedCableClubTheme {
        Surface {
            Coupons(
                coupons = RedCableClubApiServiceMock.userMock.wallet,
                redExpPoints = 3500,
                onCouponRedeemed = {}
            )
        }
    }
}

@Preview
@Composable
fun CouponCardPreview() {
    RedCableClubTheme {
        Surface {
            CouponCard(
                coupon = RedCableClubApiServiceMock.userMock.wallet[4],
                onCouponRedeemed = {}
            )
        }
    }
}

@Preview
@Composable
fun MembershipTierCardCarouselPreview() {
    RedCableClubTheme {
        Surface {
            MembershipTierCardCarousel(
                points = 3500
            )
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
            points = 100,
            triggerAnimation = true, // Preview with animation
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
                points = 3500,
                triggerAnimation = true, // Preview with animation
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
                points = 100,
                triggerAnimation = true, // Preview with animation
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
                points = 37429,
                triggerAnimation = true, // Preview with animation
                modifier =  Modifier.padding(16.dp)
            )
        }
    }
}

package com.oneplus.redcableclub.ui.utils

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.oneplus.redcableclub.R

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> Carousel(
    items: List<T>,
    itemContent: @Composable (item: T, isHero: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = dimensionResource(R.dimen.padding_small),
    bottomPadding:Dp = 0.dp,
    showOnlyHeroItem: Boolean = false,
    enableInfiniteScroll: Boolean = true
) {

    if(items.isEmpty()) return

    val hapticFeedback = LocalHapticFeedback.current

    val itemsSize = items.size.coerceAtLeast(1)
    val actualPageCount = if (enableInfiniteScroll) Int.MAX_VALUE else itemsSize
    val initialActualPage = if (enableInfiniteScroll) {
        (Int.MAX_VALUE / 2) - ((Int.MAX_VALUE / 2).mod(itemsSize.coerceAtLeast(1)))
    } else {
        0
    }

    // 3. Remember PagerState with the virtual count and calculated start page
    val pagerState = rememberPagerState(
        initialPage = initialActualPage,
        pageCount = { actualPageCount }
    )

    val currentPageIndex = if( enableInfiniteScroll) {
        pagerState.currentPage.mod(itemsSize)
    } else {
        pagerState.currentPage
    }


    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.currentPage == pagerState.settledPage) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.SegmentFrequentTick)

        }
    }


    Column {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = if(showOnlyHeroItem) {
                PaddingValues(horizontal = 0.dp)
            } else {
                PaddingValues(horizontal = dimensionResource(R.dimen.carousel_base_padding) + itemSpacing / 2)
            },
            pageSpacing = itemSpacing
        ) { virtualPageIndex ->

            val actualIndex = if (enableInfiniteScroll) {
                virtualPageIndex.mod(itemsSize.coerceAtLeast(1))
            } else {
                virtualPageIndex // For finite scroll, virtualPageIndex is the actual index
            }

            // Ensure actualIndex is valid before accessing items
            if (actualIndex < 0 || actualIndex >= itemsSize) {
                // This case should ideally not happen
                Box(modifier = Modifier.fillMaxWidth())
                return@HorizontalPager
            }

            val isHero = if(enableInfiniteScroll) {
                virtualPageIndex == pagerState.currentPage
            } else {
                actualIndex == currentPageIndex
            }

            val graphicsModifier = if(showOnlyHeroItem) {
                Modifier
            } else {
                val pageOffset =
                    (pagerState.currentPage - virtualPageIndex) + pagerState.currentPageOffsetFraction
                val scale by animateFloatAsState(
                    targetValue = lerp(1f, 0.85f, pageOffset.absoluteValue.coerceAtMost(1f)),
                    label = "scaleAnimation",

                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
                val alpha by animateFloatAsState(
                    targetValue = lerp(1f, 0.7f, pageOffset.absoluteValue.coerceAtMost(1f)),
                    label = "alphaAnimation"
                )
                Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                }
            }




            Box(
                modifier = graphicsModifier
            ) {
                itemContent(items[actualIndex], isHero)
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))

        DynamicCarouselIndicator(
            size = itemsSize,
            currentPage = currentPageIndex,
            modifier = Modifier.padding(
                bottom = bottomPadding
            )
        )
    }
}

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CarouselHeroPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    Carousel(
        items = items,
        showOnlyHeroItem = true,
        itemContent = { item, _ ->
            // Replace with your actual item content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                //Text(text = item, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.align(Alignment.Center))
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun CarouselPreview() {
    val items = listOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")
    Carousel(
        items = items,
        itemContent = { item, _ ->
            // Replace with your actual item content
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                //Text(text = item, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.align(Alignment.Center))
            }
        }
    )
}
@Composable
fun DynamicCarouselIndicator(
    size: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    maxVisibleIndicators: Int = 7,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.LightGray,
    indicatorSize: Dp = 8.dp,
    indicatorPadding: Dp = 4.dp,
) {

    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(currentPage) {
        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
    }

    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically // Align text vertically
    ) {
        // Calculate the range of indicators to display
        val startIndex = max(0, currentPage - maxVisibleIndicators / 2)
        val endIndex = min(size - 1, startIndex + maxVisibleIndicators - 1)
        if (startIndex > 0) {
            for(i in min(3,startIndex) downTo 1) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = indicatorPadding)
                        .clip(CircleShape)
                        .background(inactiveColor)
                        .size(indicatorSize / (i + 1))
                )
            }
        }


        for (i in startIndex..endIndex) {
            val isCurrentPage = currentPage == i

            val indicatorColor by animateColorAsState(
                targetValue = if (isCurrentPage) activeColor else inactiveColor,
                label = "indicatorColorAnimation"
                // You can customize animation spec here, e.g., tween(durationMillis = 300)
            )

            val indicatorWidth by animateDpAsState(
                targetValue = if (isCurrentPage) indicatorSize * 2 else indicatorSize,
                label = "indicatorWidthAnimation",
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
                // You can customize animation spec here
            )
            val cornerSize by animateDpAsState(
                targetValue = if (isCurrentPage) indicatorSize else indicatorSize / 2, // Half of size for circular appearance
                label = "indicatorCornerSizeAnimation",
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )

            Box(
                modifier = Modifier
                    .padding(horizontal = indicatorPadding)
                    .clip(RoundedCornerShape(cornerSize))
                    .background(indicatorColor)
                    .size(
                        height = indicatorSize,
                        width = indicatorWidth
                    )
            )
        }


        if (endIndex < size - 1) {
            for(i in 1..min(3,size - 1 - endIndex)) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = indicatorPadding)
                        .clip(CircleShape)
                        .background(inactiveColor)
                        .size(indicatorSize / (i + 1))
                )
            }
        }
    }
}
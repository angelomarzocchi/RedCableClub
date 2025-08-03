package com.oneplus.redcableclub.ui.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oneplus.redcableclub.R
import androidx.compose.material3.Text

enum class DetailWithLazyGridLayoutMode {
    COLLAPSING_VERTICAL,
    SIDE_BY_SIDE_HORIZONTAL
}

private val DefaultMaxHeaderHeight = 280.dp
private val DefaultMinHeaderHeight = 120.dp
private val DefaultSideDetailWidth = 300.dp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun <T> CollapsingDetailWithLazyGrid(
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    layoutMode: DetailWithLazyGridLayoutMode = DetailWithLazyGridLayoutMode.COLLAPSING_VERTICAL,
    scaffoldModifier: Modifier = Modifier,
    // Parameters for COLLAPSING_VERTICAL mode
    maxHeaderHeight: Dp = DefaultMaxHeaderHeight,
    minHeaderHeight: Dp = DefaultMinHeaderHeight,
    // Parameters for SIDE_BY_SIDE_HORIZONTAL mode
    sideDetailWidth: Dp = DefaultSideDetailWidth,
    headerPadding: PaddingValues = PaddingValues(all = dimensionResource(R.dimen.padding_medium)),
    gridContentPadding: PaddingValues = PaddingValues(all = dimensionResource(R.dimen.padding_medium)),
    gridArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
    gridColumns: GridCells = GridCells.Fixed(3),
    animationDurationInMillis: Int = 300,
    detailContent: @Composable SharedTransitionScope.(
        item: T,
        collapseProgress: Float,
        isExpanded: Boolean,
        animatedVisibilityScope: AnimatedVisibilityScope
    ) -> Unit,
    gridItemContent: @Composable SharedTransitionScope.(
            item: T,
            onClick: () -> Unit
            ) -> Unit
    ) {
    if(items.isEmpty() && selectedItem == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No items to display.") // Or a more sophisticated empty state
        }
        return
    }


    val density = LocalDensity.current

    SharedTransitionLayout(modifier = modifier.fillMaxSize()) {
        when(layoutMode) {
            DetailWithLazyGridLayoutMode.COLLAPSING_VERTICAL -> {
                val maxHeaderHeightPx = remember(maxHeaderHeight) { with(density) { maxHeaderHeight.toPx() } }
                val minHeaderHeightPx = remember(minHeaderHeight) { with(density) { minHeaderHeight.toPx() } }
                var currentHeaderHeightPx by remember { mutableFloatStateOf(maxHeaderHeightPx) }
                val gridState = rememberLazyGridState()

                val nestedScrollConnection = remember {
                    object : NestedScrollConnection {
                        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                            val delta = available.y
                            val newHeight = currentHeaderHeightPx + delta
                            val newClampedHeight = newHeight.coerceIn(minHeaderHeightPx, maxHeaderHeightPx)
                            val consumed = newClampedHeight - currentHeaderHeightPx
                            currentHeaderHeightPx = newClampedHeight
                            return Offset(0f, consumed)
                        }
                    }
                }

                val headerHeightDp: Dp by remember {
                    derivedStateOf { with(density) { currentHeaderHeightPx.toDp() } }
                }

                val collapseProgress by remember {
                    derivedStateOf {
                        val range = maxHeaderHeightPx - minHeaderHeightPx
                        if (range == 0f) 1f else ((currentHeaderHeightPx - minHeaderHeightPx) / range).coerceIn(0f, 1f)
                    }
                }

                Surface(
                    modifier = scaffoldModifier
                        .fillMaxSize()
                        .nestedScroll(nestedScrollConnection)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        //Collapsible Header Section
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(headerHeightDp)
                                .padding(headerPadding)
                        ) {
                            selectedItem?.let { currentItem ->
                                val isExpandedLayout = collapseProgress > 0.5f
                                val animationDelay = animationDurationInMillis / 5

                                Card(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .animateContentSize(animationSpec = tween(animationDurationInMillis))
                                ) {
                                    AnimatedContent(
                                        targetState = isExpandedLayout,
                                        modifier = Modifier.fillMaxSize(),
                                        label = "collapsing_header_detail_transition",
                                        transitionSpec = {
                                            val enter = fadeIn(animationSpec = tween(durationMillis = animationDurationInMillis, delayMillis = animationDelay)) +
                                                    expandVertically(
                                                        animationSpec = tween(durationMillis = animationDurationInMillis, delayMillis = animationDelay),
                                                        expandFrom = Alignment.Top
                                                    )
                                            val exit = fadeOut(animationSpec = tween(durationMillis = animationDurationInMillis)) +
                                                    shrinkVertically(animationSpec = tween(durationMillis = animationDurationInMillis), shrinkTowards = Alignment.Top)
                                            enter togetherWith exit
                                        }
                                    ) { targetIsExpanded ->
                                        this@SharedTransitionLayout.detailContent(
                                            currentItem,
                                            collapseProgress,
                                            targetIsExpanded,
                                            this
                                        )
                                    }
                                }
                            }
                        } //end box

                        LazyVerticalGrid(
                            columns = gridColumns,
                            state = gridState,
                            contentPadding = gridContentPadding,
                            verticalArrangement =   gridArrangement,
                            horizontalArrangement = gridArrangement,
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                        ) {
                            items(
                                count = items.size,
                                key = { index -> items[index].hashCode() }
                            ) {index ->
                                val item = items[index]
                                this@SharedTransitionLayout.gridItemContent(
                                    item,
                                    { onItemSelected(item) }
                                )

                            }
                        }
                    }
                }

            }
            DetailWithLazyGridLayoutMode.SIDE_BY_SIDE_HORIZONTAL -> {
                val gridState = rememberLazyGridState()

                Scaffold(
                    modifier = scaffoldModifier.fillMaxSize()
                ) { scaffoldPaddingValues ->
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(scaffoldPaddingValues)
                    ) {
                        Box(
                            modifier = Modifier
                                .width(sideDetailWidth)
                                .fillMaxHeight()
                                .padding(headerPadding)
                        ) {
                            selectedItem?.let { currentItem ->
                                val isDetailInternallyExpanded = true

                                Card(modifier = Modifier.fillMaxSize()) {
                                    AnimatedContent(
                                        targetState = isDetailInternallyExpanded,
                                        modifier = Modifier.fillMaxSize(),
                                        label = "side_detail_content_transition"
                                    ) {targetIsExpended ->
                                        this@SharedTransitionLayout.detailContent(
                                            currentItem,
                                            1f,
                                            targetIsExpended,
                                            this
                                        )
                                    }
                                }
                            }
                        }

                        LazyVerticalGrid(
                            columns = gridColumns,
                            state = gridState,
                            contentPadding = gridContentPadding,
                            verticalArrangement = gridArrangement,
                            horizontalArrangement = gridArrangement,
                            modifier = Modifier.fillMaxSize().weight(1f)
                        ) {
                            items(
                                count = items.size,
                                key = { index -> items[index].hashCode() }
                            ) { index ->
                                val item = items[index]
                                this@SharedTransitionLayout.gridItemContent(
                                    item,
                                    { onItemSelected(item)
                                    }
                                )
                            }
                        }
                    }
                }
            }

        }

    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun CollapsingToolbarWithLazyGridPreview() {
    val items = List(10) { "Item $it" }
    var selectedItem by remember { mutableStateOf<String?>(items.firstOrNull()) }

    CollapsingDetailWithLazyGrid(
        items = items,
        selectedItem = selectedItem,
        onItemSelected = { item -> selectedItem = item },
        detailContent = { item, collapseProgress, isExpanded, _ ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Detail for $item")
                    Text(text = "Collapse Progress: %.2f".format(collapseProgress))
                    Text(text = if (isExpanded) "Expanded" else "Collapsed")
                }
            }
        },
        gridItemContent = { item, onClick ->
            Card(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Box(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)), contentAlignment = Alignment.Center) {
                    Text(text = item)
                }
            }
        }
    )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
private fun CollapsingToolbarWithLazyGridPreviewHorizontal() {
    val items = List(18) { "Item $it" }
    var selectedItem by remember { mutableStateOf<String?>(items.firstOrNull()) }

    CollapsingDetailWithLazyGrid(
        items = items,
        selectedItem = selectedItem,
        onItemSelected = { item -> selectedItem = item },
        layoutMode = DetailWithLazyGridLayoutMode.SIDE_BY_SIDE_HORIZONTAL,
        detailContent = { item, collapseProgress, isExpanded, _ ->
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Detail for $item")
                    Text(text = "Collapse Progress: %.2f".format(collapseProgress))
                    Text(text = if (isExpanded) "Expanded" else "Collapsed")
                }
            }
        },
        gridItemContent = { item, onClick ->
            Card(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Box(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)), contentAlignment = Alignment.Center) {
                    Text(text = item)
                }
            }
        }
    )
}
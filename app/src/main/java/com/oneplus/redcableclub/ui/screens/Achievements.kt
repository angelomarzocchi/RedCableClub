package com.oneplus.redcableclub.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Achievement
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation

// Define constants for header heights
private val MaxHeaderHeight = 280.dp
private val MinHeaderHeight = 120.dp

private val BadgeSizeExtraLarge = 120.dp
private val BadgeSizeSmall = 52.dp

private val NameTextSizeExpanded = 32.sp
private val NameTextSizeCollapsed = 24.sp




@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Achievements(
    achievements: List<Achievement>,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp)

) {
    var selectedAchievement: Achievement by remember { mutableStateOf(achievements[0]) }
    val density = LocalDensity.current

    // Convert dp heights to pixels for nested scroll calculations
    val maxHeaderHeightPx = remember { with(density) { MaxHeaderHeight.toPx() } }
    val minHeaderHeightPx = remember { with(density) { MinHeaderHeight.toPx() } }

    // State to track the current header height in pixels
    var currentHeaderHeightPx by remember { mutableFloatStateOf(maxHeaderHeightPx) }

    // LazyGridState for the LazyVerticalGrid to observe scroll position
    val gridState = rememberLazyGridState()

    // NestedScrollConnection to intercept scroll events
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            // Called when a scroll event is about to happen
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Determine the amount to scroll based on user input
                val delta = available.y
                val newHeight = currentHeaderHeightPx + delta

                // Clamp the new height between min and max header heights
                val newClampedHeight = newHeight.coerceIn(minHeaderHeightPx, maxHeaderHeightPx)

                // Calculate the consumed scroll amount
                val consumed = newClampedHeight - currentHeaderHeightPx

                // Update the header height
                currentHeaderHeightPx = newClampedHeight

                // Return the consumed scroll amount
                return Offset(0f, consumed)
            }
        }
    }

    SharedTransitionLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
                // Attach the nested scroll connection to the Scaffold
                .nestedScroll(nestedScrollConnection)
                .padding(top = paddingValues.calculateTopPadding())
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // Convert current header height from pixels back to dp
                val headerHeightDp: Dp by remember {
                    derivedStateOf {
                        with(density) { currentHeaderHeightPx.toDp() }
                    }
                }

                val collapseProgress by remember {
                    derivedStateOf {
                        val range = maxHeaderHeightPx - minHeaderHeightPx
                        if (range == 0f) 1f
                        else ((currentHeaderHeightPx - minHeaderHeightPx) / range).coerceIn(0f, 1f)
                    }
                }

                // Collapsible Header Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeightDp) // Apply the dynamic height
                        .padding(
                            start = dimensionResource(R.dimen.padding_small),
                            end = dimensionResource(R.dimen.padding_small),
                        )
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {

                        AchievementDetail(
                            achievement = selectedAchievement,
                            collapseProgress = collapseProgress,
                            sharedTransitionScope = this@SharedTransitionLayout, // Pass the scope
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // LazyVerticalGrid Section
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3), // 3 columns for the grid
                    state = gridState, // Attach the grid state
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Generate 50 sample items for the grid
                    items(achievements.size) { index ->
                        Achievement(
                            achievement = achievements[index],
                            onClick = { selectedAchievement = achievements[index] }
                        )
                    }
                }
            }
        }
    }


}



@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AchievementDetail(
    achievement: Achievement,
    collapseProgress: Float,
    sharedTransitionScope: SharedTransitionScope, // Receive the scope
    modifier: Modifier = Modifier
) {
    // Use the SharedTransitionScope for shared element modifiers
    with(sharedTransitionScope) {
        // Determine if the header is expanded enough to switch to the expanded layout.
        val isExpandedLayout = collapseProgress > 0.5f

        // Animate the image size using linear interpolation (lerp) based on collapse progress.
        val animatedImageSize: Dp by remember(collapseProgress) {
            derivedStateOf {
                lerp(BadgeSizeSmall, BadgeSizeExtraLarge, collapseProgress)
            }
        }

        // Animate the name text font size using linear interpolation.
        val animatedNameTextSize by remember(collapseProgress) {
            derivedStateOf {
                lerp(NameTextSizeCollapsed, NameTextSizeExpanded, collapseProgress)
            }
        }

        // Create shared content states for the elements that will transition
        // The key should be unique for each shared element.
        val iconSharedState = rememberSharedContentState(key = "achievement-icon-${achievement.iconUrl}")
        val nameSharedState = rememberSharedContentState(key = "achievement-name-${achievement.name}")

        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
                // animateContentSize makes the card smoothly animate its own size changes
                .animateContentSize(animationSpec = tween(300))
        ) {
            // AnimatedContent handles the smooth transition between the Column (expanded)
            // and Row (collapsed) layouts based on `targetIsExpanded`.
            AnimatedContent(
                targetState = isExpandedLayout,
                modifier = Modifier.fillMaxSize(),
                label = "achievement_detail_layout_transition",
                transitionSpec = {
                    // Define transitions for content entering and exiting.
                    // `togetherWith` applies transitions to both the old and new content concurrently.
                    // These transitions apply to the *container* of the shared elements,
                    // while shared elements handle their own positional animation.
                    ( fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(durationMillis = 300, delayMillis = 150), expandFrom = Alignment.Top)) togetherWith
                            (fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(durationMillis = 300, delayMillis = 150), shrinkTowards = Alignment.Top))
                }
            ) { targetIsExpanded ->
                if (targetIsExpanded) {
                    // Expanded state: Content arranged in a vertical Column.
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_small)),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Achievement Icon - now a shared element
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(achievement.iconUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = achievement.description,
                            modifier = Modifier
                                .size(animatedImageSize) // Size driven by lerp
                                .sharedElement(iconSharedState, this@AnimatedContent), // Shared element modifier
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .size(animatedImageSize)
                                        .shimmerLoadingAnimation()
                                )
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .size(animatedImageSize)
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Image loading failed",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(animatedImageSize * 0.7f)
                                    )
                                }
                            }
                        )

                        Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

                        // Achievement Name - now a shared element
                        Text(
                            text = achievement.name,
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = animatedNameTextSize), // Font size driven by lerp
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.sharedElement(nameSharedState, this@AnimatedContent) // Shared element modifier
                        )

                        // Achievement Description: Animates its visibility based on expansion.
                        AnimatedVisibility(
                            visible = collapseProgress > 0.8f,
                            enter = fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 100)) +
                                    slideInVertically(initialOffsetY = { it / 2 }, animationSpec = tween(durationMillis = 300, delayMillis = 100)),
                            exit = fadeOut(animationSpec = tween(durationMillis = 300)) +
                                    slideOutVertically(targetOffsetY = { it / 2 }, animationSpec = tween(durationMillis = 300))
                        ) {
                            Text(
                                text = achievement.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                } else {
                    // Collapsed state: Content arranged in a horizontal Row.
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(dimensionResource(R.dimen.padding_small)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        // Achievement Icon - shared element
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(achievement.iconUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = achievement.description,
                            modifier = Modifier
                                .size(animatedImageSize) // Size driven by lerp
                                .sharedElement(
                                    sharedContentState = iconSharedState,
                                    animatedVisibilityScope = this@AnimatedContent,
                                ), // Shared element modifier
                            loading = {
                                Box(
                                    modifier = Modifier
                                        .size(animatedImageSize)
                                        .shimmerLoadingAnimation()
                                )
                            },
                            error = {
                                Box(
                                    modifier = Modifier
                                        .size(animatedImageSize)
                                        .background(MaterialTheme.colorScheme.surfaceVariant),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = "Image loading failed",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(animatedImageSize * 0.7f)
                                    )
                                }
                            }
                        )

                        Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))

                        // Achievement Name - shared element
                        Text(
                            text = achievement.name,
                            style = MaterialTheme.typography.displaySmall.copy(fontSize = animatedNameTextSize), // Font size driven by lerp
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.sharedElement(nameSharedState, this@AnimatedContent) // Shared element modifier
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Achievement(
    achievement: Achievement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.badge_size_large)
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(achievement.iconUrl)
                .crossfade(true)
                .build(),
            contentDescription = achievement.description,
            modifier = modifier
                .padding(dimensionResource(R.dimen.padding_small))
                .size(size)
                .clickable(onClick = onClick),
            loading = {
                Box(
                    modifier = Modifier
                        .size(size)
                        .shimmerLoadingAnimation()
                )
            },
            error = {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant), // Simple background for error
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Image loading failed",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(size)
                    )
                }
            }
        )

        Text(
            text = achievement.name,
            style = MaterialTheme.typography.titleSmall
        )
    }
}


@Preview
@Composable
fun AchievementScreenPreview() {
    RedCableClubTheme {
        Surface {
            Achievements(
                achievements = RedCableClubApiServiceMock.userMock.achievements
            )
        }
    }
}

@Preview
@Composable
fun AchievementPreview() {
    RedCableClubTheme {
        Surface {
        Achievement(
            achievement =
                RedCableClubApiServiceMock.userMock.achievements[2],
            onClick = {}
        )
            }
    }
}
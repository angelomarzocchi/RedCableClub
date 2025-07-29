package com.oneplus.redcableclub.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
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
import com.oneplus.redcableclub.ui.utils.CollapsingToolbarWithLazyGrid
import com.oneplus.redcableclub.ui.utils.DetailWithLazyGridLayoutMode
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation

// Define constants for header heights
private val MaxHeaderHeight = 280.dp
private val MinHeaderHeight = 120.dp

private val BadgeSizeExtraLarge = 120.dp
private val BadgeSizeSmall = 52.dp






@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Achievements(
    achievements: List<Achievement>,
    selectedAchievement: Achievement?,
    onItemSelected: (Achievement) -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp)
) {

    val configuration = LocalConfiguration.current
    val currentLayoutMode = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        DetailWithLazyGridLayoutMode.SIDE_BY_SIDE_HORIZONTAL
    } else {
        DetailWithLazyGridLayoutMode.COLLAPSING_VERTICAL
    }





    CollapsingToolbarWithLazyGrid(
        items = achievements,
        layoutMode = currentLayoutMode,
        selectedItem = selectedAchievement,
        onItemSelected = { achievement -> onItemSelected(achievement)},
        detailContent = {item, collapseProgress, isExpanded, animatedVisibilityScope ->
            AchievementDetail(
                achievement = item,
                collapseProgress = collapseProgress,
                isExpanded = isExpanded,
                animatedVisibilityScope = animatedVisibilityScope,
                sharedTransitionScope = this
            )
        },
        gridItemContent = { item, onClick ->
            Achievement(
                achievement = item,
                onClick = onClick
            )
        },
        modifier = modifier,
        scaffoldModifier = Modifier.padding(top = paddingValues.calculateTopPadding()),
    )

}

private val DefaultNameTextSizeExpanded = 32.sp
private val DefaultNameTextSizeCollapsed = 24.sp

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AchievementDetail(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    achievement: Achievement?,
    collapseProgress: Float,
    modifier: Modifier = Modifier,
    isExpanded: Boolean = false,
    badgeSizeExtraLarge: Dp = BadgeSizeExtraLarge,
    badeSizeSmall: Dp = BadgeSizeSmall,
    nameTextSizeExpanded: TextUnit = DefaultNameTextSizeExpanded,
    nameTextSizeCollapsed: TextUnit = DefaultNameTextSizeCollapsed,
) {
    with(sharedTransitionScope) {
        val animatedImageSize: Dp by remember(
            collapseProgress,
            badeSizeSmall,
            badgeSizeExtraLarge
        ) {
            derivedStateOf {
                lerp(badeSizeSmall, badgeSizeExtraLarge, collapseProgress)
            }
        }
        val animatedNameTextSize: TextUnit by remember(
            collapseProgress,
            nameTextSizeCollapsed,
            nameTextSizeExpanded
        ) {
            derivedStateOf {
                lerp(nameTextSizeCollapsed, nameTextSizeExpanded, collapseProgress)
            }
        }

        if(achievement == null) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No achievement selected.")
            }
            return
        }

        val iconSharedKey =
            "achievement-icon-${achievement.iconUrl}" // Assuming Achievement has a stable `id`
        val nameSharedKey = "achievement-name-${achievement.name}"

        if(isExpanded) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(dimensionResource(R.dimen.padding_medium)), // Consistent padding
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(achievement.iconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = achievement.description,
                    modifier = Modifier
                        .size(animatedImageSize)
                        .sharedElement(
                            rememberSharedContentState(key = iconSharedKey),
                            animatedVisibilityScope = animatedVisibilityScope // CRUCIAL
                        ),
                    loading = {
                        Box(
                            modifier = Modifier
                                .size(animatedImageSize)
                                .shimmerLoadingAnimation()
                        )
                    },
                    error = { ErrorIcon(size = animatedImageSize) }
                )

                Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))

                Text(
                    text = achievement.name,
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = animatedNameTextSize),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = nameTextSizeCollapsed,maxFontSize = nameTextSizeExpanded),
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = nameSharedKey),
                        animatedVisibilityScope = animatedVisibilityScope // CRUCIAL
                    )
                )

                AnimatedVisibility(
                    visible = collapseProgress > 0.8f, // Show when mostly expanded
                    enter = fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 100)) +
                            slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = tween(durationMillis = 300, delayMillis = 100)
                            ),
                    exit = fadeOut(animationSpec = tween(durationMillis = 300)) +
                            slideOutVertically(
                                targetOffsetY = { it / 2 },
                                animationSpec = tween(durationMillis = 300)
                            )
                ) {
                    Text(
                        text = achievement.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3, // Allow more lines in expanded view
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_extra_small))
                    )
                }
            }
        } else {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = dimensionResource(R.dimen.padding_medium),
                        vertical = dimensionResource(R.dimen.padding_small)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(achievement.iconUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = achievement.description,
                    modifier = Modifier
                        .size(animatedImageSize)
                        .sharedElement(
                            rememberSharedContentState(key = iconSharedKey),
                            animatedVisibilityScope = animatedVisibilityScope // CRUCIAL
                        ),
                    loading = {
                        Box(
                            modifier = Modifier
                                .size(animatedImageSize)
                                .shimmerLoadingAnimation()
                        )
                    },
                    error = { ErrorIcon(size = animatedImageSize) }
                )
                Spacer(Modifier.width(dimensionResource(R.dimen.padding_small)))
                Text(
                    text = achievement.name,
                    style = MaterialTheme.typography.displaySmall.copy(fontSize = animatedNameTextSize),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.sharedElement(
                        rememberSharedContentState(key = nameSharedKey),
                        animatedVisibilityScope = animatedVisibilityScope // CRUCIAL
                    )
                )
            }
        }
    }

}

@Composable
private fun ErrorIcon(size: Dp) {
    Box(
        modifier = Modifier
            .size(size)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Filled.Clear,
            contentDescription = "Image loading failed",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(size * 0.7f)
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Achievement(
    achievement: Achievement,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.badge_size_large),
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
                achievements = RedCableClubApiServiceMock.userMock.achievements,
                selectedAchievement = RedCableClubApiServiceMock.userMock.achievements[0],
                onItemSelected = {}
            )
        }
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun AchievementScreenPreviewHorizontal() {
    RedCableClubTheme {
        Surface {
            Achievements(
                achievements = RedCableClubApiServiceMock.userMock.achievements,
                selectedAchievement = RedCableClubApiServiceMock.userMock.achievements[0],
                onItemSelected = {}
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
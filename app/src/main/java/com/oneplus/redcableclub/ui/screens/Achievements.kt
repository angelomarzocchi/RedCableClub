package com.oneplus.redcableclub.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.oneplus.redcableclub.ui.utils.CollapsingDetailWithLazyGrid
import com.oneplus.redcableclub.ui.utils.DetailWithLazyGridLayoutMode
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation

private val BadgeSizeExtraLarge = 120.dp
private val BadgeSizeSmall = 70.dp


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


    CollapsingDetailWithLazyGrid(
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
    if(achievement == null) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text( text = stringResource(R.string.no_achievement))
        }
        return
    }

    val currentBadgeSize by remember(collapseProgress) {
        derivedStateOf {
            lerp(BadgeSizeSmall, BadgeSizeExtraLarge, collapseProgress)
        }
    }



    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
        contentAlignment = Alignment.Center
    ) {
        with(sharedTransitionScope) {
            if (isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    AchievementImage(
                        achievement = achievement,
                        modifier = modifier.sharedElement(
                            rememberSharedContentState(key = "achievement_image"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        onClick = { },
                        size = currentBadgeSize
                    )


                    Text(
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "achievement_name"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        text = achievement.name,
                        autoSize = TextAutoSize
                            .StepBased(
                                minFontSize = nameTextSizeCollapsed,
                                maxFontSize = nameTextSizeExpanded
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                    ) {
                        if(!achievement.isAchieved)
                            Icon(
                                painter = painterResource(id = R.drawable.lock_icon),
                                contentDescription = stringResource(R.string.locked_badge)
                            )
                        Text(
                            text = if(achievement.isAchieved) achievement.description else stringResource(R.string.locked_badge)
                        )
                    }

                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {
                    AchievementImage(
                        achievement = achievement,
                        modifier = modifier.sharedElement(
                            rememberSharedContentState(key = "achievement_image"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        onClick = { },
                        size = BadgeSizeSmall
                    )
                    Text(
                        modifier = Modifier.sharedElement(
                            rememberSharedContentState(key = "achievement_name"),
                            animatedVisibilityScope = animatedVisibilityScope
                        ),
                        text = achievement.name,
                        autoSize = TextAutoSize
                            .StepBased(
                                minFontSize = nameTextSizeCollapsed,
                                maxFontSize = nameTextSizeExpanded
                            ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
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
        AchievementImage(
            achievement = achievement,
            modifier = modifier,
            onClick = onClick,
            size = size
        )

        Text(
            text = achievement.name,
            style = MaterialTheme.typography.titleSmall,
        )
    }
}

@Composable
fun AchievementImage(achievement: Achievement, modifier: Modifier = Modifier, onClick: () -> Unit, size: Dp = dimensionResource(R.dimen.badge_size_large)) {
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
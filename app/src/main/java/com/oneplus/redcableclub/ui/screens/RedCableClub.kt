package com.oneplus.redcableclub.ui.screens

import android.util.Log
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalCenteredHeroCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Achievement
import com.oneplus.redcableclub.data.model.Ad
import com.oneplus.redcableclub.data.model.Coupon
import com.oneplus.redcableclub.data.model.MembershipTier
import com.oneplus.redcableclub.data.model.UserProfile
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.theme.neverSettleLogoResource
import com.oneplus.redcableclub.ui.utils.DynamicCarouselIndicator
import com.oneplus.redcableclub.ui.utils.FrostedGlassBox
import com.oneplus.redcableclub.ui.utils.ResourceState
import com.oneplus.redcableclub.ui.utils.RotatingBackgroundButton
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation


@Composable
fun RedCableClub(
    uiState: RedCableClubUiState,
    onAchievementDetailClick: () -> Unit,
    onShowAllCouponsClick: () -> Unit,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    ) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)

    ) {

        val userProfileUiState = uiState.userProfileState
        Crossfade(
            targetState = userProfileUiState,
            animationSpec = tween(durationMillis = 500),

        ) {state ->
            when(state) {
                is ResourceState.Loading -> ProfileCardSkeleton(
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium),start = dimensionResource(R.dimen.padding_medium), end = dimensionResource(R.dimen.padding_medium))
                )
                is ResourceState.Error -> ProfileCardError()
                is ResourceState.Success -> {
                    ProfileCard(
                        profile = state.data,
                        onAchievementDetailClick = onAchievementDetailClick,
                        onShowAllCouponsClick = onShowAllCouponsClick,
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(R.dimen.padding_medium),
                                start = dimensionResource(R.dimen.padding_medium),
                                end = dimensionResource(R.dimen.padding_medium))
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))

        Text(
            text = stringResource(R.string.offers),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
        val adsUiState = uiState.adsState
        Crossfade(
            targetState = adsUiState,
            animationSpec = tween(durationMillis = 500)
        ) {state ->
            when(state) {
                is ResourceState.Loading -> SkeletonCarousel()
                is ResourceState.Error -> Text(text = "Error")
                is ResourceState.Success<List<Ad>> -> AdMaterial3Carousel(
                    ads = state.data,
                    modifier = Modifier.padding(
                        start =dimensionResource(R.dimen.padding_medium),
                        end = dimensionResource(R.dimen.padding_medium)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))

        Text(
            text = stringResource(R.string.discover),
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium))

        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
        val discoverUiState = uiState.discoveryState
        Crossfade(
            targetState = discoverUiState,
            animationSpec = tween(durationMillis = 500)
        ) {state ->
            when(state) {
                is ResourceState.Loading -> SkeletonCarousel()
                is ResourceState.Error -> Text(text = "Error")
                is ResourceState.Success<List<Ad>> -> DiscoverMaterial3Carousel(
                    posts = state.data,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium), end = dimensionResource(R.dimen.padding_medium)),
                )
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
        Text(
            text = "Never Settle",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height( dimensionResource(R.dimen.height_small) + paddingValues.calculateBottomPadding()))
    }
}

@Composable
fun ProfileCardError(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Text(text = "Error")
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImageSkeleton(
                    modifier = Modifier.size(dimensionResource(R.dimen.profile_image_size))
                )
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(3f)
                ) {

                    Box(
                        modifier = Modifier
                            .height(
                                dimensionResource(R.dimen.profile_image_size) - dimensionResource(
                                    R.dimen.badge_size
                                ) - dimensionResource(R.dimen.padding_small)
                            )
                            .fillMaxWidth()
                            .padding(
                                bottom = dimensionResource(R.dimen.padding_small),
                                start = dimensionResource(R.dimen.padding_small),
                                end = dimensionResource(R.dimen.padding_small)
                            )
                            .clip(RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small))))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .shimmerLoadingAnimation()
                    )
                        BadgesRowSkeleton()

                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.badge_size))
                        .clip(MaterialShapes.Sunny.toShape())
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cake_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Text(
                    text = "dd-mm-yyyy",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = Color.Transparent,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerLow,
                            shape = RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small)))
                        )
                        .shimmerLoadingAnimation()
                    )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))
            MembershipTierProgress(points = 0)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
            CouponHorizontalListSkeleton()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProfileCard(
    profile: UserProfile,
    onAchievementDetailClick: () -> Unit,
    onShowAllCouponsClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
    ) {

        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_profile_card))) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(
                    imageUrl =  profile.profilePictureUrl ,
                    modifier = Modifier.size(dimensionResource(R.dimen.profile_image_size))
                )
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small))
                ) {
                    Text(
                        text =  profile.username ,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                    )
                    BadgesRow(
                        achievements = profile.achievements,
                        onAchievementDetailClick = onAchievementDetailClick,
                        modifier = Modifier.fillMaxWidth()
                    )


                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(dimensionResource(id = R.dimen.badge_size))
                        .clip(MaterialShapes.Sunny.toShape())
                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.cake_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_small)))

                Text(
                    text = stringResource(R.string.birthday,  profile.birthday ),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,

                )
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))
            MembershipTierProgress(points = profile.redExpPoints)
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
            CouponHorizontalList(
                coupons = profile.wallet,
                onShowAllCouponsClick = onShowAllCouponsClick,
                modifier = Modifier.fillMaxWidth()
            )


        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SkeletonCarousel(
    modifier: Modifier = Modifier,
    itemSpacing: Dp = dimensionResource(R.dimen.padding_small),
    bottomPadding: Dp = 0.dp,
) {
    val state = rememberCarouselState(initialItem = 0, itemCount = { 3 })
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalCenteredHeroCarousel(
            state = state,
            content = { CarouselItemSkeleton() },
            modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small))),
            itemSpacing = itemSpacing
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
        DynamicCarouselIndicator(3 , 0)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdMaterial3Carousel(
    ads: List<Ad>,
    modifier: Modifier = Modifier,
    itemSpacing: Dp = dimensionResource(R.dimen.padding_small)
) {

    val state = rememberCarouselState(initialItem = 0, itemCount = { ads.size })
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalCenteredHeroCarousel(
            state = state,
            content = { HeroAd(ad = ads[it]) },
            modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small))),
            itemSpacing = itemSpacing
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
        DynamicCarouselIndicator(ads.size, state.currentItem)
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverMaterial3Carousel(
    posts: List<Ad>,
    modifier: Modifier = Modifier,
    bottomPadding: Dp = 0.dp,
    itemSpacing: Dp = dimensionResource(R.dimen.padding_small)
) {
    val state = rememberCarouselState(initialItem = posts.size / 2, itemCount = { posts.size })
    Column(
        modifier = Modifier.padding(bottom = bottomPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalCenteredHeroCarousel(
            state = state,
            content = { DiscoverCard(post = posts[it]) },
            modifier = modifier.clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small))),
            itemSpacing = itemSpacing
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
        DynamicCarouselIndicator(posts.size, state.currentItem)
    }
}











@Composable
fun DiscoverCard(post: Ad, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth()
        .aspectRatio(1.45f)
    )
         {
        Box(
            modifier = Modifier.fillMaxSize() // Box fills the Card
        ) {

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(post.adImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = post.description,
                contentScale = ContentScale.Crop,
                modifier = modifier.fillMaxSize(),
                loading = {
                    Box(
                        modifier = modifier
                            .profileImageModifier()
                            .shimmerLoadingAnimation()
                    )
                },
                error = {errorState ->
                    Log.e("ProfileImageError", "Image loading failed for URL: ${post.adImageUrl}", errorState.result.throwable)

                    Box(
                        modifier = modifier
                            .profileImageModifier()
                            .background(MaterialTheme.colorScheme.surfaceVariant), // Simple background for error
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Image loading failed",
                            tint = MaterialTheme.colorScheme.onSurface,
                            // Adjust icon size
                        )
                    }
                }
            )

            val roundedCornerShape: Shape = RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small)))
            FrostedGlassBox(
                shape = roundedCornerShape,
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                Text(
                    text = post.description,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }


        }
    }
}




@Composable
fun CarouselItemSkeleton(
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.aspectRatio(1.5f)
    ) {
        Box(
            modifier = Modifier
                .carouselElementModifier()
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small)))
                )
                .fillMaxSize()
                .shimmerLoadingAnimation()
        )
        Text(
            text = "Loading...",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Transparent,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
        )
    }
}

@Preview
@Composable
fun CarouselItemSkeletonPreview() {
    RedCableClubTheme {
        Surface {
            CarouselItemSkeleton()
        }
    }
}


@Composable
fun AdCard(ad: Ad, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier) {

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(ad.adImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = ad.description,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(1.66f),
            loading = {
                Box(
                    modifier = modifier
                        .profileImageModifier()
                        .shimmerLoadingAnimation()
                )
            },
            error = {errorState ->
                Log.e("ProfileImageError", "Image loading failed for URL: $ad.adImageUrl", errorState.result.throwable)

                Box(
                    modifier = modifier
                        .profileImageModifier()
                        .background(MaterialTheme.colorScheme.surfaceVariant), // Simple background for error
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Image loading failed",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxSize()
                        // Adjust icon size
                    )
                }
            }
        )
        Text(text = ad.description, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
    }
}

@Composable
fun HeroAd(ad: Ad, modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .clip(RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small))))) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(ad.adImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = ad.description,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .carouselElementModifier(),
            loading = {
                Box(
                    modifier = modifier
                        .profileImageModifier()
                        .shimmerLoadingAnimation()
                )
            },
            error = { errorState ->
                Log.e(
                    "ProfileImageError",
                    "Image loading failed for URL: $ad.adImageUrl",
                    errorState.result.throwable
                )

                Box(
                    modifier = modifier
                        .profileImageModifier()
                        .background(MaterialTheme.colorScheme.surfaceVariant), // Simple background for error
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "Image loading failed",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxSize()
                        // Adjust icon size
                    )
                }
            }
        )

        val roundedCornerShape: Shape = RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small)))
        FrostedGlassBox(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            shape = roundedCornerShape
        ){
            Text(
                text = ad.description,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CouponHorizontalListSkeleton(modifier: Modifier = Modifier) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    Row(
        horizontalArrangement = Arrangement.spacedBy(smallPadding),
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_small))
            .fillMaxWidth() // Ensure Row takes full width for arrangement
    ) {
        for (i in 1..3) {
            CouponElementSkeleton( modifier = Modifier.weight(1f))
        }
        val iconSize = (dimensionResource(R.dimen.coupon_size).value * 1.4).dp
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .weight(1f) // Distribute space equally
                .widthIn(max = iconSize * 1.5f) // Constrain width
        ) {
            RotatingBackgroundButton(
                enabled = false,
                onClick = {},
                modifier = Modifier.size(iconSize),
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.coupon_icon),
                        contentDescription = stringResource(R.string.show_all),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                shape = MaterialShapes.Cookie12Sided.toShape(),
                backgroundColor = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
            Text(
                text = stringResource(R.string.show_all),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.widthIn(max = iconSize * 1.5f),
                textAlign = TextAlign.Center // Center the text for better balance
            )
        }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CouponHorizontalList(
    coupons: List<Coupon>,
    onShowAllCouponsClick: () -> Unit,
    modifier: Modifier = Modifier) {
    val smallPadding = dimensionResource(R.dimen.padding_small)

    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        modifier = modifier
            .fillMaxWidth() // Ensure Row takes full width for arrangement
    ) {
        // Display up to 3 coupons
        coupons.take(3).forEach { coupon ->
            CouponElement(
                coupon = coupon,
                modifier = Modifier.weight(1f) // Distribute space equally
            )
        }


            val iconSize = (dimensionResource(R.dimen.coupon_size).value * 1.4).dp
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f) // Distribute space equally
            ) {
               RotatingBackgroundButton(
                   onClick = { onShowAllCouponsClick() },
                   modifier = Modifier.size(iconSize),
                   icon = {
                       Icon(
                           painter = painterResource(R.drawable.coupon_icon),
                           contentDescription = stringResource(R.string.show_all),
                           tint = MaterialTheme.colorScheme.onPrimary
                       )
                   },
                   shape = MaterialShapes.Cookie12Sided.toShape(),
                   backgroundColor = MaterialTheme.colorScheme.primary
               )

                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
                Text(
                    text = stringResource(R.string.show_all),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.widthIn(max = iconSize * 1.5f),
                    textAlign = TextAlign.Center // Center the text for better balance
                )
            }

    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CouponElementSkeleton(
    modifier: Modifier = Modifier,
    icon: ImageVector = Icons.Filled.LocalOffer,
    iconSize: Dp = dimensionResource(R.dimen.coupon_size)
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
                .background(MaterialTheme.colorScheme.surfaceContainerLow, CircleShape) // Add a subtle background
                .shimmerLoadingAnimation()
        ) {

        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.padding_extra_small)))
        Text(
            text = "Coupons are loading...",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Transparent,
            modifier = Modifier
                .widthIn(max = iconSize * 1.5f)
                .background(
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    shape = RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small)))
                )
                .shimmerLoadingAnimation()

        )

    }
}



@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MembershipTierProgress(
    points: Int,
    modifier: Modifier = Modifier,
    cableThickness: Dp = 16.dp,

) {
    val actualProgress = remember(points) { MembershipTier.computeProgressToNextTier(points) }
    var animationTarget by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(actualProgress) {
        animationTarget = actualProgress
    }

    val animatedProgress by animateFloatAsState(
        targetValue = animationTarget,
        label = "progressAnimation",
        animationSpec = tween(durationMillis = 800)
    )


    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = MembershipTier.getTierForPoints(points).label,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = points.toString(),
                style = MaterialTheme.typography.titleSmall
            )
        }
        LinearWavyProgressIndicator(
            progress = {animatedProgress},
            modifier = Modifier.fillMaxWidth(),
            stroke = Stroke(width = cableThickness.value, cap = StrokeCap.Round),
            waveSpeed = 5.dp
        )

    }
}


@Composable
fun BadgesRowSkeleton(
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.badge_size),
    ) {
    Row(horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(3) {
            Box(
                modifier =
                    Modifier.size(size)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)
                        .shimmerLoadingAnimation()

            )
        }
        FilledIconButton(onClick = { /*TODO*/ }, enabled = false) {
            Icon(imageVector =Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}



@Composable
fun BadgesRow(
    achievements: List<Achievement>,
    onAchievementDetailClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.badge_size),
    ) {
    Row(horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically, modifier = modifier) {


        for( achievement in achievements.filter{it -> it.isAchieved}.take(3)) {

            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(achievement.iconUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = stringResource(R.string.profile_image),
                modifier = Modifier.size(size),
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
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier
                                .size(size)
                        )
                    }
                }
            )
        }
        FilledIconButton(onClick = onAchievementDetailClick, modifier = Modifier.size(size)) {
            Icon(imageVector =Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
fun Modifier.profileImageModifier(
    size: Dp = dimensionResource(R.dimen.profile_image_size),
    borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.primary,
): Modifier {
    return this
        .size(size)
        .clip(CircleShape)
        .border(borderWidth, borderColor, CircleShape)
}

@Composable
fun Modifier.carouselElementModifier(): Modifier {
    return this
        .clip(RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small))))
        .fillMaxWidth()
        .aspectRatio(1.45f)
}

@Composable
fun ProfileImageSkeleton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .profileImageModifier()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .shimmerLoadingAnimation(isLoading = true) // Apply shimmer
    )
}



@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier,
) {


    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = stringResource(R.string.profile_image),
    modifier = modifier.profileImageModifier(),
        loading = {
            Box(
                modifier = modifier
                    .profileImageModifier()
                    .shimmerLoadingAnimation()
            )
        },
        error = {errorState ->
            Log.e("ProfileImageError", "Image loading failed for URL: $imageUrl", errorState.result.throwable)

            Box(
                modifier = modifier
                    .profileImageModifier()
                    .background(MaterialTheme.colorScheme.surfaceVariant), // Simple background for error
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Clear,
                    contentDescription = "Image loading failed",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                     // Adjust icon size
                )
            }
        }
    )


}



/*
@Preview
@Composable
fun AdCarouselPreview() {
    RedCableClubTheme(dynamicColor = false) {
        Surface {
            AdCarousel(ads = FakeAdRepository().ads)
        }
    }
}

 */

@Preview
@Composable
fun SkeletonCarouselPreview() {
    RedCableClubTheme(dynamicColor = false) {
        Surface {
            SkeletonCarousel()
        }
    }
}



@Preview
@Composable
fun AdMaterial3CarouselPreview() {
    RedCableClubTheme(dynamicColor = false) {
        Surface {
            AdMaterial3Carousel(
                ads = RedCableClubApiServiceMock.ads
            )
        }
    }
}


@Preview
@Composable
fun RedCableClubPreview() {
    RedCableClubTheme(dynamicColor = false, darkTheme = false) {
        Surface {
            RedCableClub(
                onAchievementDetailClick = {},
                onShowAllCouponsClick = {},
                uiState = RedCableClubUiState(
                    adsState = ResourceState.Success(RedCableClubApiServiceMock.ads),
                    discoveryState = ResourceState.Success(RedCableClubApiServiceMock.discoverPosts),
                    userProfileState = ResourceState.Success(RedCableClubApiServiceMock.userMock)
                )
            )
        }
    }
}

@Preview
@Composable
fun ProfileCardPreview() {
    RedCableClubTheme(dynamicColor = false, darkTheme = true) {
        ProfileCard(
            profile = RedCableClubApiServiceMock.userMock,
            onAchievementDetailClick = {},
            onShowAllCouponsClick = {}
        )
    }
}

@Preview
@Composable
fun ProfileCardPreviewLight() {
    RedCableClubTheme(dynamicColor = false) {
        ProfileCard(
            profile = RedCableClubApiServiceMock.userMock,
            onAchievementDetailClick = {},
            onShowAllCouponsClick = {}
        )
    }
}

@Preview
@Composable
fun ProfileCardLoadingPreview() {
    RedCableClubTheme(dynamicColor = false) {
        ProfileCardSkeleton()
    }

}
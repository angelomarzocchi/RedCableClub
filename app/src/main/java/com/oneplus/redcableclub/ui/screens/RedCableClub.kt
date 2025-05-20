package com.oneplus.redcableclub.ui.screens

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import com.oneplus.redcableclub.ui.navigation.NavigationDestination
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.theme.gold
import com.oneplus.redcableclub.ui.utils.ResourceState
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation
import kotlin.math.absoluteValue
import kotlin.math.sin

object RedCableClubDestination: NavigationDestination {
    override val route = "redcableclub"
}

@Composable
fun RedCableClub(
    uiState: RedCableClubUiState,
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    ) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = dimensionResource(R.dimen.padding_small),
                end = dimensionResource(R.dimen.padding_small)
            )
            .verticalScroll(scrollState)

    ) {

        val userProfileUiState = uiState.userProfileState
        when(userProfileUiState) {
            is ResourceState.Loading -> ProfileCardSkeleton()
            is ResourceState.Error -> ProfileCardError()
            is ResourceState.Success -> {
                ProfileCard(
                    profile = userProfileUiState.data
                )
            }
        }


        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))

        Text(text = stringResource(R.string.offers), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
       // AdCarousel(ads = FakeAdRepository().ads)

        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))

        Text(text = stringResource(R.string.discover), style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
      /*  DiscoverPostCarousel(
            posts = FakeAdRepository().discoverPosts,
            bottomPadding = paddingValues.calculateBottomPadding(),
        )

       */

    }
}

@Composable
fun ProfileCardError(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Text(text = "Error")
    }
}

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
                ProfileImageSkeleton()
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(3f)
                ) {

                    Box(
                        modifier = Modifier
                            .height(dimensionResource(R.dimen.profile_image_size) -  dimensionResource(R.dimen.badge_size) -  dimensionResource(R.dimen.padding_small))
                            .fillMaxWidth()
                            .padding(bottom =dimensionResource(R.dimen.padding_small), start = dimensionResource(R.dimen.padding_small), end = dimensionResource( R.dimen.padding_small))
                            .clip(RoundedCornerShape(corner = CornerSize(dimensionResource(R.dimen.padding_small))))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)
                            .shimmerLoadingAnimation()
                    )
                        BadgesRowSkeleton()

                }
            }
        }
    }
}

@Composable
fun ProfileCard(
    profile: UserProfile,
    modifier: Modifier = Modifier) {
    Card(
        modifier = modifier) {

        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(
                    imageUrl =  profile.profilePictureUrl ,
                    modifier = Modifier.size(dimensionResource(R.dimen.profile_image_size))
                )
                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(3f)
                ) {
                    Text(
                        text =  profile.username ,
                        style = MaterialTheme.typography.headlineLarge,
                        textAlign = TextAlign.Center,
                    )
                    BadgesRow(
                        achievements = profile.achievements
                    )

                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_medium)))
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.cake_icon),
                    contentDescription = null)
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
                coupons = profile.wallet
            )


        }
    }
}





@Composable
fun AdCarousel(ads: List<Ad>,
               itemSpacing: Dp = dimensionResource(R.dimen.padding_small),
               modifier: Modifier = Modifier) {
    Carousel(
        items = ads,
        itemContent = { AdCard(ad = it) },
        itemSpacing = itemSpacing,
        modifier = modifier
    )




    }

@Composable
fun DiscoverPostCarousel(
    posts: List<Ad>,
    bottomPadding: Dp = 0.dp,
    modifier: Modifier = Modifier) {
    Carousel(
        items = posts,
        itemContent = { DiscoverCard(post = it) },
        itemSpacing = dimensionResource(R.dimen.padding_small),
        bottomPadding = bottomPadding,
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> Carousel(
    items: List<T>,
    itemContent: @Composable (item: T) -> Unit,
    itemSpacing: Dp = dimensionResource(R.dimen.padding_small),
    bottomPadding:Dp = 0.dp,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }
    // 1. Use a very large number for the virtual page count
    val virtualCount = Int.MAX_VALUE

    // 2. Calculate a starting page in the middle of the virtual range
    //    that maps correctly to the first actual item (index 0).
    val startPage = (virtualCount / 2) - ((virtualCount / 2).mod(items.size))

    // 3. Remember PagerState with the virtual count and calculated start page
    val pagerState = rememberPagerState(
        initialPage = startPage,
        pageCount = { virtualCount }
    )
    val pattern = longArrayOf(20,30,20)

    LaunchedEffect(pagerState.settledPage) {
        if (pagerState.currentPage == pagerState.settledPage) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern,-1))

        }
    }


    Column {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = dimensionResource(R.dimen.padding_large) + itemSpacing / 2),

            pageSpacing = itemSpacing
        ) { virtualPageIndex ->

            val actualIndex = virtualPageIndex.mod(items.size)
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


            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                    }
            ) {
                itemContent(items[actualIndex])
            }
        }
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.height_small)))
       /* CarouselIndicator(
            items.size,
            pagerState.currentPage.mod(items.size),
            modifier = Modifier.padding(
                bottom = bottomPadding
            )
        )

        */
        DynamicCarouselIndicator(
            size = items.size,
            currentPage = pagerState.currentPage.mod(items.size),
            modifier = Modifier.padding(
                bottom = bottomPadding
            )
        )
    }
}


@Composable
fun CarouselIndicator(size: Int, currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(size) { iteration ->
            val color =
                if (currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
            Box(
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_mini))
                    .clip(CircleShape)
                    .background(color)
                    .size(dimensionResource(R.dimen.height_medium))
            )
        }
    }
}

@Composable
fun DynamicCarouselIndicator(
    size: Int, // Total number of items
    currentPage: Int,
    maxVisibleIndicators: Int = 7, // Maximum number of indicators to show (including current)
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    inactiveColor: Color = Color.LightGray,
    indicatorSize: Dp = 8.dp,
    indicatorPadding: Dp = 4.dp,
    ellipsisText: String = "...",
    ellipsisColor: Color = Color.Gray
) {
    Row(
        modifier = modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically // Align text vertically
    ) {
        // Calculate the range of indicators to display
        val startIndex = kotlin.math.max(0, currentPage - maxVisibleIndicators / 2)
        val endIndex = kotlin.math.min(size - 1, startIndex + maxVisibleIndicators - 1)
        if (startIndex > 0) {
            for(i in kotlin.math.min(3,startIndex) downTo 1) {
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
            val color = if (currentPage == i) activeColor else inactiveColor
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
            for(i in 1..kotlin.math.min(3,size - 1 - endIndex)) {
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

fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + fraction * (stop - start)
}

@Composable
fun DiscoverCard(post: Ad, modifier: Modifier = Modifier) {
    Card(modifier = modifier.aspectRatio(1.5f)) {
        Box(
            modifier = Modifier.fillMaxSize() // Box fills the Card
        ) {
            // Image fills the Box and is squared
            /*
            Image(
                painter = painterResource(id = post.adImageUrl),
                contentDescription = post.description,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

             */

            // Text overlay
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // Position text at the bottom
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.5f)) // Semi-transparent dark background for readability
                    .padding(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = post.description,
                    color = Color.White, // White text for contrast against the dark background
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2, // Limit text lines to prevent overflow
                    overflow = TextOverflow.Ellipsis // Add ellipsis if text is too long
                )
            }
        }
    }
}





@Composable
fun AdCard(ad: Ad, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier) {
        /*
        Image(
            painter = painterResource(id = ad.adImageId),
            contentDescription = ad.description,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )

         */
        Text(text = ad.description, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
    }

}

@Composable
fun CouponHorizontalList(coupons: List<Coupon>,modifier: Modifier = Modifier) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        modifier = modifier.padding(dimensionResource(R.dimen.padding_small))
    ) {

        items(coupons.size) { index ->
            CouponElement(
                coupon = coupons[index]
            )
        }
    }
}

@Composable
fun CouponElement(
    coupon: Coupon,
    borderColor: Color = MaterialTheme.colorScheme.gold,
    borderWidth: Dp = dimensionResource(R.dimen.padding_mini),
    paddingAroundIcon: Dp = dimensionResource(R.dimen.padding_small),
    iconSize: Dp = dimensionResource(R.dimen.coupon_size),
    modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(iconSize * 1.5f)) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(iconSize)
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = CircleShape
                )
                .padding(paddingAroundIcon)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.coupon_icon),
                contentDescription = coupon.description,
                modifier = Modifier.size(iconSize),
                tint = MaterialTheme.colorScheme.gold
            )
        }
        Text(
            text = coupon.description,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.widthIn(max = iconSize * 1.5f)
        )
    }
}

@Composable
fun MembershipTierProgress(
    points: Int,
    progressColor: Color = ProgressIndicatorDefaults.linearColor,
    toCompleteColor: Color = ProgressIndicatorDefaults.linearTrackColor,
    cableThickness: Dp = 8.dp,
    twists: Int = 3,
    modifier: Modifier = Modifier) {
    val progress = MembershipTier.computeProgressToNextTier(points)
    // Animate the progress value
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        label = "progressAnimation"
        // You can customize animation spec here (e.g., tween)
    )


    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = MembershipTier.getTierForPoints(points).label,
                style = MaterialTheme.typography.titleSmall
            )
            Text(
                text = points.toString(),
                style = MaterialTheme.typography.titleSmall
            )
        }
        // LinearProgressIndicator(progress = { MembershipTier.computeProgressToNextTier(points) },modifier = Modifier.fillMaxWidth())

        val density = LocalDensity.current
        val cableThicknessPx = with(density) { cableThickness.toPx() }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(cableThickness * 2) // Give some height for the twists
                .drawBehind {
                    val width = size.width
                    val height = size.height

                    // Create the path for the twisted cable
                    val path = Path()
                    val numSegments = 100 // Increase for smoother twists
                    val segmentWidth = width / numSegments.toFloat()
                    val twistAmplitude = height / 4f // Adjust amplitude of the twist

                    path.moveTo(0f, height / 2f) // Start at the left center

                    for (i in 0..numSegments) {
                        val x = i * segmentWidth
                        val y =
                            height / 2f + sin((i.toFloat() / numSegments.toFloat()) * twists * 2 * Math.PI).toFloat() * twistAmplitude // Use sine wave for twist
                        path.lineTo(x, y)
                    }

                    // Draw the entire path with the uncompleted color (background)
                    drawPath(
                        path = path,
                        color = toCompleteColor,
                        style = Stroke(width = cableThicknessPx, cap = StrokeCap.Round)
                    )

                    // Draw the completed portion of the path with the progress color (foreground)
                    // We need to create a new path that only goes up to the animated progress
                    val progressPath = Path()
                    progressPath.moveTo(0f, height / 2f)

                    for (i in 0..numSegments) {
                        val x = i * segmentWidth
                        val y =
                            height / 2f + sin((i.toFloat() / numSegments.toFloat()) * twists * 2 * Math.PI).toFloat() * twistAmplitude

                        if (x <= animatedProgress * width || i == numSegments) {
                            progressPath.lineTo(x, y)
                        } else {
                            // Move to the current point to avoid drawing a line to the end
                            progressPath.moveTo(x, y)
                        }
                    }

                    drawPath(
                        path = progressPath,
                        color = progressColor,
                        style = Stroke(width = cableThicknessPx, cap = StrokeCap.Round)
                    )

                })


    }
}


@Composable
fun BadgesRowSkeleton(size: Dp = dimensionResource(R.dimen.badge_size),spacing: Dp = dimensionResource(R.dimen.padding_small), modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(
        space = spacing,
        alignment = Alignment.CenterHorizontally
    ), verticalAlignment = Alignment.CenterVertically, modifier = modifier)  {
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
fun BadgesRow(achievements: List<Achievement>, size: Dp = dimensionResource(R.dimen.badge_size),spacing: Dp = dimensionResource(R.dimen.padding_small), modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(
        space = spacing,
        alignment = Alignment.CenterHorizontally
    ), verticalAlignment = Alignment.CenterVertically, modifier = modifier) {


        for( achievement in achievements.take(3)) {

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
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .size(size)
                        )
                    }
                }
            )
        }
        FilledIconButton(onClick = { /*TODO*/ }) {
            Icon(imageVector =Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }


    }
}

@Composable
fun Modifier.profileImageModifier(
    size: Dp = dimensionResource(R.dimen.profile_image_size),
     borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.secondary): Modifier {
    return this.size(size).clip(CircleShape).border(borderWidth, borderColor, CircleShape)
}

@Composable
fun ProfileImageSkeleton(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.profileImageModifier()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .shimmerLoadingAnimation(isLoading = true) // Apply shimmer
    )
}



@Composable
fun ProfileImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
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
                modifier = modifier.profileImageModifier()
                    .shimmerLoadingAnimation()
            )
        },
        error = {errorState ->
            Log.e("ProfileImageError", "Image loading failed for URL: $imageUrl", errorState.result.throwable)

            Box(
                modifier = modifier.profileImageModifier()
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
fun RedCableClubPreview() {
    RedCableClubTheme(dynamicColor = false) {
        Surface {
            RedCableClub(modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@Preview
@Composable
fun AdCarouselPreview() {
    RedCableClubTheme(dynamicColor = false) {
        Surface {
            AdCarousel(ads = FakeAdRepository().ads)
        }
    }
}

@Preview
@Composable
fun DiscoverCarouselPreview() {
    RedCableClubTheme(dynamicColor = false) {
        Surface {
            DiscoverPostCarousel(
            posts = FakeAdRepository().discoverPosts
            )
        }
    }
}

 */

/*
@Preview
@Composable
fun DiscoverCardPreview() {
    RedCableClubTheme(dynamicColor = false) {
        Surface {
            DiscoverCard(post = FakeAdRepository().discoverPosts[1])
        }
    }
}

@Preview
@Composable
fun AdCardPreview() {
    RedCableClubTheme {
        Surface {
            AdCard(ad = FakeAdRepository().ads[0], modifier = Modifier.padding(dimensionResource(R.dimen.padding_small)))
        }
    }
}

@Preview
@Composable
fun CouponPreview() {
    RedCableClubTheme {
        Surface {
            CouponElement(coupon = FakeUserProfileRepository().fakeUserProfile.wallet[0])
        }
    }
}

@Preview
@Composable
fun ProfileImagePreview() {
    RedCableClubTheme {
        ProfileImage(imageResourceId = R.drawable.userprofile_image)
    }
}

 */

@Preview
@Composable
fun ProfileCardPreview() {
    RedCableClubTheme(dynamicColor = false, darkTheme = true) {
        ProfileCard(profile = RedCableClubApiServiceMock.userMock)
    }
}

@Preview
@Composable
fun ProfileCardPreviewLight() {
    RedCableClubTheme(dynamicColor = false) {
        ProfileCard(profile = RedCableClubApiServiceMock.userMock)
    }
}

@Preview
@Composable
fun ProfileCardLoadingPreview() {
    RedCableClubTheme(dynamicColor = false) {
        ProfileCardSkeleton()
    }

}
package com.oneplus.redcableclub.ui.utils

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Product
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductImageCarousel(
    imageUrls: List<String>,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.product_image_size),
    borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    innerBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
) {

    if(imageUrls.isEmpty()) {
        return
    }

    val pagerState = rememberPagerState(pageCount = {imageUrls.size})

    Box(
        modifier =modifier
            .aspectRatio(1f)
            .size(size)
            .clip(CircleShape)
            .background(innerBackgroundColor)
            .border(borderWidth, borderColor, CircleShape)
            .padding(dimensionResource(R.dimen.padding_large))
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {page ->
                val currentImageUrl = imageUrls[page]
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(currentImageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = contentDescription,
                    loading = {
                        Box(
                            modifier = modifier
                                .size(size)
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .shimmerLoadingAnimation()
                        )
                    }
                )

            }
            if(imageUrls.size > 1) {
                DynamicCarouselIndicator(
                    size = imageUrls.size,
                    currentPage = pagerState.currentPage
                )
            }
        }

    }

}

@Preview
@Composable
fun ProductImagePreview() {
    val product = RedCableClubApiServiceMock.storeProducts[0] as Product
    RedCableClubTheme {
        Surface {
            ProductImageCarousel(
                imageUrls = product.imageUrls,
                contentDescription =  product.name
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductImageLoading(
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.product_image_size),
    borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    innerBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
) {

    Box(
        modifier =modifier
            .aspectRatio(1f)
            .size(size)
            .clip(CircleShape)
            .background(innerBackgroundColor)
            .shimmerLoadingAnimation()
            .border(borderWidth, borderColor, CircleShape)
            .padding(dimensionResource(R.dimen.padding_large))
    ) {}
}

@Preview
@Composable
fun ProductImageLoadingPreview() {
    RedCableClubTheme {
        Surface {
            ProductImageLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductBenefitIcon(
    iconText: String,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.product_image_size),
    borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    innerBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(size)
            .clip(MaterialShapes.Pill.toShape())
            .background(innerBackgroundColor)
            .border(borderWidth, borderColor, MaterialShapes.Pill.toShape())
            .padding(dimensionResource(R.dimen.padding_large)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = iconText,
            color = MaterialTheme.colorScheme.onPrimary,
            fontWeight = FontWeight.Bold,
            autoSize = TextAutoSize.StepBased(),
            maxLines = 1
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun ProductBenefitIconPreview() {
    RedCableClubTheme {
        Surface {
            ProductBenefitIcon(
                iconText = "-10%"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductBenefitIconLoading(
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.product_image_size),
    borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    innerBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .size(size)
            .clip(MaterialShapes.Pill.toShape())
            .background(innerBackgroundColor)
            .shimmerLoadingAnimation()
            .border(borderWidth, borderColor, MaterialShapes.Pill.toShape())
            .padding(dimensionResource(R.dimen.padding_large))
        ,
        contentAlignment = Alignment.Center
    ) {

    }

}



@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun ProductBenefitIconLoadingPreview() {
    RedCableClubTheme {
        Surface {
            ProductBenefitIconLoading(
            )
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RedCoins(
    redCoins: Int,
    modifier : Modifier = Modifier) {

        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small)) // Adjusted spacing
        ) {
            Icon(
                painter = painterResource(id = R.drawable.red_coins_icon),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
                modifier = Modifier.size(dimensionResource(R.dimen.red_coins_product_card_size))
            )
            Text(
                text = "$redCoins",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize)
            )
        }

}

/**
 * A utility object that serves as a single source of truth for generating
 * the keys used in shared element transitions between the shop grid and the detail screen.
 * Using this object prevents typos and ensures consistency.
 */
object SharedElementKeys {

    private const val PREFIX_IMAGE = "product_image_"
    private const val PREFIX_NAME = "product_name_"
    private const val PREFIX_KSP = "ksp_"

    /**
     * Generates the unique key for a product's image or benefit icon.
     * @param contentKey The unique identifier of the ShopItem (e.g., product name).
     */
    fun getImageKey(contentKey: String): String = "$PREFIX_IMAGE$contentKey"

    /**
     * Generates the unique key for a product's name text.
     * @param contentKey The unique identifier of the ShopItem (e.g., product name).
     */
    fun getNameKey(contentKey: String): String = "$PREFIX_NAME$contentKey"

    fun getKspKey(contentKey: String): String = "$PREFIX_KSP$contentKey$"
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewWithTransitionScopes(
    content: @Composable SharedTransitionScope.(AnimatedVisibilityScope) -> Unit
) {
    SharedTransitionLayout {
        // We use a simple AnimatedContent to provide the AnimatedVisibilityScope.
        // The lambda for AnimatedContent provides the target state as a parameter.
        // Since we don't use it, we name it `_` to suppress the warning.
        androidx.compose.animation.AnimatedContent(
            targetState = true,
            label = "preview"
        ) { _ ->
            content(this@SharedTransitionLayout, this@AnimatedContent)
        }
    }
}
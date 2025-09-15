package com.oneplus.redcableclub.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.ShopItem
import com.oneplus.redcableclub.data.model.Product
import com.oneplus.redcableclub.data.model.Benefit
import com.oneplus.redcableclub.data.model.KeySellingPoint
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.utils.PreviewWithTransitionScopes
import com.oneplus.redcableclub.ui.utils.ProductBenefitIcon
import com.oneplus.redcableclub.ui.utils.ProductImageCarousel
import com.oneplus.redcableclub.ui.utils.RedCoins

/**
 * Displays the detailed view of a [ShopItem] with shared element transitions.
 * This screen acts as the **destination** for the animations starting from [RedCoinsShop].
 *
 * It uses the [contentKey] to construct unique keys via [com.oneplus.redcableclub.ui.utils.SharedElementKeys] that must match
 * the source elements in [ProductCard] for a seamless transition.
 *
 * @param sharedTransitionScope The scope for coordinating shared element transitions.
 * @param animatedVisibilityScope The scope that defines the visibility of the animated elements.
 * @param shopItem The product or benefit to display details for.
 * @param contentKey The unique identifier for the [shopItem] (e.g., product name), used to
 * create matching transition keys. This must be the same key passed from the source screen.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RedCoinsProductDetail(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    shopItem: ShopItem,
    contentKey: String,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium))
    ) {
        with(sharedTransitionScope) {
            when (shopItem) {
                is Product -> {
                    ProductImageCarousel(
                        imageUrls = shopItem.imageUrls,
                        contentDescription = shopItem.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElement(
                                rememberSharedContentState(key = "product_image_$contentKey"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
                is Benefit -> {
                    ProductBenefitIcon(
                        iconText = shopItem.iconText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElement(
                                rememberSharedContentState(key = "product_image_$contentKey"),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .sharedElement(
                        rememberSharedContentState(key = "product_name_$contentKey"),
                        animatedVisibilityScope = animatedVisibilityScope
                    )
                    .padding(vertical = dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    text = shopItem.name,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            RedCoins(shopItem.redCoinsRequired)
            if(shopItem is Product) {
                Text(
                    text = " + ", // Added separator
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_extra_small)),
                    autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize)
                )
                Text(
                    text = stringResource(R.string.price, shopItem.price),
                    style = MaterialTheme.typography.titleMedium,
                    autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize)
                )// Matched style for consistency
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun RedCoinsProductDetailPreview() {
    RedCableClubTheme {
        Surface {
            PreviewWithTransitionScopes { animatedVisibilityScope ->
                RedCoinsProductDetail(
                    sharedTransitionScope = this,
                    animatedVisibilityScope = animatedVisibilityScope,
                    shopItem = RedCableClubApiServiceMock.storeProducts.first(),
                    contentKey = RedCableClubApiServiceMock.storeProducts.first().name
                )
            }
        }
    }
}

@Composable
fun KeySellingPoint(
    keySellingPoint: KeySellingPoint,
    isExpanded: Boolean,
    modifier : Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
    }
}

@Preview
@Composable
fun KeySellingPointPreview() {
    RedCableClubTheme {
        Surface {
            KeySellingPoint(
                keySellingPoint = (RedCableClubApiServiceMock.storeProducts[0] as Product).keySellingPoints[0],
                isExpanded = false
            )
        }
    }
}



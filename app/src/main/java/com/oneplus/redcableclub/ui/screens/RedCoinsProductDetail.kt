package com.oneplus.redcableclub.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Benefit
import com.oneplus.redcableclub.data.model.KeySellingPoint
import com.oneplus.redcableclub.data.model.Product
import com.oneplus.redcableclub.data.model.ShopItem
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
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RedCoinsProductDetail(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    shopItem: ShopItem,
    contentKey: String,
) {

    val orientation = LocalConfiguration.current.orientation

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
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


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    RedCoins(
                        shopItem.redCoinsRequired
                    )
                    if (shopItem is Product) {
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
                        )
                    }
                }
            }
            if (shopItem is Product) {
                var expandedKeySellingPoint by remember { mutableStateOf<Int?>(null) }

                for (i in shopItem.keySellingPoints.indices) {
                    KeySellingPoint(
                        keySellingPoint = shopItem.keySellingPoints[i],
                        isExpanded = expandedKeySellingPoint == i,
                        onToggleExpand = {
                            expandedKeySellingPoint = if (expandedKeySellingPoint == i) null else i
                        },
                        modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
                    )
                }
            }
        }
        else {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                ) {
                    with(sharedTransitionScope) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .sharedElement(
                                    rememberSharedContentState(key = "product_name_$contentKey"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                        ) {
                            Text(
                                text = shopItem.name,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            RedCoins(
                                shopItem.redCoinsRequired
                            )
                            if (shopItem is Product) {
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
                                )
                            }
                        }
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

                    }
                }
                if (shopItem is Product) {
                    var expandedKeySellingPoint by remember { mutableStateOf<Int?>(null) }
                    Column(
                        modifier = Modifier.weight(3f)
                    ) {
                        for (i in shopItem.keySellingPoints.indices) {
                            KeySellingPoint(
                                keySellingPoint = shopItem.keySellingPoints[i],
                                isExpanded = expandedKeySellingPoint == i,
                                onToggleExpand = {
                                    expandedKeySellingPoint =
                                        if (expandedKeySellingPoint == i) null else i
                                },
                                modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small))
                            )
                        }
                    }
                }
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp // Adds a shadow to separate it from the content
        ) {
            Row() {
                if(orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Box(modifier = Modifier.weight(1f))
            }

                Button(
                    onClick = { /* TODO: Implement buy/redeem logic */ },
                    modifier = Modifier
                        .weight(3f)
                        .padding(dimensionResource(R.dimen.padding_medium))
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                    ) {
                        Icon(
                            Icons.Filled.ShoppingCart,
                            contentDescription = stringResource(R.string.buy_now)
                        )
                        Text(text = stringResource(R.string.buy_now))
                    }

                }
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


@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(device = "spec:width=1080px,height=2340px,dpi=440,orientation=landscape")
@Composable
fun RedCoinsProductDetailLandscapePreview() {
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

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun KeySellingPoint(
    keySellingPoint: KeySellingPoint,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconRotation by animateFloatAsState(
        targetValue = if (isExpanded) 180f else 0f,
        label = "iconRotation"
    )

    val roundedCornerDp by animateIntAsState(
        targetValue = if (isExpanded) 8 else 50,
        label = "roundedCornerDp"
    )

    val smallImageAlpha by animateFloatAsState(
        targetValue = if (isExpanded) 0f else 1f,
        label = "smallImageAlpha"
    )

    Surface(
        onClick = onToggleExpand,
        shape = RoundedCornerShape(roundedCornerDp.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
            .fillMaxWidth()
    ) {

            Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
                ) {

                        AsyncImage(
                            model = keySellingPoint.imageUri,
                            contentDescription = null,
                            modifier = Modifier
                                .alpha(smallImageAlpha)
                                .size(48.dp)
                                .clip(CircleShape)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )



                    Text(
                        text = keySellingPoint.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = onToggleExpand) {
                        Icon(
                            imageVector = Icons.Filled.ExpandMore,
                            contentDescription = if (isExpanded) "Collapse" else "Expand",
                            modifier = Modifier.rotate(iconRotation), // Keep rotation for visual flair
                            tint = MaterialTheme.colorScheme.primary // Use primary color for the icon
                        )
                    }
                }

                AnimatedVisibility(visible = isExpanded) {
                    Column(
                        modifier = Modifier.padding(top = dimensionResource(R.dimen.padding_medium))
                    ) {
                        // Full-width Image for expanded state
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = dimensionResource(R.dimen.padding_small))
                                .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))

                        ) {
                            AsyncImage(
                                model = keySellingPoint.imageUri,
                                contentDescription = null, // decorative
                                modifier = Modifier
                                    .fillMaxWidth()
                                ,
                                contentScale = ContentScale.Fit
                            )
                        }
                        Text(
                            text = keySellingPoint.description,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth() // Ensure description fills width
                        )
                    }
                }
            }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun KeySellingPointPreview() {
    RedCableClubTheme {
        Surface {
            PreviewWithTransitionScopes { animatedVisibilityScope ->
                KeySellingPoint(
                    keySellingPoint = (RedCableClubApiServiceMock.storeProducts[0] as Product).keySellingPoints[0],
                    isExpanded = true,
                    onToggleExpand = {},
                )
            }

        }
    }
}

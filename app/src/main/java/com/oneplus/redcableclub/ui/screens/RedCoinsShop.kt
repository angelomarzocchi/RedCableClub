package com.oneplus.redcableclub.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Benefit
import com.oneplus.redcableclub.data.model.Product
import com.oneplus.redcableclub.data.model.ShopItem
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.utils.FaqItem
import com.oneplus.redcableclub.ui.utils.FaqItemViewWithCustomExtension
import com.oneplus.redcableclub.ui.utils.PreviewWithTransitionScopes
import com.oneplus.redcableclub.ui.utils.ProductBenefitIcon
import com.oneplus.redcableclub.ui.utils.ProductBenefitIconLoading
import com.oneplus.redcableclub.ui.utils.ProductImageCarousel
import com.oneplus.redcableclub.ui.utils.ProductImageLoading
import com.oneplus.redcableclub.ui.utils.RedCoins
import com.oneplus.redcableclub.ui.utils.ResourceState
import com.oneplus.redcableclub.ui.utils.SharedElementKeys
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Displays the main Red Coins shop screen, including the user's coin balance and a grid of products.
 * This screen acts as the **source** for the shared element transitions.
 *
 * @param redCoinsShopUiState The state containing the list of shop items.
 * @param redCoins The current number of Red Coins the user has.
 * @param sharedTransitionScope The scope from the parent `SharedTransitionLayout`, required to
 * enable shared element transitions. It is passed down to [ProductCard].
 * @param animatedVisibilityScope The scope from the parent `AnimatedContent` (likely a NavHost or a NavDisplay),
 * required by the `.sharedElement` modifier. It is passed down to [ProductCard].
 * @param onProductClick Lambda triggered when a product is clicked, providing the item and its unique key.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RedCoinsShop(
    redCoinsShopUiState: RedCoinsShopUiState,
    redCoins: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onProductClick: (ShopItem, String) -> Unit,
    modifier: Modifier = Modifier,
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium)),
    ) {
        RedCoinsCard(
            redCoins = redCoins,
            modifier = Modifier
                .padding(vertical = dimensionResource(R.dimen.padding_medium))
        )
        when (redCoinsShopUiState.shopItems) {
            is ResourceState.Success -> {

                ProductCardGridList(
                    products = redCoinsShopUiState.shopItems.data,
                    onProductClick = onProductClick,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = modifier
                )
            }
            is ResourceState.Loading -> {
                ProductCardGridListLoading(modifier = modifier)
            }
            is ResourceState.Error -> {

            }
        }
    }

}



enum class ShopItemFilter {
    ALL, PRODUCTS, BENEFITS
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductCardGridList(
    products: List<ShopItem>,
    onProductClick: (ShopItem, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {

   var currentFilter by remember { mutableStateOf(ShopItemFilter.ALL) }

    val filteredItems = remember(currentFilter, products) {
        when (currentFilter) {
            ShopItemFilter.ALL -> products
            ShopItemFilter.PRODUCTS -> products.filterIsInstance<Product>()
            ShopItemFilter.BENEFITS -> products.filterIsInstance<Benefit>()
        }
    }

    Column(modifier = modifier) {
        ProductFilterSelection(
            selectedFilter = currentFilter,
            onFilterSelected = { currentFilter = it },
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
            modifier = Modifier,

        ) {
            items(filteredItems.size) { index ->
                ProductCard(
                    product = filteredItems[index],
                    onProductClick = onProductClick,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
fun ProductCardGridListLoading(modifier: Modifier = Modifier) {
    Column() {
        ProductFilterSelection(
            selectedFilter = ShopItemFilter.ALL,
            onFilterSelected = { },
            enabled = false
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
            modifier = modifier
        ) {
            items(8) { index ->
                ProductCardLoading(
                    product = if (index % 2 == 0)
                        Product(
                            name = "",
                            redCoinsRequired = 100,
                            price = 10.0,
                            imageUrls = listOf(""),
                            keySellingPoints = listOf()
                        )
                    else
                        Benefit(name = "", redCoinsRequired = 100, description = "", iconText = ""),
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
            }
        }
    }
}

@Composable
fun ProductFilterSelection(
    selectedFilter: ShopItemFilter,
    onFilterSelected: (ShopItemFilter) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val filterOptions = listOf(
        ShopItemFilter.ALL to stringResource(R.string.filter_all), // Replace with actual R.string.filter_all
        ShopItemFilter.PRODUCTS to stringResource(R.string.filter_products), // Replace with actual R.string.filter_products
        ShopItemFilter.BENEFITS to stringResource(R.string.filter_benefits) // Replace with actual R.string.filter_benefits
    )

    SingleChoiceSegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = dimensionResource(R.dimen.padding_small))
    ) {
        filterOptions.forEachIndexed { index, (filter, label) ->
            SegmentedButton(
                enabled = enabled,
                shape = SegmentedButtonDefaults.itemShape(index = index, count = filterOptions.size),
                onClick = { onFilterSelected(filter) },
                selected = selectedFilter == filter,
                icon = {}
            ) {
                Text(label)
            }
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun ProductCardGridListPreview() {
    RedCableClubTheme {
        Surface {
            PreviewWithTransitionScopes { animatedVisibilityScope ->
                ProductCardGridList(
                    products = RedCableClubApiServiceMock.storeProducts,
                    onProductClick = {_,_ -> },
                    sharedTransitionScope = this,
                    animatedVisibilityScope = animatedVisibilityScope
                )
            }
        }
    }
}

/**
 * A card that displays a single [ShopItem] in the grid. It defines the **source** elements
 * for the shared element transition (image/icon and name).
 *
 * @param product The item to display.
 * @param onProductClick Lambda to call when the card is clicked.
 * @param sharedTransitionScope The scope for coordinating the transition.
 * @param animatedVisibilityScope The scope defining the animated container's visibility.
 */
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ProductCard(
    product: ShopItem,
    onProductClick: (ShopItem, String) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        Card(
            onClick = { onProductClick(product, product.name) }, modifier = modifier
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                when (product) {
                    is Product -> ProductImageCarousel(
                        imageUrls = product.imageUrls.subList(0,1),
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = SharedElementKeys.getImageKey(product.name)),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )

                    is Benefit -> ProductBenefitIcon(
                        iconText = product.iconText,
                        modifier = Modifier
                            .fillMaxWidth()
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = SharedElementKeys.getImageKey(product.name)),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth() // Give the container a defined size
                            .sharedElement(
                                rememberSharedContentState(key = SharedElementKeys.getNameKey(product.name)),
                                animatedVisibilityScope = animatedVisibilityScope
                            )
                    ) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center,
                            // The Text itself no longer needs the sharedElement modifier
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_mini)), // Changed from SpaceAround
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RedCoins(product.redCoinsRequired)

                        when (product) {
                            is Product -> {
                                Text(
                                    text = " + ", // Added separator
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_extra_small)),
                                    autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize)
                                )
                                Text(
                                    text = stringResource(R.string.price, product.price),
                                    style = MaterialTheme.typography.titleMedium,
                                    autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize)
                                )// Matched style for consistency

                            }

                            is Benefit -> Spacer(modifier = Modifier) // No price for benefits
                        }
                    }
                }


            }
        }
    }

}

@Composable
fun ProductCardLoading(
    product: ShopItem,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            when(product) {
                is Product -> ProductImageLoading(modifier = Modifier.fillMaxWidth())
                is Benefit -> ProductBenefitIconLoading(modifier = Modifier.fillMaxWidth())
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .clip(RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                        .background(MaterialTheme.colorScheme.surfaceContainerLow)

                        .shimmerLoadingAnimation()
                ) {
                    Text(
                        text = "Loading super duper long product name...",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        textAlign = TextAlign.Center,
                        color = Color.Transparent
                    )
                }
                Column(
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_mini)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_extra_small))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.red_coins_icon),
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null,
                            modifier = Modifier.size(dimensionResource(R.dimen.red_coins_product_card_size))
                        )
                        Box(modifier = Modifier
                            .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                            .background(MaterialTheme.colorScheme.surfaceContainerLow)

                            .shimmerLoadingAnimation()) {
                            Text(
                                text = "1000",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = Color.Transparent,
                                autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize)
                            )
                        }
                    }
                    when (product) {
                        is Product -> {
                            Text(
                                text = " + ", // Added separator
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_extra_small)),
                                autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize)
                            )
                            Box(modifier = Modifier
                                .clip(shape = RoundedCornerShape(dimensionResource(R.dimen.padding_small)))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .shimmerLoadingAnimation()
                            ) {
                                Text(
                                    text = stringResource(R.string.price, product.price),
                                    style = MaterialTheme.typography.titleMedium,
                                    autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.titleMedium.fontSize),
                                    color = Color.Transparent
                                )// Matched style for consistency
                            }

                        }
                        is Benefit -> Spacer(modifier = Modifier) // No price for benefits
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ProductCardLoadingPreview() {
    RedCableClubTheme {
        Surface {
            ProductCardLoading(
                product = Product(name = "", redCoinsRequired = 100, price = 10.0, imageUrls = listOf(""), keySellingPoints = listOf()),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun ProductCardBenefitPreview() {
    val product = Benefit(
        name = "10% off on OnePlus products",
        redCoinsRequired = 100,
        description = "Get 10% off on all OnePlus products when you redeem this coupon.",
        iconText = "-10%"
    )
    RedCableClubTheme {
        Surface {
            PreviewWithTransitionScopes { animatedVisibilityScope ->
                ProductCard(
                    product = product,
                    onProductClick = {_,_ ->},
                    sharedTransitionScope = this,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
            }

        }
    }

}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
fun ProductCardPreview() {

    RedCableClubTheme {
        Surface {
            PreviewWithTransitionScopes { animatedVisibilityScope ->
                ProductCard(
                    product = RedCableClubApiServiceMock.storeProducts[0],
                    onProductClick = {_,_ ->},
                    sharedTransitionScope = this,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
            }
            }
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RedCoinsCard(
    redCoins: Int,
    modifier: Modifier = Modifier,
) {

    val animatedProgress by animateFloatAsState(
        targetValue = 1f,
        label = "progressAnimation",
        animationSpec = tween(durationMillis = 800)
    )

    Card(modifier = modifier) {
        LinearWavyProgressIndicator(
            progress = {animatedProgress},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_medium)),
            stroke = Stroke(width = 16.dp.value, cap = StrokeCap.Round),
            waveSpeed = 0.dp
        )
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.red_coins_icon),
                    tint = MaterialTheme.colorScheme.primary,
                    contentDescription = null,
                    modifier = Modifier.size(MaterialTheme.typography.headlineMedium.fontSize.value.dp)
                )
                Text(
                    text = stringResource(R.string.red_coins, redCoins),
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )
            }
            FaqItemViewWithCustomExtension(
                faqItem = FaqItem(
                    question = "How to earn RedCoins ?",
                    id = 1,
                    answer = ""
                ),
                expandedView = {ExplanationsTab()}
            )




        }


    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplanationsTab(
    modifier: Modifier = Modifier,
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val options = listOf(stringResource(R.string.earn_red_coins), stringResource(R.string.spend_red_coins))

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier.fillMaxSize()
    ) {
        item {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.66f)
                    .padding(dimensionResource(R.dimen.padding_medium))
            ) {
                options.forEachIndexed { index, label ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        onClick = { selectedIndex = index },
                        selected = index == selectedIndex
                    ) {
                        Text(label)
                    }
                }
            }
        }

        item {
            if (selectedIndex == 0) {
                EarnRedCoinsExplanations()
            } else {
                SpendRedCoinsExplanations()
            }
        }
    }
}

@Preview
@Composable
fun ExplanationsTabPreview() {
    RedCableClubTheme {
        Surface {
            ExplanationsTab(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

data class ExplanationItem(
    val position: Int,
    val title: String,
    val body: String,
)

@Composable
fun EarnRedCoinsExplanations(
    modifier: Modifier = Modifier,
) {

    val explanations = listOf(
        ExplanationItem(
            title = stringResource(R.string.earn_explanation_t1),
            body = stringResource(R.string.earn_explanation_b1),
            position = 1
        ),
        ExplanationItem(
            title = stringResource(R.string.earn_explanation_t2),
            body = stringResource(R.string.earn_explanation_b2),
            position = 2
        ),
        ExplanationItem(
            title = stringResource(R.string.earn_explanation_t3),
            body = stringResource(R.string.earn_explanation_b3),
            position = 3
        )
    )

    val equivalenceList = listOf(
        CurrencyEquivalence(1, BigDecimal(1), "€"),
        CurrencyEquivalence(
            1,
            BigDecimal(0.88).setScale(2, RoundingMode.HALF_EVEN),
            "£"
        ),
        CurrencyEquivalence(
            1,
            BigDecimal(10.50).setScale(2, RoundingMode.HALF_EVEN),
            "SEK"
        ),
        CurrencyEquivalence(
            1,
            BigDecimal(7.44).setScale(2, RoundingMode.HALF_EVEN),
            "DKK"
        )
    )

    ExplanationBlock(
        explanations = explanations,
        equivalenceList = equivalenceList,
        redCoinsOrder = EuroRedCoinsOrder.EURO_FIRST,
        modifier = modifier
    )
}

@Composable
fun SpendRedCoinsExplanations(
    modifier: Modifier = Modifier,
) {
    val explanations = listOf(
        ExplanationItem(
            title = stringResource(R.string.spend_explanation_t1),
            body = stringResource(R.string.spend_explanation_b1),
            position = 1
        ),
        ExplanationItem(
            title = stringResource(R.string.spend_explanation_t2),
            body = stringResource(R.string.spend_explanation_b2),
            position = 2
        ),
        ExplanationItem(
            title = stringResource(R.string.spend_explanation_t3),
            body = stringResource(R.string.spend_explanation_b3),
            position = 3
        )
    )

    val equivalenceList = listOf(
        CurrencyEquivalence(75, BigDecimal(1), "€"),
        CurrencyEquivalence(
            85,
            BigDecimal(1).setScale(2, RoundingMode.HALF_EVEN),
            "£"
        ),
        CurrencyEquivalence(
            7,
            BigDecimal(1).setScale(2, RoundingMode.HALF_EVEN),
            "SEK"
        ),
        CurrencyEquivalence(
            10,
            BigDecimal(1).setScale(2, RoundingMode.HALF_EVEN),
            "DKK"
        )
    )

    ExplanationBlock(
        explanations = explanations,
        equivalenceList = equivalenceList,
        redCoinsOrder = EuroRedCoinsOrder.RED_COINS_FIRST,
        modifier = modifier
    )

}

@Preview
@Composable
fun SpendRedCoinsExplanationsPreview() {
    RedCableClubTheme {
        Surface {
            SpendRedCoinsExplanations(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@Composable
fun ExplanationBlock(
    explanations: List<ExplanationItem>,
    equivalenceList: List<CurrencyEquivalence>,
    modifier: Modifier = Modifier,
    redCoinsOrder: EuroRedCoinsOrder = EuroRedCoinsOrder.EURO_FIRST,
) {
    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
        explanations.forEach {
            Explanation(explanationItem = it,
                equivalenceList = if(it.position == 1)
                    equivalenceList
                else null,
                redCoinsOrder = redCoinsOrder)
        }
    }
}

@Preview
@Composable
fun EarnRedCoinsExplanationsPreview() {
    RedCableClubTheme {
        Surface {
            EarnRedCoinsExplanations(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        }
    }
}

@Composable
fun Explanation(
    explanationItem: ExplanationItem,
    modifier: Modifier = Modifier,
    equivalenceList: List<CurrencyEquivalence>? = null,
    redCoinsOrder: EuroRedCoinsOrder = EuroRedCoinsOrder.EURO_FIRST,

    ) {
    Column(
        horizontalAlignment = Alignment.Start,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            NumberIcon(position = explanationItem.position)
            Text(text = explanationItem.title, style = MaterialTheme.typography.titleLarge)
        }

        Text(
            text = explanationItem.body,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(all = dimensionResource(R.dimen.padding_small))
        )
        if(equivalenceList != null) {
            EuroRedCoinsEquivalenceGrid(
                redCoinsOrder = redCoinsOrder,
                equivalenceList = equivalenceList,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_small))
            )
        }
    }
}

@Preview
@Composable
fun ExplanationPreview() {
    RedCableClubTheme {
        Surface {
            Explanation(
                ExplanationItem(
                title = "Title example",
                body = "This is the explanation body. It should explain how the body works",
                position = 1
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NumberIcon(
    position: Int,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.primary,
    // Add a size parameter for better control and to ensure it's a circle
    iconSize: Dp = 32.dp, // Example default size, adjust as needed
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(iconSize) // <--- *** ADD THIS: Enforce a square size ***
            .clip(shape = CircleShape)
            .border(
                width = 2.dp, // Adjusted for a typical icon border size
                color = borderColor,
                shape = CircleShape
            )
    ) {
        Text(
            text = "$position",
            style = MaterialTheme.typography.titleMedium,
            autoSize = TextAutoSize.StepBased(),
            color = borderColor // Optionally, match text color to border or make it configurable
        )
    }
}

enum class EuroRedCoinsOrder {
    EURO_FIRST,
    RED_COINS_FIRST
}

data class CurrencyEquivalence(val redCoins: Int, val currencyValue: BigDecimal, val currencySymbol: String)

@Composable
fun EuroRedCoinsEquivalenceGrid(
    redCoinsOrder: EuroRedCoinsOrder,
    equivalenceList: List<CurrencyEquivalence>,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
        ) {
            equivalenceList.forEach { currencyEquivalence ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val currencyText =
                        "${currencyEquivalence.currencyValue} ${currencyEquivalence.currencySymbol}"

                    when (redCoinsOrder) {
                        EuroRedCoinsOrder.EURO_FIRST -> {
                            Text(
                                text = currencyText,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = "=",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.red_coins_icon), contentDescription = null)
                                Text(text = " ${currencyEquivalence.redCoins}", style = MaterialTheme.typography.bodyLarge)
                            }
                        }

                        EuroRedCoinsOrder.RED_COINS_FIRST -> {
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(painter = painterResource(id = R.drawable.red_coins_icon), contentDescription = null)
                                Text(text = currencyEquivalence.redCoins.toString(), style = MaterialTheme.typography.bodyLarge)
                            }

                            Text(
                                text = "=",
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = currencyText,
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview
@Composable
fun EuroRedCoinsEquivalenceGridPreview() {
    RedCableClubTheme {
        Surface {
            EuroRedCoinsEquivalenceGrid(
                redCoinsOrder = EuroRedCoinsOrder.RED_COINS_FIRST,
                equivalenceList = listOf(
                    CurrencyEquivalence(100, BigDecimal(1),"€"),
                    CurrencyEquivalence(200, BigDecimal(2),"€"),
                    CurrencyEquivalence(500, BigDecimal(5),"€")
                ),
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
fun NumberIconPreview() {
    RedCableClubTheme {
        Surface {
            NumberIcon(9, modifier = Modifier.padding(8.dp))
        }
    }
}

@Preview
@Composable
fun RedCoinsCardPreview() {
    RedCableClubTheme {
        Surface { RedCoinsCard(redCoins = 100, modifier = Modifier.padding(16.dp)) }


    }

}




package com.oneplus.redcableclub.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.FakeRedCoinsShopRepository
import com.oneplus.redcableclub.data.model.Benefit
import com.oneplus.redcableclub.data.model.Product
import com.oneplus.redcableclub.data.model.ShopItem
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.utils.FaqItem
import com.oneplus.redcableclub.ui.utils.FaqItemViewWithCustomExtension
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation
import java.math.BigDecimal
import java.math.RoundingMode


@Composable
fun ProductCard(
    product: ShopItem,
    modifier: Modifier = Modifier
) {

    Card(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            when(product) {
                is Product -> ProductImage(
                    imageUrl = product.imageUrls[0],
                    contentDescription = product.name,
                    modifier = Modifier.fillMaxWidth()
                )
                is Benefit -> ProductBenefitIcon(
                    iconText = product.iconText,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small))
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Row(
                    horizontalArrangement = Arrangement.Center, // Changed from SpaceAround
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
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
                            text = stringResource(R.string.red_coins, product.redCoinsRequired),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }

                    when (product) {
                        is Product -> {
                            Text(
                                text = " + ", // Added separator
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_extra_small))
                            )
                            Text(
                                text = stringResource(R.string.price, product.price),
                                style = MaterialTheme.typography.titleMedium // Matched style for consistency
                            )
                        }
                        is Benefit -> Spacer(modifier = Modifier) // No price for benefits
                    }
                }
            }


        }
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductImage(
    imageUrl: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.product_image_size),
    borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.secondary,
    innerBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {

    Box(
        modifier =modifier
            .aspectRatio(1f)
            .size(size)
            .clip(CircleShape)
            .background(innerBackgroundColor)
            .border(borderWidth, borderColor, CircleShape)
            .padding(dimensionResource(R.dimen.padding_large))
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
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

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ProductBenefitIcon(
    iconText: String,
    modifier: Modifier = Modifier,
    size: Dp = dimensionResource(R.dimen.product_image_size),
    borderWidth: Dp = dimensionResource(R.dimen.padding_extra_small),
    borderColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    innerBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer
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
            style = MaterialTheme.typography.displayLargeEmphasized.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary,
            autoSize = TextAutoSize.StepBased()
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
            ProductCard(product = product, modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
        }
    }

}

@Preview
@Composable
fun ProductCardPreview() {

    RedCableClubTheme {
        Surface {
            ProductCard(product = RedCableClubApiServiceMock.storeProducts[0], modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium)))
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
    modifier: Modifier = Modifier
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
                modifier = Modifier.fillMaxWidth(fraction = 0.66f)
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
    val body: String
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
    modifier: Modifier = Modifier
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
    redCoinsOrder: EuroRedCoinsOrder = EuroRedCoinsOrder.EURO_FIRST
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
    redCoinsOrder: EuroRedCoinsOrder = EuroRedCoinsOrder.EURO_FIRST

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
    modifier: Modifier = Modifier
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

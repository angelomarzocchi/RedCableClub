package com.oneplus.redcableclub.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearWavyProgressIndicator
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.utils.FaqItem
import com.oneplus.redcableclub.ui.utils.FaqItemQuestion
import com.oneplus.redcableclub.ui.utils.FaqItemViewWithCustomExtension
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RedCoinsCard(
    redCoins: Int,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        LinearWavyProgressIndicator(
            progress = {1f},
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = dimensionResource(R.dimen.padding_medium)),
            stroke = Stroke(width = 16.dp.value, cap = StrokeCap.Round),
            waveSpeed = 5.dp
        )
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.red_coins_icon),
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
                expandedView = {EarnRedCoinsExplanations()}
            )




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

    val explanations = listOf<ExplanationItem>(
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

    Column(
        modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
    ) {
      explanations.forEach {
          Explanation(explanationItem = it)
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
                body = "This is the explanation body. It should expain how the body works",
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
    REDCOINS_FIRST
}

data class CurrencyEquivalence(val redCoins: Int, val currencyValue: BigDecimal, val currencySymbol: String)

@Composable
fun EuroRedCoinsEquivalenceGrid(
    redCoinsOrder: EuroRedCoinsOrder,
    equivalenceList: List<CurrencyEquivalence>,
    modifier: Modifier = Modifier
) {
    Column {
        equivalenceList.forEach { currencyEquivalence ->
            Row {
                Box(
                    modifier = Modifier.border(
                        width = 4.dp,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    if(redCoinsOrder == EuroRedCoinsOrder.EURO_FIRST) {
                        Row(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
                            Text(
                                text = currencyEquivalence.currencySymbol,
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(
                                text = currencyEquivalence.currencyValue.toString(),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    } else {
                        Row(modifier = Modifier.padding(dimensionResource(R.dimen.padding_small))) {
                            Icon(painter = painterResource(R.drawable.red_coins_icon), contentDescription = null)
                            Text(
                                text = "${currencyEquivalence.redCoins}"
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
                redCoinsOrder = EuroRedCoinsOrder.REDCOINS_FIRST,
                equivalenceList = listOf(CurrencyEquivalence(1, BigDecimal(1),"€"))
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

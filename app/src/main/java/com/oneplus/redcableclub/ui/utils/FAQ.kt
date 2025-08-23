package com.oneplus.redcableclub.ui.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme

data class FaqItem(
    val id: Int,
    val question: String,
    val answer: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FaqItemView(
    faqItem: FaqItem,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    modifier: Modifier = Modifier,
) {

    val iconRotation by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f, label = "iconRotation")


    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable{ onToggleExpand() }

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = faqItem.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                onToggleExpand()
            }) {
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.RemoveCircle else Icons.Filled.AddCircle,
                    contentDescription = if (isExpanded) "Collapse" else "Expand",
                    modifier = Modifier.rotate(iconRotation) // Simpler: just change icon
                )
            }
        }

        AnimatedVisibility(visible = isExpanded) {
            Text(
                text = faqItem.answer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.padding_small),
                    start =dimensionResource(R.dimen.padding_small),
                    end = dimensionResource(R.dimen.padding_small)
                )
            )
        }
    }

}

@Composable
fun FaqList(
    faqItems: List<FaqItem>,
    modifier: Modifier = Modifier
) {

    var expandedItemId by remember { mutableStateOf<Int?>(null) }

    if (faqItems.isEmpty()) {
        Text("No FAQs available at the moment.", modifier = modifier.padding(dimensionResource(R.dimen.padding_medium)))
        return
    }
    Column(modifier = modifier) {
        faqItems.forEach{ faqItem ->
            FaqItemView(
                faqItem = faqItem,
                isExpanded = faqItem.id == expandedItemId,
                onToggleExpand = {
                    if(faqItem.id == expandedItemId)
                    expandedItemId = null
                    else expandedItemId = faqItem.id
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FaqListPreview() {
    val sampleFaqs = listOf(
        FaqItem(1, "What is Jetpack Compose?", "Jetpack Compose is Android's modern toolkit for building native UI. It simplifies and accelerates UI development on Android."),
        FaqItem(2, "How does BringIntoViewRequester work?", "It allows a composable to request its parents to scroll so that it's visible on the screen. This is useful for focusing on elements that might be out of view after an interaction, like expanding an item."),
        FaqItem(3, "Is this a long question to test how the text wrapping behaves and ensure that the layout remains stable and looks good even with more content?", "Yes, this is a longer answer to demonstrate how the content will be displayed when the FAQ item is expanded. We want to make sure that the text flows correctly and that the BringIntoViewRequester properly adjusts the scroll position if this answer goes off-screen."),
        FaqItem(4, "Another question?", "Another answer.")
    )
    RedCableClubTheme {
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Spacer(modifier = Modifier.fillMaxHeight())
            FaqList(
                faqItems = sampleFaqs,
            )
        }

    }
}

package com.oneplus.redcableclub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Device
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme
import com.oneplus.redcableclub.ui.utils.Carousel
import com.oneplus.redcableclub.ui.utils.FaqItem
import com.oneplus.redcableclub.ui.utils.FaqList
import com.oneplus.redcableclub.ui.utils.shimmerLoadingAnimation


@Composable
fun Service(
    devices: List<Device>,
    modifier: Modifier = Modifier
) {
    val serviceScrollState = rememberScrollState()

    Column(
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_medium))
            .verticalScroll(serviceScrollState)) {
        DeviceCarousel(devices = devices)
        //spacer = dynamic indicator (8.dp) + carousel-indicator padding (8.dp) + normal spacing (8.dp)
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small)))
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small) * 3),
        ) {
            DiagnosticCard(modifier = Modifier.fillMaxWidth())
            TroubleshootingCard()
        }
        Spacer(modifier = Modifier.size(dimensionResource(R.dimen.padding_small) * 3))
        FaqList(
            faqItems = listOf(
                FaqItem(
                    1,
                    stringResource(R.string.faq_service_q1),
                    stringResource(R.string.faq_service_a1)
                ),
                FaqItem(
                    2,
                    stringResource(R.string.faq_service_q2),
                    stringResource(R.string.faq_service_a2))

            )
        )
    }

}

@Preview
@Composable
fun ServicePreview() {
    RedCableClubTheme {
        Surface {
            Service(
                devices = RedCableClubApiServiceMock.userMock.devices,
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }
    }
}




@Composable
fun TroubleshootingCard(
    modifier: Modifier = Modifier
) {
    QuickActionCard(action = "Troubleshooting", icon = painterResource(R.drawable.troubleshooting_icon), modifier = modifier)
}



@Composable
fun DiagnosticCard(
    modifier: Modifier = Modifier
) {
    QuickActionCard(action = "Diagnostic", icon = painterResource(R.drawable.outline_troubleshoot_24), modifier = modifier)
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuickActionCard(
    action: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Spacer(modifier = Modifier)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    painter = icon,
                    contentDescription = action,
                    modifier = Modifier.size(64.dp)
                )
                Text(
                    text = action,
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    modifier = Modifier
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Request Support", // Describes the action
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun DiagnosticCardPreview() {
    RedCableClubTheme {
        DiagnosticCard()
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun TroubleshootingCardPreview() {
    RedCableClubTheme {
        TroubleshootingCard()
    }
}

@Composable
fun DeviceCarousel(
    devices: List<Device>,
    modifier: Modifier = Modifier
) {
    Carousel(
        items = devices,
        showOnlyHeroItem = true,
        enableInfiniteScroll = true,
        itemContent = { device, isHero ->
            DeviceCard(
                device = device,
                modifier = Modifier.fillMaxWidth()
            )
        },
        modifier = modifier
    )
}

@Preview
@Composable
fun DeviceCarouselPreview() {

    RedCableClubTheme {
        DeviceCarousel(
            devices = RedCableClubApiServiceMock.userMock.devices,
            modifier = Modifier
        )
    }

}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DeviceCard(
    device: Device,
   modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            SubcomposeAsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(device.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = device.name,
                modifier = Modifier.weight(1f),
                loading = {
                    Box(
                        modifier = Modifier
                            .shimmerLoadingAnimation()
                    )
                }
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(2f)
            ) {
                Text(
                    text = device.name,
                    style = MaterialTheme.typography.titleLargeEmphasized,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "IMEI: ${device.imei}",
                    style = MaterialTheme.typography.bodySmall
                )
                Row(modifier = Modifier.padding(dimensionResource(R.dimen.padding_extra_small))) {
                    Icon(
                        painter = painterResource(R.drawable.repair_icon),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Request Support")
                    Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_extra_small)))
                    Text(text = "Request Support", color = MaterialTheme.colorScheme.primary)
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Request Support", // Describes the action
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}

@Preview
@Composable
fun DeviceCardPreview() {
    RedCableClubTheme {
        DeviceCard(device = RedCableClubApiServiceMock.userMock.devices[0])
    }

}
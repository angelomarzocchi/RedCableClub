package com.oneplus.redcableclub.ui.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarScrollBehavior
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.navigation.RedCableClubScreen
import com.oneplus.redcableclub.navigation.RedCoinsShopScreen
import com.oneplus.redcableclub.navigation.ServiceDetailScreen

data class RedCableClubNavigationItem(
    val screen: NavKey,
    @StringRes val labelRes:  Int,
    @DrawableRes val iconRes: Int,
)


val bottomNavigationItems = listOf(
    RedCableClubNavigationItem(
        screen = RedCableClubScreen,
        labelRes = R.string.app_name,
        iconRes = R.drawable.red_cable_club_icon,
    ),
    RedCableClubNavigationItem(
        screen = RedCoinsShopScreen,
        labelRes = R.string.red_coins_shop,
        iconRes = R.drawable.crown_icon,
    ),
    RedCableClubNavigationItem(
        screen = ServiceDetailScreen,
        labelRes = R.string.service_detail,
        iconRes = R.drawable.service_icon
    )

)

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RedCableClubNavigationBar(
    isSelected: (NavKey) -> Boolean,
    onSelected: (NavKey) -> Unit,
    scrollBehavior: BottomAppBarScrollBehavior? = null
) {

    BottomAppBar(
        scrollBehavior = scrollBehavior,
    ) {
        bottomNavigationItems.forEach { it ->
            NavigationBarItem(
                selected = isSelected(it.screen),
                onClick = { onSelected(it.screen) },
                icon = {
                    Icon(
                        painterResource(it.iconRes),
                        contentDescription = stringResource(it.labelRes),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(stringResource(it.labelRes)) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RedCableClubFloatingNavigationBar(
    isSelected: (NavKey) -> Boolean,
    onSelected: (NavKey) -> Unit,
    scrollBehavior: FloatingToolbarScrollBehavior? = null
) {

    HorizontalFloatingToolbar(
        scrollBehavior = scrollBehavior,expanded = true) {
        bottomNavigationItems.forEach { it ->
            NavigationBarItem(
                selected = isSelected(it.screen),
                onClick = { onSelected(it.screen) },
                icon = {
                    Icon(
                        painterResource(it.iconRes),
                        contentDescription = stringResource(it.labelRes),
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = { Text(stringResource(it.labelRes)) }
            )
        }
    }

}

@Composable
fun RedCableClubNavigationRail(
    isSelected: (NavKey) -> Boolean,
    onSelected: (NavKey) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bottomNavigationItems.forEach { it ->
                NavigationRailItem(
                    selected = isSelected(it.screen),
                    onClick = { onSelected(it.screen)},
                    icon = {
                        Icon(
                            painterResource(it.iconRes),
                            contentDescription = stringResource(it.labelRes),
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(stringResource(it.labelRes)) }
                )
            }
        }

    }
}

@Composable
fun RedCableClubPermanentNavigationDrawer(
    isSelected: (NavKey) -> Boolean,
    onSelected: (NavKey) -> Unit,
    appContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    drawerWidth: Dp = 240.dp
) {
    PermanentNavigationDrawer(
        modifier = modifier,
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(drawerWidth),
                drawerContainerColor = MaterialTheme.colorScheme.inverseSurface
            ) {
                NavigationDrawerContent(
                    isSelected = isSelected,
                    onSelected = onSelected,
                    modifier = Modifier
                        .wrapContentWidth()
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.inverseSurface)
                        .padding(dimensionResource(R.dimen.padding_medium))
                )
            }
        }
    ) { appContent() }
}

@Composable
private fun NavigationDrawerContent(
    isSelected: (NavKey) -> Boolean,
    onSelected: (NavKey) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        NavigationDrawerHeader(
            modifier = Modifier.fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )

        bottomNavigationItems.forEach{ item ->
            NavigationDrawerItem(
                selected = isSelected(item.screen),
                onClick = { onSelected(item.screen) },
                label = { Text(stringResource(item.labelRes)) },
                icon = {
                    Icon(
                        painterResource(item.iconRes),
                        contentDescription = stringResource(item.labelRes),
                        modifier = Modifier.size(24.dp)
                    )
                }
            )
        }
    }
}

@Preview
@Composable
fun NavigationDrawerContentPreview() {
    NavigationDrawerContent(
        isSelected = { key -> key == RedCableClubScreen },
        onSelected = {}
    )
}

@Composable
private fun NavigationDrawerHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Image(
            painter = painterResource(id = R.drawable.red_cable_club_icon),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
            modifier = Modifier.size(
                dimensionResource(id = R.dimen.top_bar_icon_size)
            )
        )
        Spacer(modifier = Modifier.width(dimensionResource(R.dimen.padding_medium)))
        Text(
            text =  stringResource(R.string.never_settle),
            maxLines = 2,
            autoSize = TextAutoSize.StepBased(maxFontSize = MaterialTheme.typography.bodyLarge.fontSize),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
        )
    }
}

@Preview
@Composable
fun RedCableClubNavigationRailPreview() {
    RedCableClubNavigationRail(
        isSelected = { key -> key == RedCableClubScreen },
        onSelected = {}
    )
}

@Preview
@Composable
fun NavigationDrawerHeaderPreview() {
    NavigationDrawerHeader()
}




@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview
@Composable
fun RedCableClubFloatingNavigationBarPreview() {
    RedCableClubFloatingNavigationBar(
        isSelected = { key -> key == RedCableClubScreen },
        onSelected = {}
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RedCableClubNavigationBarPreview() {
    RedCableClubNavigationBar(
        isSelected = { key -> key == RedCableClubScreen },
        onSelected = {}
    )
}
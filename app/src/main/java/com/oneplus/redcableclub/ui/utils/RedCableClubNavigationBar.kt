package com.oneplus.redcableclub.ui.utils

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarScrollBehavior
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
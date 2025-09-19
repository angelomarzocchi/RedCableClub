package com.oneplus.redcableclub.ui.utils


import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.ui.theme.RedCableClubTheme

data class TopBarState(
    val textResource: Int,
    val textResourceArgs: List<Any> = emptyList(),
    val showNavigateBack: Boolean = false,
    val navigateBack: () -> Unit = {},
    val icon : Int? = null,
    val isNavigatingBack: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RedCableClubTopBar(
    @StringRes textResource: Int,
    textResourceArgs: List<Any> = emptyList(),
    scrollBehavior: TopAppBarScrollBehavior,
    modifier : Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    showNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    @DrawableRes icon : Int? = R.drawable.red_cable_club_icon,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = actions,
        navigationIcon = {
            if(showNavigateBack)
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Box(
                    modifier =if(icon != null) Modifier.size(
                        dimensionResource(id = R.dimen.top_bar_icon_size)
                    ) else Modifier.height(dimensionResource(id = R.dimen.top_bar_icon_size))
                ) {
                    if (icon != null) {
                        Image(
                            painter = painterResource(id = icon),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                            modifier = Modifier.size(
                                dimensionResource(id = R.dimen.top_bar_icon_size)
                            )
                        )
                    }
                }
                    AnimatedContent(targetState = textResource) {title ->
                        Text(
                            text = stringResource(id = title, *textResourceArgs.toTypedArray()),
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }



            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
fun RedCableClubTopBarPreview() {
    RedCableClubTheme {
        RedCableClubTopBar(
            textResource = R.string.app_name,
            scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
fun RedCableClubTopBarCustomIconPreview() {
    RedCableClubTopBar(
        textResource = R.string.app_name,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        icon = R.drawable.achievement_icon
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Preview(showBackground = true)
@Composable
fun RedCableClubTopBarNoIconPreview() {
    RedCableClubTopBar(
        textResource = R.string.app_name,
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        icon = null
    )
}

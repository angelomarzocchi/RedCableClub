package com.oneplus.redcableclub.ui.utils


import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.oneplus.redcableclub.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RedCableClubTopBar(
    @StringRes textResource: Int,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier : Modifier = Modifier,
    actions: @Composable RowScope.() -> Unit = {},
    showNavigateBack: Boolean = false,
    navigateBack: () -> Unit = {},
    icon : Int? = R.drawable.ic_launcher_foreground,
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        actions = actions,
        navigationIcon = {
            if(showNavigateBack)
            IconButton(onClick = navigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Localized description"
                )
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                if(icon != null)
                    Image(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                    )

                Text(
                    text = stringResource(id = textResource),
                    style = MaterialTheme.typography.headlineMediumEmphasized)
            }
        }
    )
}
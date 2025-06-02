package com.oneplus.redcableclub.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.ui.screens.RedCableClub
import com.oneplus.redcableclub.ui.screens.RedCableClubViewModel
import com.oneplus.redcableclub.ui.navigation.RedCableClubNavigationType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(
    windowSize: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val navigationType: RedCableClubNavigationType = getNavigationTypeByWindowSize(windowSize)
    val redCableClubViewModel: RedCableClubViewModel = viewModel(factory = RedCableClubViewModel.Factory)
    LaunchedEffect(Unit) { // Keyed on Unit to run once, or on a userId if it can change
        redCableClubViewModel.getUserProfile("AngeloMarzo")
    }
    val uiState by redCableClubViewModel.uiState.collectAsState()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val navController = rememberNavController()

    val navigateToDestination: (String) -> Unit = {route->
        navController.navigate(route) {
            popUpTo(navController.graph.startDestinationId) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

   /* when(navigationType) {
        RedCableClubNavigationType.BOTTOM_NAVIGATION -> /*TODO BottomNavigationBar(
            navController = navController,
            navigateToDestination = navigateToDestination
        ) */
        RedCableClubNavigationType.NAVIGATION_RAIL -> /* TODO NavigationRailBar(
            navController = navController,
            navigateToDestination = navigateToDestination
        )
       */
        RedCableClubNavigationType.PERMANENT_NAVIGATION_DRAWER -> /*TODO PermanentNavigationBar(
            navController = navController,
            navigateToDestination = navigateToDestination
        )*/


    }*/

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {

            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                   Row(
                       verticalAlignment = Alignment.CenterVertically,
                       horizontalArrangement = Arrangement.Center,
                   ) {
                       Image(
                           painter = painterResource(id = R.drawable.ic_launcher_foreground),
                           contentDescription = null,
                       )
                       Text(stringResource(id = R.string.app_name),
                           style = MaterialTheme.typography.displaySmall)
                   }
                }
            )
        }
    ) { innerPadding ->
        RedCableClub(
            uiState = uiState,
            paddingValues = innerPadding)
    }
}

private fun getNavigationTypeByWindowSize(windowSize: WindowSizeClass): RedCableClubNavigationType {
    val widthSize = windowSize.widthSizeClass
    val heightSize = windowSize.heightSizeClass
    return if(widthSize == WindowWidthSizeClass.Compact){
        RedCableClubNavigationType.BOTTOM_NAVIGATION
    } else if(widthSize == WindowWidthSizeClass.Medium || heightSize != WindowHeightSizeClass.Expanded){
        RedCableClubNavigationType.NAVIGATION_RAIL
    } else {
        RedCableClubNavigationType.PERMANENT_NAVIGATION_DRAWER
    }
}
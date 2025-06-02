package com.oneplus.redcableclub.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.oneplus.redcableclub.ui.screens.RedCableClub
import com.oneplus.redcableclub.ui.screens.RedCableClubDestination
import com.oneplus.redcableclub.ui.screens.RedCableClubUiState


@Composable
fun RedCableClubNavHost(
    navController: NavHostController,
    uiState: RedCableClubUiState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = RedCableClubDestination.route,
        modifier = modifier
    ) {
        composable(route = RedCableClubDestination.route) {
            RedCableClub(
                uiState = uiState
            )
        }
    }
}
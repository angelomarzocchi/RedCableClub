package com.oneplus.redcableclub.ui.navigation

import androidx.compose.ui.graphics.vector.ImageVector

interface NavigationDestination {
    val route: String
    val labelStringId: Int
    val iconId: Int
}
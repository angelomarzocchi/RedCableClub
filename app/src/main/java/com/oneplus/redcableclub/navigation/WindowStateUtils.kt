package com.oneplus.redcableclub.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass


enum class RedCableClubNavigationType {
    BOTTOM_NAVIGATION,
    NAVIGATION_RAIL,
    PERMANENT_NAVIGATION_DRAWER
}


fun computeNavigationType(windowSizeClass: WindowSizeClass): RedCableClubNavigationType {
    return when {
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact -> RedCableClubNavigationType.BOTTOM_NAVIGATION
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Medium -> RedCableClubNavigationType.NAVIGATION_RAIL
        windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded -> RedCableClubNavigationType.PERMANENT_NAVIGATION_DRAWER
        else -> RedCableClubNavigationType.BOTTOM_NAVIGATION
        }
}
package com.oneplus.redcableclub.navigation




import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.UserProfile
import com.oneplus.redcableclub.ui.screens.Achievements
import com.oneplus.redcableclub.ui.screens.RedCableClub
import com.oneplus.redcableclub.ui.screens.RedCableClubViewModel
import com.oneplus.redcableclub.ui.utils.RedCableClubNavigationBar
import com.oneplus.redcableclub.ui.utils.RedCableClubNavigationRail
import com.oneplus.redcableclub.ui.utils.RedCableClubPermanentNavigationDrawer
import com.oneplus.redcableclub.ui.utils.RedCableClubTopBar
import com.oneplus.redcableclub.ui.utils.ResourceState
import com.oneplus.redcableclub.ui.utils.TopBarState
import kotlinx.serialization.Serializable

@Serializable
data object RedCableClubScreen: NavKey

@Serializable
data object AchievementScreen: NavKey

@Serializable
data object RedCoinsShopScreen: NavKey

@Serializable
data object ServiceDetailScreen: NavKey





@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavigationRoot(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier
) {
    val redCableClubViewModel: RedCableClubViewModel = viewModel(factory = RedCableClubViewModel.Factory)

    val uiState by redCableClubViewModel.uiState.collectAsState()

    val backStack = rememberNavBackStack(RedCableClubScreen)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior(rememberBottomAppBarState())
    val navigationType = computeNavigationType(windowSizeClass)

    var topBarState by remember { mutableStateOf(TopBarState(R.string.app_name, showNavigateBack = false, icon = R.drawable.red_cable_club_icon)) }
    val insets = WindowInsets.systemBars


    val currentScreen by remember { derivedStateOf { (backStack.lastOrNull()) } }

    val isScreenSelected: (NavKey) -> Boolean = { navKey -> backStack.lastOrNull() == navKey}
    val onScreenSelected: (NavKey) -> Unit = { navKey -> backStack.apply { clear(); addLast(navKey) } }

    if(navigationType != RedCableClubNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        Scaffold(
            modifier = modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .nestedScroll(bottomScrollBehavior.nestedScrollConnection),
            topBar = {
                AnimatedContent(
                    // Slide in from the top
                    targetState = topBarState.textResource,
                    transitionSpec = { slideInHorizontally { if (topBarState.isNavigatingBack) it else -it } togetherWith slideOutHorizontally { if (topBarState.isNavigatingBack) -it else it } }
                ) { text ->
                    RedCableClubTopBar(
                        scrollBehavior = scrollBehavior,
                        textResource = text,
                        showNavigateBack = topBarState.showNavigateBack,
                        icon = topBarState.icon,
                        navigateBack = topBarState.navigateBack
                    )
                }
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomNavigation(backStack.lastOrNull()) && navigationType == RedCableClubNavigationType.BOTTOM_NAVIGATION,
                    // Slide in from the bottom
                    enter = slideInVertically(initialOffsetY = { it }),
                    // Slide out to the bottom
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    RedCableClubNavigationBar(
                        isSelected = isScreenSelected,
                        onSelected = onScreenSelected,
                        scrollBehavior = bottomScrollBehavior
                    )
                }
            }
        ) { innerPadding ->
            Row(modifier = Modifier.padding(innerPadding)) {

                    if (navigationType == RedCableClubNavigationType.NAVIGATION_RAIL)
                        RedCableClubNavigationRail(
                            isSelected = isScreenSelected,
                            onSelected = onScreenSelected,
                            modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                        )

                    RedCableClubNavDisplay(
                        modifier = modifier,
                        backStack = backStack,
                        onTopBarStateChange = { newTopBarState -> topBarState = newTopBarState },
                        uiState = uiState,
                        insets = insets
                    )

            }
        }
    } else {
        RedCableClubPermanentNavigationDrawer(
            isSelected = isScreenSelected,
            onSelected = onScreenSelected,
            appContent = {
                RedCableClubNavDisplay(
                    modifier = modifier,
                    backStack = backStack,
                    onTopBarStateChange = { newTopBarState -> topBarState = newTopBarState },
                    uiState = uiState,
                    insets = insets
                )
            }
        )
    }
}


fun showBottomNavigation(navKey: NavKey?): Boolean {
    return navKey == RedCableClubScreen ||
            navKey == RedCoinsShopScreen ||
            navKey == ServiceDetailScreen
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RedCableClubNavDisplay(
    modifier: Modifier,
    backStack: androidx.navigation3.runtime.NavBackStack,
    onTopBarStateChange: (TopBarState) -> Unit,
    uiState: com.oneplus.redcableclub.ui.screens.RedCableClubUiState,
    insets: WindowInsets
) {
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = { route ->
            when(route) {
                is RedCableClubScreen -> {
                    NavEntry(key = route) {
                        Log.d("NavigationRoot", "RedCableClubScreen")
                        onTopBarStateChange(TopBarState(R.string.app_name, showNavigateBack = false, icon = R.drawable.red_cable_club_icon))
                        RedCableClub(
                            uiState = uiState,
                            onAchievementDetailClick = {
                                backStack.addLast(AchievementScreen)
                            },
                            paddingValues = insets.asPaddingValues(),

                            )
                    }
                }
                is AchievementScreen -> {
                    NavEntry(key = route) {
                        Log.d("NavigationRoot", "AchievementScreen")
                        onTopBarStateChange(TopBarState(
                            R.string.achievements,
                            showNavigateBack = true,
                            navigateBack = {
                                backStack.remove(AchievementScreen)
                            },
                            isNavigatingBack = true
                        ))
                        Achievements(
                            achievements = (uiState.userProfileState as ResourceState.Success<UserProfile>).data.achievements,
                        )
                    }
                }
                else -> {
                    NavEntry(key = RedCableClubScreen) {}
                }
            }
        },
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith slideOutHorizontally(targetOffsetX = { it })
        }
    )
}

package com.oneplus.redcableclub.navigation




import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Achievement
import com.oneplus.redcableclub.data.model.Coupon
import com.oneplus.redcableclub.data.model.UserProfile
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import com.oneplus.redcableclub.ui.screens.Achievements
import com.oneplus.redcableclub.ui.screens.AchievementsUiState
import com.oneplus.redcableclub.ui.screens.AchievementsViewModel
import com.oneplus.redcableclub.ui.screens.Coupons
import com.oneplus.redcableclub.ui.screens.MembershipTierCardCarousel
import com.oneplus.redcableclub.ui.screens.RedCableClub
import com.oneplus.redcableclub.ui.screens.RedCableClubUiState
import com.oneplus.redcableclub.ui.screens.RedCableClubViewModel
import com.oneplus.redcableclub.ui.utils.RedCableClubNavigationBar
import com.oneplus.redcableclub.ui.utils.RedCableClubNavigationRail
import com.oneplus.redcableclub.ui.utils.RedCableClubPermanentNavigationDrawer
import com.oneplus.redcableclub.ui.utils.RedCableClubTopBar
import com.oneplus.redcableclub.ui.utils.ResourceState
import com.oneplus.redcableclub.ui.utils.TopBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Serializable
data object RedCableClubScreen: NavKey

@Serializable
data object AchievementScreen: NavKey

@Serializable
data object CouponsScreen: NavKey

@Serializable
data object RedCoinsShopScreen: NavKey

@Serializable
data object ServiceDetailScreen: NavKey






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationRoot(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val redCableClubViewModel: RedCableClubViewModel = viewModel(factory = RedCableClubViewModel.Factory)
    val achievementsViewModel: AchievementsViewModel = viewModel(factory = AchievementsViewModel.Factory)

    val redCableClubUiState by redCableClubViewModel.uiState.collectAsState()
    val achievementsUiState by achievementsViewModel.uiState.collectAsState()

    val backStack = rememberNavBackStack(RedCableClubScreen)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior(rememberBottomAppBarState())
    val navigationType = computeNavigationType(windowSizeClass)

    var topBarState by remember { mutableStateOf(TopBarState(R.string.app_name, showNavigateBack = false, icon = R.drawable.red_cable_club_icon)) }
    val insets = WindowInsets.systemBars
    val layoutDirection = LocalLayoutDirection.current // Get current layout direction

    var mainContentSize by remember { mutableIntStateOf(0) }


    val isScreenSelected: (NavKey) -> Boolean = { navKey -> backStack.lastOrNull() == navKey}
    val onScreenSelected: (NavKey) -> Unit = { navKey -> backStack.apply { clear(); addLast(navKey) } }

    if(navigationType != RedCableClubNavigationType.PERMANENT_NAVIGATION_DRAWER) {
        Scaffold(
            modifier = modifier
                .fillMaxSize()
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
                    visible = showNavigation(backStack.lastOrNull()) && navigationType == RedCableClubNavigationType.BOTTOM_NAVIGATION,
                    // Slide in from the bottom
                    enter = slideInVertically(initialOffsetY = { it }),
                    // Slide out to the bottom
                    exit = slideOutVertically(targetOffsetY = { it }) + shrinkVertically(shrinkTowards = Alignment.Bottom)
                ) {
                    RedCableClubNavigationBar(
                        isSelected = isScreenSelected,
                        onSelected = onScreenSelected,
                        scrollBehavior = bottomScrollBehavior
                    )
                }
            },
            contentWindowInsets = WindowInsets(0,0,0,0)
        ) { innerPadding ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
            ) {
                    if (navigationType == RedCableClubNavigationType.NAVIGATION_RAIL) {

                            RedCableClubNavigationRail(
                                isSelected = isScreenSelected,
                                onSelected = onScreenSelected,
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .padding(
                                        start = dimensionResource(R.dimen.padding_small),
                                        top = insets.asPaddingValues().calculateTopPadding(),
                                        bottom = insets.asPaddingValues().calculateBottomPadding()
                                    )
                                    .onSizeChanged { size -> mainContentSize = size.width }
                            )


                    } else { mainContentSize = 0}

                    RedCableClubNavDisplay(
                        modifier = modifier
                            .weight(1f) // Ensure NavDisplay takes remaining space
                            .padding(
                                start = if (navigationType == RedCableClubNavigationType.NAVIGATION_RAIL) {
                                    // If rail is present, no additional start padding needed from innerPaddingScaffold,
                                    // as the rail itself is already there. You might add a small fixed padding if desired.
                                    0.dp // Or a small dimensionResource for spacing between rail and content
                                } else {
                                    // No rail, so content starts from the edge defined by innerPaddingScaffold
                                    innerPadding.calculateStartPadding(layoutDirection)
                                },
                                end = innerPadding.calculateEndPadding(layoutDirection),
                            ),
                        backStack = backStack,
                        onTopBarStateChange = { newTopBarState -> topBarState = newTopBarState },
                        redCableClubUiState = redCableClubUiState,
                        achievementsUiState = achievementsUiState,
                        onAchievementSelected = {achievementsViewModel.selectAchievement(it)},
                        insets = insets,
                        mainContentWidthInPx = mainContentSize,
                        onCouponRedeemed = { redCableClubViewModel.onCouponRedeemed(it) }
                    )

            }
        }
    } else {
        RedCableClubPermanentNavigationDrawer(
            modifier = Modifier.onSizeChanged {size -> mainContentSize = size.width},
            isSelected = isScreenSelected,
            onSelected = onScreenSelected,
            appContent = {
                RedCableClubNavDisplay(
                    modifier = modifier,
                    backStack = backStack,
                    onTopBarStateChange = { newTopBarState -> topBarState = newTopBarState },
                    redCableClubUiState = redCableClubUiState,
                    achievementsUiState = achievementsUiState,
                    onAchievementSelected = {achievementsViewModel.selectAchievement(it)},
                    insets = insets,
                    mainContentWidthInPx = mainContentSize,
                    onCouponRedeemed = {}
                )
            }
        )
    }
}



fun showNavigation(navKey: NavKey?): Boolean {
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
    redCableClubUiState: RedCableClubUiState,
    achievementsUiState: AchievementsUiState,
    onAchievementSelected: (Achievement) -> Unit,
    onCouponRedeemed: (Coupon) -> Unit,
    insets: WindowInsets,
    mainContentWidthInPx: Int,
) {
    val coroutineScope = rememberCoroutineScope()
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
                            uiState = redCableClubUiState,
                            onAchievementDetailClick = {
                                backStack.addLast(AchievementScreen)
                            },
                            onShowAllCouponsClick = {
                                coroutineScope.launch {
                                    delay(300L)
                                    backStack.addLast(CouponsScreen)
                                }
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
                            achievements = (
                                    achievementsUiState.achievementsUiState as ResourceState.Success<List<Achievement>>
                                    ).data,
                            selectedAchievement = achievementsUiState.selectedAchievement,
                            onItemSelected = { achievement ->
                                onAchievementSelected(achievement)
                            },
                            paddingValues = insets.asPaddingValues()
                        )


                    }
                }
                is CouponsScreen -> {
                    NavEntry(key = route) {
                        Log.d("NavigationRoot", "CouponsScreen")
                        onTopBarStateChange(TopBarState(
                            R.string.coupons,
                            showNavigateBack = true,
                            navigateBack = {
                                backStack.remove(CouponsScreen)
                            },
                            isNavigatingBack = true
                        ))
                        val userProfile = (redCableClubUiState.userProfileState as ResourceState.Success<UserProfile>).data
                        Coupons(
                            coupons = userProfile.wallet,
                            redExpPoints = userProfile.redExpPoints,
                            onCouponRedeemed = { onCouponRedeemed(it) },
                            paddingValues = insets.asPaddingValues()
                        )
                    }
                }

                else -> {
                    NavEntry(key = RedCableClubScreen) {}
                }
            }
        }, transitionSpec = {
            slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth + mainContentWidthInPx }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { fullWidth -> -fullWidth - mainContentWidthInPx }) + fadeOut()
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth - mainContentWidthInPx }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth + mainContentWidthInPx }) + fadeOut()
        },
        predictivePopTransitionSpec = {
            slideInHorizontally(initialOffsetX = { fullWidth -> -fullWidth - mainContentWidthInPx }) + fadeIn() togetherWith
                    slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth + mainContentWidthInPx }) + fadeOut()
        }
    )
}

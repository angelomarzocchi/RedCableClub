package com.oneplus.redcableclub.navigation




import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingToolbarDefaults
import androidx.compose.material3.FloatingToolbarExitDirection
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberFloatingToolbarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
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
import com.oneplus.redcableclub.ui.utils.RedCableClubTopBar
import com.oneplus.redcableclub.ui.utils.ResourceState
import kotlinx.serialization.Serializable

@Serializable
data object RedCableClubScreen: NavKey

@Serializable
data object AchievementScreen: NavKey

@Serializable
data object  CouponsScreen: NavKey

@Serializable
data class CouponDetailScreen(val couponCode: String): NavKey

@Serializable
data object RedCoinsShopScreen: NavKey

@Serializable
data object ServiceDetailScreen: NavKey





@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val redCableClubViewModel: RedCableClubViewModel = viewModel(factory = RedCableClubViewModel.Factory)
    LaunchedEffect(Unit) { // Keyed on Unit to run once, or on a userId if it can change
        redCableClubViewModel.getUserProfile("AngeloMarzo")
    }
    val uiState by redCableClubViewModel.uiState.collectAsState()

    val backStack = rememberNavBackStack(RedCableClubScreen)
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomScrollBehavior = BottomAppBarDefaults.exitAlwaysScrollBehavior(rememberBottomAppBarState())
    val bottomFloatingScrollBehavior = FloatingToolbarDefaults.exitAlwaysScrollBehavior(exitDirection = FloatingToolbarExitDirection.Bottom, rememberFloatingToolbarState())

    var topBarTitle by remember { mutableIntStateOf(R.string.app_name) }

    val insets = WindowInsets.systemBars


    Scaffold(
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .nestedScroll(bottomScrollBehavior.nestedScrollConnection),
        topBar = {
            AnimatedContent(
                // Slide in from the top
                targetState = topBarTitle,
                transitionSpec = { slideInHorizontally { it } togetherWith slideOutHorizontally { -it } }
            ) { title ->
                RedCableClubTopBar(
                    scrollBehavior = scrollBehavior,
                    textResource = title
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomNavigation(backStack.lastOrNull()),
                // Slide in from the bottom
                enter = slideInVertically(initialOffsetY = { it }),
                // Slide out to the bottom
                exit = slideOutVertically(targetOffsetY = { it })
            ) {

                RedCableClubNavigationBar(
                    isSelected = { navKey -> backStack.lastOrNull() == navKey },
                    onSelected = { navKey -> backStack.apply { clear(); addLast(navKey) } },
                    scrollBehavior = bottomScrollBehavior
                )


            }
        }
    ){ innerPadding ->
        NavDisplay(
            modifier = modifier.padding(innerPadding),
            backStack = backStack,
            entryDecorators = listOf(
                rememberSceneSetupNavEntryDecorator(),
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
        ),
            entryProvider = entryProvider {
                entry<RedCableClubScreen> {
                    topBarTitle = R.string.app_name
                    RedCableClub(
                        uiState = uiState,
                        onAchievementDetailClick = {
                            backStack.addLast(AchievementScreen)
                        },
                        paddingValues = insets.asPaddingValues(),

                    )
                }

                entry<AchievementScreen> {
                    topBarTitle = R.string.achievements
                    Achievements(
                        achievements = (uiState.userProfileState as ResourceState.Success<UserProfile>).data.achievements,
                    )
                }
            },
            transitionSpec = {
                // Slide in from right when navigating forward
                slideInHorizontally(initialOffsetX = { it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { -it })
            },
            popTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
            predictivePopTransitionSpec = {
                // Slide in from left when navigating back
                slideInHorizontally(initialOffsetX = { -it }) togetherWith
                        slideOutHorizontally(targetOffsetX = { it })
            },
        )
    }
}

fun showBottomNavigation(navKey: NavKey?): Boolean { // Changed NavKey to NavKey?
    return navKey == RedCableClubScreen ||
            navKey == RedCoinsShopScreen ||
            navKey == ServiceDetailScreen
}






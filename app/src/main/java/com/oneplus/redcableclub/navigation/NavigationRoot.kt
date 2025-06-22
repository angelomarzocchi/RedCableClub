package com.oneplus.redcableclub.navigation




import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.oneplus.redcableclub.data.model.UserProfile
import com.oneplus.redcableclub.ui.screens.AchievementsScreen
import com.oneplus.redcableclub.ui.screens.RedCableClubScreen
import com.oneplus.redcableclub.ui.screens.RedCableClubViewModel
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
data object ServiceDetailScreen: NavKey




@OptIn(ExperimentalMaterial3Api::class)
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
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()


    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),

        ),
        entryProvider = entryProvider {
            entry<RedCableClubScreen> {
                RedCableClubScreen(
                    scrollBehavior = scrollBehavior,
                    uiState = uiState,
                    onAchievementDetailClick = {
                        backStack.addLast(AchievementScreen)
                    }
                )
            }

            entry<AchievementScreen> {
                when(uiState.userProfileState) {
                    is ResourceState.Loading -> Text(text = "Loading")
                    is ResourceState.Error -> Text(text = "Error")
                    is ResourceState.Success -> {
                        AchievementsScreen(
                            modifier = modifier,
                            scrollBehavior = scrollBehavior,
                            navigateBack = {backStack.remove(AchievementScreen)},
                            achievements = (uiState.userProfileState as ResourceState.Success<UserProfile>).data.achievements
                        )
                    }
                }
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




package com.oneplus.redcableclub.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.oneplus.redcableclub.RedCableClubApplication
import com.oneplus.redcableclub.data.UserProfileRepository
import com.oneplus.redcableclub.data.model.Achievement
import com.oneplus.redcableclub.ui.utils.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AchievementsUiState(
    val achievementsUiState: ResourceState<List<Achievement>> = ResourceState.Loading,
    val selectedAchievement: Achievement? = null
)

class AchievementsViewModel(
    private val userProfileRepository: UserProfileRepository
): ViewModel() {
    //TODO remove from here and find a way to get this from login when it will be implemented
    private val currentUserId = "AngeloMarzo"
    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState: StateFlow<AchievementsUiState> = _uiState.asStateFlow()

    init {
        getUserProfile(currentUserId)
    }

    fun getUserProfile(username: String) {
        viewModelScope.launch {
            _uiState.update { it.copy( achievementsUiState = ResourceState.Loading) }
            try {
                val userProfile = userProfileRepository.getUserProfile(username, forceRefresh = false)
                _uiState.update { it.copy(
                    achievementsUiState = ResourceState.Success(userProfile.achievements),
                    selectedAchievement = userProfile.achievements.firstOrNull()
                ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(achievementsUiState = ResourceState.Error(e.message)) }
            }
        }
    }

    fun selectAchievement(achievement: Achievement) {
        _uiState.update { it.copy(selectedAchievement = achievement) }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RedCableClubApplication)
                AchievementsViewModel(
                    application.container.userProfileRepository
                )
            }
        }
    }
}
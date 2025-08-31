package com.oneplus.redcableclub.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.oneplus.redcableclub.RedCableClubApplication
import com.oneplus.redcableclub.data.RedCoinsShopRepository
import com.oneplus.redcableclub.data.model.ShopItem
import com.oneplus.redcableclub.ui.utils.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RedCoinsShopUiState(
    val redCoinsShopUiState: ResourceState<List<ShopItem>> = ResourceState.Loading,
)

class RedCoinsShopViewModel(
    private val RedCoinsShopRepository: RedCoinsShopRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(RedCoinsShopUiState())
    val uiState: StateFlow<RedCoinsShopUiState> = _uiState.asStateFlow()


    fun getRedCoinsShopItems() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(redCoinsShopUiState = ResourceState.Loading)
            }
            try {
                val shopItems = RedCoinsShopRepository.getRedCoinsShopItems()
                _uiState.update {
                    it.copy(
                        redCoinsShopUiState = ResourceState.Success(
                            shopItems)
                    ) }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    redCoinsShopUiState = ResourceState.Error(
                        message = e.message)
                ) }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RedCableClubApplication)
                RedCoinsShopViewModel(
                    application.container.redCoinsShopRepository
                )
            }
        }
    }

}
package com.oneplus.redcableclub.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.oneplus.redcableclub.RedCableClubApplication
import com.oneplus.redcableclub.data.AdRepository
import com.oneplus.redcableclub.data.UserProfileRepository
import com.oneplus.redcableclub.data.model.Ad
import com.oneplus.redcableclub.data.model.AmountCoupon
import com.oneplus.redcableclub.data.model.Coupon
import com.oneplus.redcableclub.data.model.CouponFactory
import com.oneplus.redcableclub.data.model.UserProfile
import com.oneplus.redcableclub.ui.utils.ResourceState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class RedCableClubUiState(
     val adsState: ResourceState<List<Ad>> = ResourceState.Loading,
     val discoveryState: ResourceState<List<Ad>> = ResourceState.Loading,
     val userProfileState: ResourceState<UserProfile> = ResourceState.Loading,
)


class RedCableClubViewModel(
    private val userProfileRepository: UserProfileRepository,
    private val adRepository: AdRepository
    ) : ViewModel()  {

        private val _uiState = MutableStateFlow(RedCableClubUiState())
    val uiState: StateFlow<RedCableClubUiState> = _uiState.asStateFlow()

    //TODO remove from here and find a way to get this from login when it will be implemented
    private val currentUserId = "AngeloMarzo"

    init {
        getUserProfile(currentUserId)
        getAds()
        getDiscoverPosts()
    }


    fun getUserProfile(username: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(userProfileState = ResourceState.Loading) }
            try {
                val userProfile = userProfileRepository.getUserProfile(username, forceRefresh = true)
                _uiState.update { it.copy(userProfileState = ResourceState.Success(userProfile)) }
        } catch (e: Exception) {
            _uiState.update { it.copy(userProfileState = ResourceState.Error(e.message)) }
            }
        }
    }


        fun getAds() {
            viewModelScope.launch {
               _uiState.update { it.copy(adsState = ResourceState.Loading) }
                try {
                    val ads = adRepository.getAds()
                    _uiState.update { it.copy(adsState = ResourceState.Success(ads)) }
                } catch (e: Exception) {
                    _uiState.update { it.copy(adsState = ResourceState.Error(e.message)) }

                }
            }
        }

    fun getDiscoverPosts() {
        viewModelScope.launch {
            _uiState.update { it.copy(discoveryState = ResourceState.Loading) }
            try {
                val ads = adRepository.getDiscoverPosts()
                _uiState.update { it.copy(discoveryState = ResourceState.Success(ads)) }
                } catch (e: Exception) {
                _uiState.update { it.copy(discoveryState = ResourceState.Error(e.message)) }
            }
        }
    }

    fun onCouponRedeemed(coupon: Coupon) {
        /*
        in real life scenario we would be calling some API to redeem the coupon
         */
        viewModelScope.launch {
            val wallet = (_uiState.value.userProfileState as ResourceState.Success).data.wallet
             wallet.find { it.code == coupon.code }?.isRedeemed = true
        }
    }

        companion object {
            val Factory: ViewModelProvider.Factory = viewModelFactory {
                initializer {
                    val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RedCableClubApplication)
                    RedCableClubViewModel(
                        application.container.userProfileRepository,
                        application.container.adsRepository
                    )
                }
            }
        }
}
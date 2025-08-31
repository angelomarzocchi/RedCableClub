package com.oneplus.redcableclub.network

import com.oneplus.redcableclub.data.model.Ad
import com.oneplus.redcableclub.data.model.ShopItem
import com.oneplus.redcableclub.data.model.UserProfile
import retrofit2.http.GET


interface RedCableClubApiService {

    @GET("ads")
    suspend fun getAds(): List<Ad>
    @GET("discover")
    suspend fun getDiscoverPosts(): List<Ad>
    @GET("user/{username}")
    suspend fun getUserProfile(username: String): UserProfile
    @GET("store/products")
    suspend fun getStoreProducts(): List<ShopItem>
}
package com.oneplus.redcableclub.data

import android.content.Context
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock


interface AppContainer {
    val adsRepository: AdRepository
    val userProfileRepository: UserProfileRepository
    val redCoinsShopRepository: RedCoinsShopRepository
}

class DefaultAppContainer(): AppContainer {

    override val adsRepository: AdRepository by lazy {
        FakeAdRepository(mockRedCableClubApiService)
    }

    override val userProfileRepository: UserProfileRepository by lazy {
        FakeUserProfileRepository(mockRedCableClubApiService)
    }

    override val redCoinsShopRepository: RedCoinsShopRepository by lazy {
        FakeRedCoinsShopRepository(mockRedCableClubApiService)
    }

    private val mockRedCableClubApiService = RedCableClubApiServiceMock()
    /*
    //in case of real API, implement Retrofit

    private val baseUrl = "https://redcableclub.com/api/"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: RedCableClubApiService by lazy {
        retrofit.create(RedCableClubApiService::class.java)
    }

     */
}
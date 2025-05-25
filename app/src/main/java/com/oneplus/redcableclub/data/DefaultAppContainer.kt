package com.oneplus.redcableclub.data

import android.content.Context
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock


interface AppContainer {
    val adsRepository: AdRepository
    val userProfileRepository: UserProfileRepository
}

class DefaultAppContainer(): AppContainer {

    override val adsRepository: AdRepository by lazy {
        FakeAdRepository(mockRedCableClubApiService)
    }

    override val userProfileRepository: UserProfileRepository by lazy {
        FakeUserProfileRepository(mockRedCableClubApiService)
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
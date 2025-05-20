package com.oneplus.redcableclub.data

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.oneplus.redcableclub.network.RedCableClubApiService
import com.oneplus.redcableclub.network.RedCableClubApiServiceMock
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val adsRepository: AdRepository
    val userProfileRepository: UserProfileRepository
}

class DefaultAppContainer(private val context: Context): AppContainer {

    override val adsRepository: AdRepository by lazy {
        FakeAdRepository(mockRedCableClubApiService)
    }

    override val userProfileRepository: UserProfileRepository by lazy {
        FakeUserProfileRepository(mockRedCableClubApiService)
    }

    private val mockRedCableClubApiService = RedCableClubApiServiceMock(context)
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
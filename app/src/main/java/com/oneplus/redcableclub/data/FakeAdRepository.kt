package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.Ad
import kotlinx.coroutines.flow.Flow
import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.network.RedCableClubApiService
import kotlinx.coroutines.flow.flowOf

class FakeAdRepository(private val redCableClubApiService: RedCableClubApiService): AdRepository {

    override suspend fun getAds(): List<Ad> = redCableClubApiService.getAds()

    override suspend fun getDiscoverPosts(): List<Ad> = redCableClubApiService.getDiscoverPosts()


}
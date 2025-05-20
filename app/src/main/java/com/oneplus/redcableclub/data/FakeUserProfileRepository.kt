package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.network.RedCableClubApiService
import kotlinx.coroutines.flow.flowOf

class FakeUserProfileRepository(private val redCableClubApiService: RedCableClubApiService): UserProfileRepository {

    override suspend fun getUserProfile(username: String) =
        redCableClubApiService.getUserProfile(username)
}
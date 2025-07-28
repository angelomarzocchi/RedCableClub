package com.oneplus.redcableclub.data

import android.util.Log
import com.oneplus.redcableclub.data.model.UserProfile
import com.oneplus.redcableclub.network.RedCableClubApiService
import kotlinx.coroutines.flow.flowOf

class FakeUserProfileRepository(
    private val redCableClubApiService: RedCableClubApiService
): UserProfileRepository {

    private val userProfileCache = mutableMapOf<String, UserProfile>()

    override suspend fun getUserProfile(
        username: String,
        forceRefresh: Boolean
    ): UserProfile {
        val cachedProfile = userProfileCache[username]
        if (cachedProfile != null && !forceRefresh) {
              return cachedProfile
        }
        Log.d("UserProfileRepository", "Fetching fresh user profile for $username from network. ForceRefresh: $forceRefresh")
        val freshProfile = redCableClubApiService.getUserProfile(username)
        userProfileCache[username] = freshProfile
        return freshProfile
    }
}
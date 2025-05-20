package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
   suspend fun getUserProfile(username: String): UserProfile
}
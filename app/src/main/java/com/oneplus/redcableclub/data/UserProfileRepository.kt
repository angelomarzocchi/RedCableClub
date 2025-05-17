package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(username: String): Flow<UserProfile?>;
}
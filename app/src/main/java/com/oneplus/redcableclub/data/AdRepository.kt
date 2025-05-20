package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.Ad
import kotlinx.coroutines.flow.Flow

interface AdRepository {
    suspend fun getAds(): List<Ad>;
    suspend fun getDiscoverPosts(): List<Ad>;
}
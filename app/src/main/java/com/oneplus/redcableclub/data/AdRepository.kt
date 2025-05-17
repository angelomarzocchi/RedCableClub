package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.Ad
import kotlinx.coroutines.flow.Flow

interface AdRepository {
    fun getAds(): Flow<List<Ad>>;
    fun getDiscoverPosts(): Flow<List<Ad>>;
}
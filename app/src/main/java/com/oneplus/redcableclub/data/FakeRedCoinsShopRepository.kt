package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.ShopItem
import com.oneplus.redcableclub.network.RedCableClubApiService

class FakeRedCoinsShopRepository(
    private val redCableClubApiService: RedCableClubApiService
): RedCoinsShopRepository {

    override suspend fun getRedCoinsShopItems(): List<ShopItem> {
       return redCableClubApiService.getStoreProducts()
    }
}
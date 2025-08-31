package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.data.model.ShopItem

interface RedCoinsShopRepository {
    suspend fun getRedCoinsShopItems(): List<ShopItem>
}
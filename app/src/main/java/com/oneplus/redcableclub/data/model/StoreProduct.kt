package com.oneplus.redcableclub.data.model

import com.oneplus.redcableclub.R
import kotlinx.serialization.Serializable


enum class RedCoinsShopCategory(val labelId: Int) {
   PHYSICAL_PRODUCTS(R.string.products),
   COUPONS_AND_SERVICES(R.string.coupons_and_services)
}

@Serializable
sealed interface ShopItem {
   val name: String
   val redCoinsRequired: Int
   val displayCategory: RedCoinsShopCategory
}

@Serializable
data class  KeySellingPoint(
   val title: String,
   val description: String,
   val imageUri: String
)

@Serializable
data class Product(
   override val name: String,
   override val redCoinsRequired: Int,
   override val displayCategory: RedCoinsShopCategory = RedCoinsShopCategory.PHYSICAL_PRODUCTS,
   val imageUrls: List<String>,
   val keySellingPoints: List<KeySellingPoint>,
   val price: Double
) : ShopItem

@Serializable
data class Benefit(
   override val name: String,
   override val redCoinsRequired: Int,
   override val displayCategory: RedCoinsShopCategory = RedCoinsShopCategory.COUPONS_AND_SERVICES,
   val description: String,
   val iconText: String
) : ShopItem
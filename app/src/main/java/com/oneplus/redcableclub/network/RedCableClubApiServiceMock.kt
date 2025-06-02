package com.oneplus.redcableclub.network

import com.oneplus.redcableclub.data.model.Achievement
import com.oneplus.redcableclub.data.model.Ad
import com.oneplus.redcableclub.data.model.CouponFactory
import com.oneplus.redcableclub.data.model.Device
import com.oneplus.redcableclub.data.model.ProductCategory
import com.oneplus.redcableclub.data.model.UserProfile
import kotlinx.coroutines.delay
import java.time.LocalDate

class RedCableClubApiServiceMock(): RedCableClubApiService {

    companion object {
        val userMock = UserProfile(
            username = "AngeloMarzo",
            firstName = "Angelo",
            lastName = "Marzocchi",
            birthday = LocalDate.of(1998, 11, 25),
            email = "angelomarzocchi@proton.me",
            profilePictureUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.userprofile_image),//"android.resource://com.oneplus.redcableclub/drawable/userprofile_image.jpg",
            redExpPoints = 7499,
            redCoins = 1201,
            wallet = listOf(
                CouponFactory.createAmountCoupon(
                    code = "X0001F",
                    description = "€50 off a OnePlus phone",
                    productCategories = listOf(ProductCategory.PHONE),
                    amountOff = 50.0
                ),
                CouponFactory.createPercentageCoupon(
                    code = "X0002F",
                    description = "20% off a OnePlus accessory",
                    productCategories = listOf(ProductCategory.ACCESSORIES),
                    percentageOff = 0.2
                ),
                CouponFactory.createAmountCoupon(
                    code = "X0003F",
                    description = "€50 off a OnePlus tablet",
                    productCategories = listOf(ProductCategory.TABLET),
                    amountOff = 50.0
                ),
                CouponFactory.createPercentageCoupon(
                    code = "X0004F",
                    description = "20% off a OnePlus wearable or audio product",
                    productCategories = listOf(ProductCategory.WEARABLES, ProductCategory.AUDIO),
                    percentageOff = 0.2
                ),
                CouponFactory.createPercentageCoupon(
                    code = "X0005F",
                    description = "10% off any OnePlus product",
                    productCategories = ProductCategory.entries,
                    percentageOff = 0.2
                )
            ),
            devices = listOf(
                Device(name = "OnePlus 13", imei = "0100010001000")
            ),
            achievements = listOf(
                Achievement(
                    name = "OnePlus 13",
                    description = "Pro. Everywhere.",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_13)
                ),
                Achievement(
                    name = "OnePlus 12",
                    description = "Smooth Beyond Belief",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_12)
                ),
                Achievement(
                    name = "11th Anniversary Celebration",
                    description = "Happy 11th Birthday OnePlus",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_11th_anniversary_celebration)
                )
            )
        )

        val discoverPosts = listOf<Ad>(
            Ad("Shot on OnePlus: Faces", getDrawableUri(com.oneplus.redcableclub.R.drawable.faces)),
            Ad("Flex your Flux", getDrawableUri(com.oneplus.redcableclub.R.drawable.flux)),
            Ad("Plus Key", getDrawableUri(com.oneplus.redcableclub.R.drawable.plus))
        )

        val ads = listOf<Ad>(
            Ad("Save €100 (16GB only), plus get 2 free gifts worth up to €104",getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_13_ad)),
            Ad("Save €50, plus get a free gift worth up to €49",getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_13r_ad)),
            Ad("Save €50, plus get FREE earbuds worth up to €79 (Limited stock)",getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_watch_3_ad))
        )

        private fun getDrawableUri(drawableName: Int): String {
            return "android.resource://com.oneplus.redcableclub/drawable/$drawableName"
        }
    }

    private val defaultDelayMills = 1500L

    override suspend fun getAds(): List<Ad> {
        delay(defaultDelayMills)
        return ads
    }

    override suspend fun getDiscoverPosts(): List<Ad> {
        delay(defaultDelayMills + 500L)
        return discoverPosts
    }

    override suspend fun getUserProfile(username: String): UserProfile {
        delay(defaultDelayMills - 500L)
        return userMock
    }

}
package com.oneplus.redcableclub.data

import com.oneplus.redcableclub.R
import com.oneplus.redcableclub.data.model.Achievement
import com.oneplus.redcableclub.data.model.CouponFactory
import com.oneplus.redcableclub.data.model.Device
import com.oneplus.redcableclub.data.model.ProductCategory
import com.oneplus.redcableclub.data.model.UserProfile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDate

class FakeUserProfileRepository: UserProfileRepository {

    val fakeUserProfile = UserProfile(
        username ="AngeloMarzo",
        firstName = "Angelo",
        lastName = "Marzocchi",
        birthday = LocalDate.of(1998,11,25),
        email = "angelomarzocchi@proton.me",
        profilePicture = R.drawable.userprofile_image,
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
            Achievement(name= "OnePlus 13", description = "Pro. Everywhere.", icon = R.drawable.oneplus_13),
            Achievement(name = "OnePlus 12", description = "Smooth Beyond Belief", icon = R.drawable.oneplus_12),
            Achievement(name = "11th Anniversary Celebration", description = "Happy 11th Birthday OnePlus", icon = R.drawable.oneplus_11th_anniversary_celebration)
        )
    )

    override fun getUserProfile(username: String): Flow<UserProfile?> {
        return flowOf(
            fakeUserProfile
                )
    }
}
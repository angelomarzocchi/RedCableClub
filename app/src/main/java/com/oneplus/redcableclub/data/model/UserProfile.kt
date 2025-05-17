package com.oneplus.redcableclub.data.model

import androidx.annotation.DrawableRes
import java.time.LocalDate

data class UserProfile(
    val username: String,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    @DrawableRes val profilePicture: Int,
    val email: String,
    val redExpPoints: Int,
    val redCoins: Int,
    val wallet: List<Coupon>,
    val devices: List<Device>,
    val achievements: List<Achievement>
) {

    init {
        require(redExpPoints >= 0) { "Red Exp Points must be greater than or equal to 0" }
        require(redCoins >= 0) { "Red Coins must be greater than or equal to 0" }
    }
    fun getFullName(): String {
        return "$firstName $lastName"
    }

    fun getMembershipTier(): MembershipTier {
        return MembershipTier.getTierForPoints(redExpPoints)
    }

}
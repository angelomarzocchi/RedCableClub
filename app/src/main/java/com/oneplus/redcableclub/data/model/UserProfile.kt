package com.oneplus.redcableclub.data.model


import java.time.LocalDate

data class UserProfile(
    val username: String,
    val firstName: String,
    val lastName: String,
    val birthday: LocalDate,
    val profilePictureUrl: String,
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

    fun getMembershipTierStatus(membershipTier: MembershipTier): MembershipStatus {
        val currentTier = MembershipTier.getTierForPoints(redExpPoints)
        return if(currentTier.ordinal < membershipTier.ordinal) {
            MembershipStatus.TO_ACHIEVE
        } else if(currentTier.ordinal == membershipTier.ordinal) {
            MembershipStatus.CURRENT_TIER
        } else MembershipStatus.ACHIEVED
    }

}
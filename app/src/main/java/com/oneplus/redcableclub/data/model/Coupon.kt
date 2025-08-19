package com.oneplus.redcableclub.data.model

import java.time.LocalDate

abstract class Coupon(
    val code: String,
    val description: String,
    val productCategories: List<ProductCategory>,
) {
    var isRedeemed = false
    abstract fun calculateDiscount(price: Double): Double

    open fun isValidFor(productCategory: ProductCategory): Boolean {
        return productCategories.contains(productCategory)
    }
}

class AmountCoupon(
    code: String,
    description: String,
    productCategories: List<ProductCategory>,
    val amountOff: Double,
) : Coupon(code, description, productCategories) {
    override fun calculateDiscount(price: Double): Double {
        return if (price >= amountOff) amountOff else price
    }
}

class PercentageCoupon(
    code: String,
    description: String,
    productCategories: List<ProductCategory>,
    val percentageOff: Double,
) : Coupon(code, description, productCategories) {
    init {
        require(percentageOff in 0.0..1.0)
    }

    override fun calculateDiscount(price: Double): Double {
        return price *  percentageOff
    }

}

object CouponFactory {
    fun createAmountCoupon(
        code: String,
        description: String,
        productCategories: List<ProductCategory>,
        amountOff: Double,
        isRedeemed: Boolean = false
    ): Coupon   {
        return AmountCoupon(code, description, productCategories, amountOff)
            .also { it.isRedeemed = isRedeemed }
    }

    fun createPercentageCoupon(
        code: String,
        description: String,
        productCategories: List<ProductCategory>,
        percentageOff: Double,
        isRedeemed: Boolean = false
    ): Coupon {
        return PercentageCoupon(
            code =code,
            description =description,
            productCategories =productCategories,
            percentageOff =percentageOff
        ).also { it.isRedeemed = isRedeemed }
    }
}





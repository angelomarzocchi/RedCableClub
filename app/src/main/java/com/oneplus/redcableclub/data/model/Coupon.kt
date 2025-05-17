package com.oneplus.redcableclub.data.model

import java.time.LocalDate

abstract class Coupon(
    val code: String,
    val description: String,
    val productCategories: List<ProductCategory>
) {
    abstract fun calculateDiscount(price: Double): Double

    open fun isValidFor(productCategory: ProductCategory): Boolean {
        return productCategories.contains(productCategory)
    }
}

class AmountCoupon(
    code: String,
    description: String,
    productCategories: List<ProductCategory>,
     val amountOff: Double
) : Coupon(code, description, productCategories) {
    override fun calculateDiscount(price: Double): Double {
        return if (price >= amountOff) amountOff else price
    }
}

class PercentageCoupon(
    code: String,
    description: String,
    productCategories: List<ProductCategory>,
    val percentageOff: Double
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
        amountOff: Double
    ): Coupon   {
        return AmountCoupon(code, description, productCategories, amountOff)
    }

    fun createPercentageCoupon(
        code: String,
        description: String,
        productCategories: List<ProductCategory>,
        percentageOff: Double
    ): Coupon {
        return PercentageCoupon(code, description, productCategories, percentageOff)
    }
}





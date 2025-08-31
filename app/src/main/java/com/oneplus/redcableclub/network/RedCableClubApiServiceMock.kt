package com.oneplus.redcableclub.network

import com.oneplus.redcableclub.data.model.Achievement
import com.oneplus.redcableclub.data.model.Ad
import com.oneplus.redcableclub.data.model.Benefit
import com.oneplus.redcableclub.data.model.CouponFactory
import com.oneplus.redcableclub.data.model.Device
import com.oneplus.redcableclub.data.model.CouponProductCategory
import com.oneplus.redcableclub.data.model.KeySellingPoint
import com.oneplus.redcableclub.data.model.Product
import com.oneplus.redcableclub.data.model.ShopItem
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
                    productCategories = listOf(CouponProductCategory.PHONE),
                    amountOff = 50.0
                ),
                CouponFactory.createPercentageCoupon(
                    code = "X0002F",
                    description = "20% off a OnePlus accessory",
                    productCategories = listOf(CouponProductCategory.ACCESSORIES),
                    percentageOff = 0.2
                ),
                CouponFactory.createAmountCoupon(
                    code = "X0003F",
                    description = "€50 off a OnePlus tablet",
                    productCategories = listOf(CouponProductCategory.TABLET),
                    amountOff = 50.0
                ),
                CouponFactory.createPercentageCoupon(
                    code = "X0004F",
                    description = "20% off a OnePlus wearable or audio product",
                    productCategories = listOf(CouponProductCategory.WEARABLES, CouponProductCategory.AUDIO),
                    percentageOff = 0.2
                ),
                CouponFactory.createPercentageCoupon(
                    code = "X0005F",
                    description = "10% off any OnePlus product",
                    productCategories = CouponProductCategory.entries,
                    percentageOff = 0.2
                )
            ),
            devices = listOf(
                Device(name = "OnePlus 13", imei = "0100010001000", imageUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.device_oneplus13)),
                Device(name = "OnePlus Pad 3", imei = "0200020002000", imageUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.device_onepluspad3)),
                Device(name = "OnePlus Watch 3 43mm", imei = "0300030003000", imageUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.device_onepluswatch3_43mm))
            ),
            achievements = listOf(
                Achievement(
                    id = 1001,
                    name = "OnePlus 13",
                    description = "Pro. Everywhere.",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_13),
                    isAchieved = true
                ),
                Achievement(
                    id = 1002,
                    name = "OnePlus 13R",
                    description = "Pro. Everywhere.",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_13r),
                    isAchieved = false
                ),
                Achievement(
                    id = 1003,
                    name = "OnePlus 12",
                    description = "Smooth Beyond Belief",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_12),
                    isAchieved = true
                ),
                Achievement(
                    id = 1004,
                    name = "OnePlus 12R",
                    description = "Smooth Beyond Belief",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_12r),
                    isAchieved = false
                ),
                Achievement(
                    id = 1005,
                    name = "11th Anniversary Celebration",
                    description = "Happy 11th Birthday OnePlus",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_11th_anniversary_celebration),
                    isAchieved = true
                ),
                Achievement(
                    id = 1006,
                    name = "OnePlus Nord 4",
                    description = "Absurdly Good",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_nord_4),
                    isAchieved = true
                ),
                Achievement(
                    id = 1007,
                    name = "OnePlus Nord CE4 Lite 5G",
                    description = "Absurdly Entertaining",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_nord_ce4_lite_5g),
                    isAchieved = false
                ),
                Achievement(
                    id = 1008,
                    name = "OnePlus Open",
                    description = "Open for Everything",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_open),
                    isAchieved = false
                ),
                Achievement(
                    id = 1009,
                    name = "OnePlus 10T",
                    description = "Evolve Beyond Speed",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_10t),
                    isAchieved = false
                ),
                Achievement(
                    id = 10010,
                    name = "OnePlus 10 Pro",
                    description = "Capture Every Horizon",
                    iconUrl = getDrawableUri(com.oneplus.redcableclub.R.drawable.oneplus_10_pro),
                    isAchieved = false
                ),


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

        val storeProducts = listOf<ShopItem>(
            Product(
                name = "OnePlus Everyday Sling Bag",
                redCoinsRequired = 750,
                imageUrls = listOf(
                    getDrawableUri(com.oneplus.redcableclub.R.drawable.sling_bag_1),
                    getDrawableUri(com.oneplus.redcableclub.R.drawable.sling_bag_2)
                ),
                price = 69.99,
                keySellingPoints = listOf(
                    KeySellingPoint(
                        title = "Black & Red Vibes, Stylish On The Go",
                        description = "From the great outdoors to the city center, your style goes wherever you are. Classic black with a touch of red, sling it on and start a fresh trend — style is yours to wear",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.sling_bag_ksp_1)
                    ),
                    KeySellingPoint(
                        title = "Stylish Meets Practical, Ready for Every Side of You",
                        description = "Vibrant on the outside, fully equipped on the inside — double-sided storage keeps your essentials close wherever you go.\n\nThis bag offers spacious, multi-compartment storage with one side for your phone, power bank, and daily essentials, the other for dorm keys, wireless earbuds, and small items, while the middle compartment accommodates your water bottle, folding umbrella, and other travel necessities.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.sling_bag_ksp_2)
                    ),
                    KeySellingPoint(
                        title = "Strap with Built-in Charging Cable for Maximum Versatility",
                        description = "The strap is a OnePlus Crossbody Lanyard Built-in TypeC to TypeC Cable. Use it as a sling strap outdoors, then detach it indoors to charge your phone or laptop — one bag, ready for anything.\n\n Dual Type-C ports with 8A high current, supporting up to SUPERVOOC 80W and fully compatible with PD 100W. With a data transfer speed of 480Mbps, charging and file transfers are both lightning fast and effortless.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.sling_bag_ksp_3)
                    ),
                    KeySellingPoint(
                        title = "Solid Shield, Rain Repellence",
                        description = "PU leather surface with SBS waterproof zipper offers splash and water resistance, and even accidental stains wipe clean with ease.\n\nRainproof protection comprehensively guards your digital devices and essentials.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.sling_bag_ksp_4)
                    )
                )
            ),
            Product(
                name = "OnePlus Insulated Coffee Travel Tumbler",
                redCoinsRequired = 375,
                imageUrls = listOf(
                    getDrawableUri(com.oneplus.redcableclub.R.drawable.coffe_travel_tumbler)
                ),
                price = 29.99,
                keySellingPoints = listOf(
                    KeySellingPoint(
                        title = "Engineered design",
                        description = "The cup sleeve boasts a special wood grain design that is both sophisticated and slip-resistant. Paired with the metal body, the overall look and feel make it hard to put down.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.coffe_travel_tumbler_ksp_1)
                    ),
                    KeySellingPoint(
                        title = "Attention to detail",
                        description = "The lid effectively prevents leaks, while the curved rim design enhances your drinking experience. Additionally, the top of the lid features an easy-open mechanism, allowing you to drink anytime, anywhere.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.coffe_travel_tumbler_ksp_2)
                    ),
                    KeySellingPoint(
                        title = "Your personalized accessory",
                        description = "The cup also comes with a detachable strap, making it easy to carry and freeing up your hands. You can also swap it out with different styles of straps to showcase your personal style.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.coffe_travel_tumbler_ksp_3)
                    ),
                    KeySellingPoint(
                        title = "Officially-guaranteed quality",
                        description = "An official product you can trust with solid materials for maximum durability. The silicone detailing at the bottom is designed for non-slip placement, and is wobble-proof and scratch-proof. With meticulous attention to detail, the cup is built with the premium quality of an official accessory.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.coffe_travel_tumbler_ksp_4)
                    ),

                )

            ),
            Product(
                name = "OnePlus Magnetic Card Holder with Stand",
                redCoinsRequired = 250,
                imageUrls = listOf(
                    getDrawableUri(com.oneplus.redcableclub.R.drawable.magnetic_card_holder_1),
                    getDrawableUri(com.oneplus.redcableclub.R.drawable.magnetic_card_holder_2),
                    getDrawableUri(com.oneplus.redcableclub.R.drawable.magnetic_card_holder_3)
                ),
                price = 29.99,
                keySellingPoints = listOf(
                    KeySellingPoint(
                        title = "Vegan leather for long-lasting durability",
                        description = "Soft and skin-friendly, it is crafted with premium material for an excellent hand feel and a comfortable grip that is wear-resistant and light-weight.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.magnetic_card_holder_ksp_1)
                    ),
                    KeySellingPoint(
                        title = "Magnetic card holder with built-in magnetic ring",
                        description = "It can be magnetically attached to your magnetic protective case through the built-in magnetic ring. Easy to carry and convenient to use.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.magnetic_card_holder_ksp_2)
                    ),
                    KeySellingPoint(
                        title = "A card holder and a phone stand",
                        description = "The accessory can organize your cards and be used as a phone stand through its internal folding structure.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.magnetic_card_holder_ksp_3)
                    ),
                    KeySellingPoint(
                        title = "Attention to detail for premium quality",
                        description = "The minimalist aesthetics is simple and elegant. The \"Never Settle\" lettering at the front says something about the owner. The support is adorned with a OnePlus logo for a hint of sophistication.",
                        imageUri = getDrawableUri(com.oneplus.redcableclub.R.drawable.magnetic_card_holder_ksp_4)
                    )
                )
            ),
            Benefit(
                name = "10€ off OnePlus Devices",
                redCoinsRequired = 750,
                description = "Get a 10€ off any OnePlus device you buy."
            ),
            Benefit(
                name = "30% off OnePlus Audio Devices",
                redCoinsRequired = 500,
                description = "Get a 30% off any OnePlus Audio device you buy."
            )
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

    override suspend fun getStoreProducts(): List<ShopItem> {
        delay(defaultDelayMills - 700)
        return storeProducts
    }



}
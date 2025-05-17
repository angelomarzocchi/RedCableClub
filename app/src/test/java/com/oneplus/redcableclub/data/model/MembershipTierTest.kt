package com.oneplus.redcableclub.data.model

import org.junit.Test
import kotlin.test.assertEquals

class MembershipTierTest {

    @Test
    fun `getTierForPoints   Explorer tier`() {
        // Test that points within the EXPLORER tier range returns EXPLORER.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   Bronze tier`() {
        // Test that points within the BRONZE tier range returns BRONZE.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   Silver tier`() {
        // Test that points within the SILVER tier range returns SILVER.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   Gold tier`() {
        // Test that points within the GOLD tier range returns GOLD.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   Supreme tier`() {
        // Test that points within the SUPREME tier range returns SUPREME.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   below min points`() {
        // Test that points below the minimum points of the EXPLORER tier returns EXPLORER.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   exactly min points of Explorer`() {
        // Test that points equals to the minimum points of the EXPLORER tier returns EXPLORER.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   exactly max points of Supreme`() {
        // Test that points equals to the maximum points of the SUPREME tier returns SUPREME.
        // TODO implement test
    }

    @Test
    fun `getTierForPoints   over max points`() {
        // Test that points above the maximum points of the SUPREME tier returns SUPREME.
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   Explorer to Insider`() {
        // Test progress calculation for a point value within the EXPLORER tier, 
        // transitioning to Insider.
        assertEquals(0.25f,MembershipTier.computeProgressToNextTier(500))
    }

    @Test
    fun `computeProgressToNextTier   Insider to Elite`() {
        // Test progress calculation for a point value within the BRONZE tier,
        // transitioning to SILVER.
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   Elite to Supreme`() {
        // Test progress calculation for a point value within the GOLD tier,
        // transitioning to SUPREME.
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   Supreme no next tier`() {
        // Test that progress is 1.0f when points are within the SUPREME tier,
        // as there's no next tier.
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   min points Explorer`() {
        // Test progress calculation for the minimum points of the EXPLORER tier.
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   exactly max points of Gold`() {
        // Test progress calculation for when points is exactly at the maximum value 
        // of gold tier to ensure 100% progress to supreme.
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   max points Explorer`() {
        // Test progress calculation for the maximum points of the EXPLORER tier.
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   negative points`() {
        // Test progress calculation for negative points. should behave same as 
        //min points of Explorer
        // TODO implement test
    }

    @Test
    fun `computeProgressToNextTier   over max points`() {
        // Test progress calculation for when points are over the maximum for Supreme.
        //should behave same as max points of supreme
        // TODO implement test
    }

}
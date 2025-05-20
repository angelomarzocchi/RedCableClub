package com.oneplus.redcableclub.data.model

enum class MembershipTier(val label: String,val minPoints: Int, val maxPoints: Int) {
    EXPLORER("Explorer",0,1999),
    INSIDER("Insider",2000,4999),
    ELITE("Elite",5000,9999),
    SUPREME("Supreme",10000, Int.MAX_VALUE);

    companion object {
        fun getTierForPoints(points: Int) : MembershipTier {
            return MembershipTier.entries.firstOrNull {
                points in it.minPoints..it.maxPoints
            } ?: EXPLORER
        }

        fun computeProgressToNextTier(points: Int) : Float {
            val currentTier = getTierForPoints(points)
            if (currentTier == SUPREME)
                return 1.0f

            val nextTier = MembershipTier.entries.getOrNull(
                MembershipTier.entries.indexOf(currentTier) + 1
            ) ?: SUPREME

            val pointsToNextTier = nextTier.minPoints - points

            val progress = 1 - (pointsToNextTier.toFloat() / (nextTier.minPoints - currentTier.minPoints))
            return progress.coerceIn(0.0f, 1.0f)
        }
    }
}
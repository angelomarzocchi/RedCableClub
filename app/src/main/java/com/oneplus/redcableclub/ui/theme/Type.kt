package com.oneplus.redcableclub.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.oneplus.redcableclub.R

// Set of Material typography styles to start with
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)


val OnePlusSans = FontFamily(
    Font(
        R.font.oneplus_sans_regular)
)

// Default Material 3 typography values
val baseline = Typography()
/*
val AppTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = OnePlusSans, fontWeight = FontWeight.Bold),
    displayMedium = baseline.displayMedium.copy(fontFamily = OnePlusSans),
    displaySmall = baseline.displaySmall.copy(fontFamily = OnePlusSans),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = OnePlusSans),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = OnePlusSans),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = OnePlusSans),
    titleLarge = baseline.titleLarge.copy(fontFamily = OnePlusSans),
    titleMedium = baseline.titleMedium.copy(fontFamily = OnePlusSans),
    titleSmall = baseline.titleSmall.copy(fontFamily = OnePlusSans),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = OnePlusSans),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = OnePlusSans),
    bodySmall = baseline.bodySmall.copy(fontFamily = OnePlusSans),
    labelLarge = baseline.labelLarge.copy(fontFamily = OnePlusSans),
    labelMedium = baseline.labelMedium.copy(fontFamily = OnePlusSans),
    labelSmall = baseline.labelSmall.copy(fontFamily = OnePlusSans),
)
 */
val AppTypography = Typography(
    // Display styles (very large, prominent text)
    displayLarge = baseline.displayLarge.copy(
        fontFamily = OnePlusSans
    ),
    displayMedium = baseline.displayMedium.copy(
        fontFamily = OnePlusSans
    ),
    displaySmall = baseline.displaySmall.copy(
        fontFamily = OnePlusSans
    ),

    // Headline styles (large headings)
    headlineLarge = baseline.headlineLarge.copy(
        fontFamily = OnePlusSans
    ),
    headlineMedium = baseline.headlineMedium.copy(
        fontFamily = OnePlusSans
    ),
    headlineSmall = baseline.headlineSmall.copy(
        fontFamily = OnePlusSans
    ),

    // Title styles (subheadings, toolbar titles)
    titleLarge = baseline.titleLarge.copy(
        fontFamily = OnePlusSans,
    ),
    titleMedium = baseline.titleMedium.copy(
        fontFamily = OnePlusSans,
    ),
    titleSmall = baseline.titleSmall.copy(
        fontFamily = OnePlusSans
    ),

    // Body styles (main content text)
    bodyLarge = baseline.bodyLarge.copy(
        fontFamily = OnePlusSans
    ),
    bodyMedium = baseline.bodyMedium.copy(
        fontFamily = OnePlusSans
    ),
    bodySmall = baseline.bodySmall.copy(
        fontFamily = OnePlusSans
    ),

    // Label styles (small, functional text like buttons, captions)
    labelLarge = baseline.labelLarge.copy(
        fontFamily = OnePlusSans
    ),
    labelMedium = baseline.labelMedium.copy(
        fontFamily = OnePlusSans
    ),
    labelSmall = baseline.labelSmall.copy(
        fontFamily = OnePlusSans
    )
)


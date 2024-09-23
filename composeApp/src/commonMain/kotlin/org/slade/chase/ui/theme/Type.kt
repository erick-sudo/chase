package org.slade.chase.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import chase.composeapp.generated.resources.Inter_italic_variable_font_opsz_wght
import chase.composeapp.generated.resources.Inter_variable_font_opsz_wght
import chase.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font

// Default Material 3 typography values
private val baseline = Typography()

@Composable
fun AppTypography(): Typography {

    val displayFontFamily = FontFamily(
        Font(
            resource = Res.font.Inter_variable_font_opsz_wght,
            style = FontStyle.Normal,
        ),
        Font(
            resource = Res.font.Inter_italic_variable_font_opsz_wght,
            style = FontStyle.Italic,
        )
    )

    val bodyFontFamily = FontFamily(
        Font(
            resource = Res.font.Inter_variable_font_opsz_wght,
            style = FontStyle.Normal
        ),
        Font(
            resource = Res.font.Inter_italic_variable_font_opsz_wght,
            style = FontStyle.Italic,
        )
    )

    return Typography(
        displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
        displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
        displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
        headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
        headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
        headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
        titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
        titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
        titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
        bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
        bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
        bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
        labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
        labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
        labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
    )
}


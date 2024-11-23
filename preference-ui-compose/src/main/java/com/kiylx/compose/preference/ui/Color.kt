package com.kiylx.compose.preference.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.kiylx.common.lib_materialcolorutilities.blend.Blend


fun Color.applyAlpha(alpha: Float): Color {
    return this.copy(alpha = alpha)
}

fun Color.applyOpacity(enabled: Boolean): Color {
    if (this == Color.Transparent)
        return this
    return if (enabled) this else this.applyAlpha(alpha = 0.62f)
}

@Composable
fun Color.harmonizeWith(other: Color) =
    Color(Blend.harmonize(this.toArgb(), other.toArgb()))

@Composable
fun Color.harmonizeWithPrimary(): Color =
    this.harmonizeWith(other = MaterialTheme.colorScheme.primary)

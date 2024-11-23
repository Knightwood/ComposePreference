package com.kiylx.compose.preference.ui

import androidx.compose.foundation.Indication
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalUseFallbackRippleImplementation
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

class RippleClickable {

}

@Suppress("DEPRECATION_ERROR")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun rippleOrFallbackImplementation(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified
): Indication {
    return if (LocalUseFallbackRippleImplementation.current) {
        androidx.compose.material.ripple.rememberRipple(bounded, radius, color)
    } else {
        ripple(bounded, radius, color)
    }
}
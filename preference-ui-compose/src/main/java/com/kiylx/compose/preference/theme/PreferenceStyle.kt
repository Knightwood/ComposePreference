package com.kiylx.compose.preference.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.ui.applyOpacity

/**
 * @property boxStyle
 * @property iconStyle
 * @property descMaxLine 描述的最大行数
 */
data class PreferenceStyle(
    val boxStyle: PreferenceBoxStyle,
    val iconStyle: PreferenceIconStyle,
    val titleMaxLine: Int = 1,
    val descMaxLine: Int = 2,
)

//<editor-fold desc="整个外框的样式">
data class PreferenceBoxStyle(
    val shape: Shape = RectangleShape,
    val color: Color,
    val contentColor: Color,
    val tonalElevation: Dp = 0.dp,
    val shadowElevation: Dp = 0.dp,
    val border: BorderStroke? = null,
)

val defaultPreferenceBoxStyle
    @Composable
    get() = PreferenceBoxStyle(
        color = MaterialTheme.colorScheme.surface,
        contentColor = contentColorFor(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    )

//</editor-fold>

//<editor-fold desc="item前部图标样式">
data class PreferenceIconStyle(
    val shape: Shape = CircleShape,
    val paddingValues: PaddingValues = PaddingValues(0.dp),
    val backgroundColor: Color = Color.Transparent,
    val tint: Color = Color.Unspecified
) {
    fun fixEnabledTint(enabled: Boolean): Color {
        return tint.applyOpacity(enabled)
    }

    fun fixEnabledBackgroundColor(enabled: Boolean): Color {
        return backgroundColor.applyOpacity(enabled)
    }
}

val defaultIconStyle
    @Composable
    get() = PreferenceIconStyle(
        tint = LocalContentColor.current
    )

//</editor-fold>

internal val LocalPreferenceStyle = compositionLocalOf<PreferenceStyle> {
    PreferenceStyle(
        PreferenceBoxStyle(color = Color.Unspecified, contentColor = Color.Unspecified),
        PreferenceIconStyle()
    )
}

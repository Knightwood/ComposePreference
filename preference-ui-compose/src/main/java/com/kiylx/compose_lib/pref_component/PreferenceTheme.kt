package com.kiylx.compose_lib.pref_component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiylx.compose_lib.pref_component.PreferenceDimenTokens.large_x
import com.kiylx.compose_lib.pref_component.PreferenceDimenTokens.medium
import com.kiylx.compose_lib.pref_component.PreferenceDimenTokens.small

//<editor-fold desc="快速得到paddings">

val PaddingValues.start: Dp
    @Composable get() = calculateStartPadding(LocalLayoutDirection.current)
val PaddingValues.end: Dp
    @Composable get() = calculateEndPadding(LocalLayoutDirection.current)
val PaddingValues.top: Dp
    @Composable get() = calculateTopPadding()
val PaddingValues.bottom: Dp
    @Composable get() = calculateBottomPadding()


//</editor-fold>

//<editor-fold desc="主题">
object PreferenceTheme {
    val defaultTypography
        @Composable
        get() = PreferenceTypography(
            titleLarge = PreferenceTypographyTokens.titleLarge,
            titleMedium = PreferenceTypographyTokens.titleMedium,
            titleSmall = PreferenceTypographyTokens.titleSmall,

            bodyLarge = PreferenceTypographyTokens.bodyLarge,
            bodyMedium = PreferenceTypographyTokens.bodyMedium,
            bodySmall = PreferenceTypographyTokens.bodySmall,

            labelLarge = PreferenceTypographyTokens.labelLarge,
            labelMedium = PreferenceTypographyTokens.labelLarge,
            labelSmall = PreferenceTypographyTokens.labelSmall,
        )

    @Composable
    fun SetTheme(
//        preferenceColors: PreferenceColors = PreferenceColors(),
        preferenceTypography: PreferenceTypography = defaultTypography,
        preferenceElevation: PreferenceElevation = PreferenceElevation(),
        preferenceDimens: PreferenceDimens = PreferenceDimens(),
        content: @Composable () -> Unit
    ) {
        CompositionLocalProvider(
//            LocalPreferenceColors provides preferenceColors,
            LocalPreferenceTypography provides preferenceTypography,
            LocalPreferenceElevation provides preferenceElevation,
            LocalPreferenceDimens provides preferenceDimens,
        ) {
            content()
        }

    }

    //    val colors: PreferenceColors
//        @Composable
//        get() = LocalPreferenceColors.current

    val typography: PreferenceTypography
        @Composable
        get() {
            if (LocalPreferenceTypography.current.titleMedium == TextStyle.Default) {
                return defaultTypography
            }
            return LocalPreferenceTypography.current
        }

    val elevation: PreferenceElevation
        @Composable
        get() = LocalPreferenceElevation.current

    val preferenceDimens: PreferenceDimens
        @Composable
        get() = LocalPreferenceDimens.current

    /**
     * 最普通的preferenceItem文本样式
     */
    val normalTextStyle: PreferenceTextStyle
        @Composable
        get() = PreferenceTextStyle(
            typography.titleMedium,
            typography.bodyMedium,
            typography.labelMedium,
        )

    /**
     * 大标题类型的preferenceItem文本样式
     */
    val largeTextStyle: PreferenceTextStyle
        @Composable
        get() = PreferenceTextStyle(
            typography.titleLarge,
            typography.bodyMedium,
            typography.labelMedium,
        )

    /**
     *  小标题类型的preferenceItem文本样式
     */
    val smallTextStyle: PreferenceTextStyle
        @Composable
        get() = PreferenceTextStyle(
            typography.labelLarge,
            typography.bodyMedium,
            typography.labelSmall,
        )
}

//</editor-fold>


//<editor-fold desc="数据类">
/**
 *  有左侧图标，右侧图标时，水平间距的构成：
 *  [boxItem]+[startItem]+图标+[startItem]+中间文本+[mediumBox]+[endItem]+图标+[endItem]+[boxItem]
 *  没有左侧图标，有右侧图标时，水平间距的构成：
 *  [boxItem]+[mediumBox]+中间文本+[mediumBox]+[endItem]+图标+[endItem]+[boxItem]
 *  没有左侧图标，没有右侧图标时，水平间距的构成：
 *  [boxItem]+[mediumBox]+中间文本+[mediumBox]+[boxItem]
 *
 *
 * @property boxItem PaddingValues
 * @property boxItemVert PaddingValues
 * @property iconSize Dp
 * @property startItem PaddingValues
 * @property mediumBox PaddingValues
 * @property endItem PaddingValues
 * @constructor
 */
@Immutable
data class PreferenceDimens(
    /**
     * 普通的item
     */
    val boxItem: PaddingValues = PaddingValues(
        horizontal = PreferenceDimenTokens.medium.dp,
        vertical = PreferenceDimenTokens.small.dp
    ),

    /**
     * 大块的card
     */
    val boxItemVert: PaddingValues = PaddingValues(
        horizontal = PreferenceDimenTokens.large_x.dp,
        vertical = PreferenceDimenTokens.medium.dp
    ),

    val iconSize: Dp = PreferenceDimenTokens.large_x.dp,

    val startItem: PaddingValues = PaddingValues(
        start = medium.dp,
        end = medium.dp,
    ),

    val mediumBox: PaddingValues = PaddingValues(
        start = medium.dp,
        end = medium.dp,
        top = 0.dp,
        bottom = 0.dp
    ),

    val endItem: PaddingValues = PaddingValues(
        start = large_x.dp,
        end = small.dp,
    )
)

@Immutable
data class PreferenceTypography(
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,

    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,

    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)

@Immutable
data class PreferenceTextStyle(
    val title: TextStyle,
    val body: TextStyle,
    val label: TextStyle,
)

//@Immutable
//data class PreferenceColors(
//    val content: Color,
//    val component: Color,
//    val background: List<Color>
//)

@Immutable
data class PreferenceElevation(
    val default: Dp = 0.dp,
    val pressed: Dp = 0.dp
)
//</editor-fold>

//<editor-fold desc="Local变量">


val LocalPreferenceDimens = compositionLocalOf {
    PreferenceDimens()
}

val LocalPreferenceTypography = compositionLocalOf {
    PreferenceTypography(
        titleLarge = TextStyle.Default,
        titleMedium = TextStyle.Default,
        titleSmall = TextStyle.Default,

        bodyLarge = TextStyle.Default,
        bodyMedium = TextStyle.Default,
        bodySmall = TextStyle.Default,

        labelLarge = TextStyle.Default,
        labelSmall = TextStyle.Default,
        labelMedium = TextStyle.Default,
    )
}

//val LocalPreferenceColors = compositionLocalOf {
//    PreferenceColors(
//        content = Color.Unspecified,
//        component = Color.Unspecified,
//        background = emptyList()
//    )
//}
val LocalPreferenceElevation = compositionLocalOf {
    PreferenceElevation(
        default = Dp.Unspecified,
        pressed = Dp.Unspecified
    )
}


//</editor-fold>
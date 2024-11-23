package com.kiylx.compose.preference.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

object Preferences {

    val dimens
        @Composable
        get() = LocalPreferenceDimens.current

    val textStyle
        @Composable
        get() =
            LocalPreferenceTextStyle.current

    val boxStyle
        @Composable
        get() = LocalPreferenceStyle.current.boxStyle

    val iconStyle
        @Composable
        get() = LocalPreferenceStyle.current.iconStyle

    /**
     * 快速修改当前使用的preference各个样式
     *
     * @param boxStyleProvider
     * @param iconStyleProvider
     * @param textStyleProvider
     * @param dimenProvider
     * @param content
     */
    @Composable
    fun CopyTheme(
        boxStyleProvider: @Composable PreferenceBoxStyle.() -> PreferenceBoxStyle = { this },
        iconStyleProvider: @Composable PreferenceIconStyle.() -> PreferenceIconStyle = { this },
        textStyleProvider: @Composable PreferenceTextStyle.() -> PreferenceTextStyle = { this },
        dimenProvider: @Composable PreferenceDimen.() -> PreferenceDimen = { this },
        content: @Composable () -> Unit,
    ) {
        SetTheme(
            boxStyle = LocalPreferenceStyle.current.boxStyle.boxStyleProvider(),
            iconStyle = LocalPreferenceStyle.current.iconStyle.iconStyleProvider(),
            textStyle = LocalPreferenceTextStyle.current.textStyleProvider(),
            dimen = LocalPreferenceDimens.current.dimenProvider(),
            content = content
        )
    }

    /**
     * 设置preference样式
     *
     * @param boxStyle
     * @param iconStyle
     * @param textStyle
     * @param dimen
     * @param content
     */
    @Composable
    fun SetTheme(
        boxStyle: PreferenceBoxStyle = defaultPreferenceBoxStyle,
        iconStyle: PreferenceIconStyle = defaultIconStyle,
        textStyle: PreferenceTextStyle = defaultPreferenceTextStyle,
        dimen: PreferenceDimen = PreferenceDimen(),
        content: @Composable () -> Unit,
    ) {
        CompositionLocalProvider(
            LocalPreferenceDimens provides dimen,
            LocalPreferenceTextStyle provides textStyle,
            LocalPreferenceStyle provides PreferenceStyle(boxStyle, iconStyle),
            content = content
        )
    }
}
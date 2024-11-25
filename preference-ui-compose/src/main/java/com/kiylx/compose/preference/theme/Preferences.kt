package com.kiylx.compose.preference.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

object Preferences {
    val style
        @Composable
        get() = LocalPreferenceStyle.current

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
        titleMaxLine: Int = 1,
        descMaxLine: Int = 2,
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
            content = content, titleMaxLine = titleMaxLine, descMaxLine = descMaxLine
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
        titleMaxLine: Int = 1,
        descMaxLine: Int = 2,
        boxStyle: PreferenceBoxStyle = defaultPreferenceBoxStyle,
        iconStyle: PreferenceIconStyle = defaultIconStyle,
        textStyle: PreferenceTextStyle = defaultPreferenceTextStyle,
        dimen: PreferenceDimen = PreferenceDimen(),
        content: @Composable () -> Unit,
    ) {
        CompositionLocalProvider(
            LocalPreferenceDimens provides dimen,
            LocalPreferenceTextStyle provides textStyle,
            LocalPreferenceStyle provides PreferenceStyle(
                boxStyle = boxStyle,
                iconStyle = iconStyle,
                titleMaxLine = titleMaxLine,
                descMaxLine = descMaxLine
            ),
            content = content
        )
    }
}
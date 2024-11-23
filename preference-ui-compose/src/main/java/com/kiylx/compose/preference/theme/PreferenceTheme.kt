package com.kiylx.compose.preference.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

object PreferenceTheme {

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
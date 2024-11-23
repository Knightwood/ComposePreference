package com.kiylx.compose.preference.component.auto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import com.kiylx.compose.preference.theme.LocalPreferenceDimens
import com.kiylx.compose.preference.theme.LocalPreferenceStyle
import com.kiylx.compose.preference.theme.LocalPreferenceTextStyle
import com.kiylx.compose.preference.theme.PreferenceBoxStyle
import com.kiylx.compose.preference.theme.PreferenceDimen
import com.kiylx.compose.preference.theme.PreferenceIconStyle
import com.kiylx.compose.preference.theme.PreferenceStyle
import com.kiylx.compose.preference.theme.PreferenceTextStyle
import com.kiylx.compose.preference.theme.Preferences
import com.kiylx.compose.preference.theme.defaultIconStyle
import com.kiylx.compose.preference.theme.defaultPreferenceBoxStyle
import com.kiylx.compose.preference.theme.defaultPreferenceTextStyle
import com.kiylx.libx.pref_component.core.DefaultPreferenceHolder
import com.kiylx.libx.pref_component.core.PreferenceHolder


//持有偏好值
val LocalPrefs = compositionLocalOf<PreferenceHolder> {
    DefaultPreferenceHolder.instance()
}


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
fun Preferences.CopyTheme(
    boxStyleProvider: @Composable PreferenceBoxStyle.() -> PreferenceBoxStyle = { this },
    iconStyleProvider: @Composable PreferenceIconStyle.() -> PreferenceIconStyle = { this },
    textStyleProvider: @Composable PreferenceTextStyle.() -> PreferenceTextStyle = { this },
    dimenProvider: @Composable PreferenceDimen.() -> PreferenceDimen = { this },
    holder: PreferenceHolder = LocalPrefs.current,
    content: @Composable () -> Unit,
) {
    SetTheme(
        boxStyle = LocalPreferenceStyle.current.boxStyle.boxStyleProvider(),
        iconStyle = LocalPreferenceStyle.current.iconStyle.iconStyleProvider(),
        textStyle = LocalPreferenceTextStyle.current.textStyleProvider(),
        dimen = LocalPreferenceDimens.current.dimenProvider(),
        holder = holder,
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
fun Preferences.SetTheme(
    boxStyle: PreferenceBoxStyle = defaultPreferenceBoxStyle,
    iconStyle: PreferenceIconStyle = defaultIconStyle,
    textStyle: PreferenceTextStyle = defaultPreferenceTextStyle,
    dimen: PreferenceDimen = PreferenceDimen(),
    holder: PreferenceHolder = remember {
        DefaultPreferenceHolder.instance()
    },
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalPreferenceDimens provides dimen,
        LocalPreferenceTextStyle provides textStyle,
        LocalPreferenceStyle provides PreferenceStyle(boxStyle, iconStyle),
        LocalPrefs provides holder,
        content = content
    )
}
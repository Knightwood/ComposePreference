package com.kiylx.compose.preference.component.cross

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiylx.compose.preference.theme.PreferenceTheme
import com.kiylx.compose.preference.theme.fixEnabledColor
import com.kiylx.compose.preference.ui.ParseIcon
import com.kiylx.compose.preference.ui.PreferenceRow


@Composable
fun PreferenceSubTitle(
    modifier: Modifier = Modifier,
    title: String,
    icon: Any? = null,
) {
    PreferenceRow(
        modifier = modifier,
        start = if (icon != null) {
            {
                val style = PreferenceTheme.iconStyle
                ParseIcon(
                    tint = style.tint,
                    model = icon,
                    shape = style.shape,
                    paddingValues = style.paddingValues,
                    backgroundColor = style.backgroundColor,
                    contentDescription = "icon"
                )
            }
        } else null,
        title = {
            Text(
                title,
                style = PreferenceTheme.textStyle.categoryTextStyle,
            )
        },
        enabled = false,
    )
}

@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    PreferenceRow(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        start = if (icon != null) {
            {
                val style = PreferenceTheme.iconStyle
                ParseIcon(
                    tint = style.fixEnabledTint(enabled),
                    model = icon,
                    shape = style.shape,
                    paddingValues = style.paddingValues,
                    backgroundColor = style.fixEnabledBackgroundColor(enabled),
                    contentDescription = "icon"
                )
            }
        } else null,
        title = {
            Text(
                title,
                style = PreferenceTheme.textStyle.titleStyle.fixEnabledColor(enabled),
            )
        },
        description = if (desc != null) {
            {
                Text(
                    desc,
                    style = PreferenceTheme.textStyle.descriptionTextStyle.fixEnabledColor(enabled),
                    maxLines = 1
                )
            }
        } else null,
        end = end
    )
}


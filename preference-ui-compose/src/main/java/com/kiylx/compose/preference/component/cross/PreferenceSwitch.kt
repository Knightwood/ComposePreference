package com.kiylx.compose.preference.component.cross

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.kiylx.compose.preference.theme.PreferenceTheme
import com.kiylx.compose.preference.theme.PreferenceTheme.dimens
import com.kiylx.compose.preference.theme.fixEnabledColor
import com.kiylx.compose.preference.ui.ComposeSwitch
import com.kiylx.compose.preference.ui.ParseIcon
import com.kiylx.compose.preference.ui.PreferenceRow

@Composable
fun PreferenceSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    PreferenceRow(
        modifier = modifier,
        enabled = enabled,
        onClick = {
            onCheckedChange?.invoke(!isChecked)
        },
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
        end = {
            ComposeSwitch(isChecked = isChecked, onCheckedChange = onCheckedChange)
        }
    )
}


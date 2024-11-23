package com.kiylx.compose.preference.component.cross

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.theme.Preferences
import com.kiylx.compose.preference.ui.ComposeSwitch
import com.kiylx.compose.preference.ui.SamplePreference
import com.kiylx.compose.preference.ui.harmonizeWithPrimary

@Composable
fun PreferenceSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    onChecked: ((Boolean) -> Unit)? = null,
) {
    SamplePreference(
        modifier = modifier,
        title = title, icon = icon, desc = desc,
        enabled = enabled, onClick = {
            onChecked?.invoke(!isChecked)
        },
        end = {
            ComposeSwitch(isChecked = isChecked, onCheckedChange = onChecked)
        }
    )
}


@Composable
fun PreferenceSwitchWithContainer(
    modifier: Modifier = Modifier,
    boxMarginValues: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp
    ),
    color: Color = MaterialTheme.colorScheme.errorContainer.harmonizeWithPrimary(),
    shape: Shape = RoundedCornerShape(28.dp),
    isChecked: Boolean,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    onChecked: ((Boolean) -> Unit)? = null,
) {
    Preferences.CopyTheme(
        dimenProvider = { copy(boxMarginValues = boxMarginValues) },
        boxStyleProvider = { copy(color = color, shape = shape) }
    ) {
        PreferenceSwitch(
            modifier = modifier,
            isChecked = isChecked,
            title = title,
            icon = icon,
            desc = desc,
            enabled = enabled,
            onChecked = onChecked
        )
    }

}


@Composable
fun PreferenceWithDividerSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit) = {},
    onChecked: ((Boolean) -> Unit)? = null,
) {
    SamplePreference(
        modifier = modifier,
        title = title, icon = icon, desc = desc,
        enabled = enabled, onClick = onClick,
        end = {
            Row(
                modifier = Modifier
                    .height(IntrinsicSize.Min)
                    .align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                VerticalDivider(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(end = 16.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                    thickness = 2f.dp
                )
                ComposeSwitch(isChecked = isChecked, onCheckedChange = onChecked)
            }
        }
    )

}


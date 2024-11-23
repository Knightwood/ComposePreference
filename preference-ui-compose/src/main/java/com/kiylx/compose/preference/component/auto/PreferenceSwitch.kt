package com.kiylx.compose.preference.component.auto

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.ui.harmonizeWithPrimary
import com.kiylx.compose.preference.component.cross.PreferenceSwitch as FossPreferenceSwitch
import com.kiylx.compose.preference.component.cross.PreferenceSwitchWithContainer as FossPreferenceSwitchWithContainer
import com.kiylx.compose.preference.component.cross.PreferenceWithDividerSwitch as FossPreferenceWithDividerSwitch


@Composable
fun PreferenceSwitch(
    modifier: Modifier = Modifier,
    defaultValue: Boolean = false,
    title: String,
    dependenceKey: String?,
    keyName: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    onChecked: ((Boolean) -> Unit)? = null,
) {
    PreferenceNodeBase(
        dependenceKey = dependenceKey,
        keyName = keyName, defaultValue = defaultValue,
        enabled = enabled
    ) { scope, state, provider, writer ->
        val prefValue = provider()

        var checked by remember {
            mutableStateOf(defaultValue)
        }
        remember(prefValue) {
            if (prefValue != checked)
                checked = prefValue
            onChecked?.invoke(prefValue)
            provider
        }

        FossPreferenceSwitch(
            modifier = modifier,
            isChecked = checked,
            title = title,
            icon = icon,
            desc = desc,
            enabled = state,
            onChecked = {
                checked = it
                writer.invoke(it)
            }
        )
    }
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
    defaultValue: Boolean,
    dependenceKey: String?,
    keyName: String,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    onChecked: ((Boolean) -> Unit)? = null,
) {
    PreferenceNodeBase(
        dependenceKey = dependenceKey,
        keyName = keyName, defaultValue = defaultValue,
        enabled = enabled
    ) { scope, state, provider, writer ->
        val prefValue = provider()

        var checked by remember {
            mutableStateOf(defaultValue)
        }
        remember(prefValue) {
            if (prefValue != checked)
                checked = prefValue
            onChecked?.invoke(prefValue)
            provider
        }

        FossPreferenceSwitchWithContainer(
            modifier = modifier, isChecked = checked,
            boxMarginValues = boxMarginValues, color = color, shape = shape,
            title = title, icon = icon, desc = desc, enabled = state,
            onChecked = {
                checked = it
                writer.invoke(it)
            },
        )
    }
}

@Composable
fun PreferenceWithDividerSwitch(
    modifier: Modifier = Modifier,
    defaultValue: Boolean,
    dependenceKey: String?,
    keyName: String,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    onClick: (() -> Unit) = {},
    onChecked: ((Boolean) -> Unit)? = null,
) {
    PreferenceNodeBase(
        dependenceKey = dependenceKey,
        keyName = keyName, defaultValue = defaultValue,
        enabled = enabled
    ) { scope, state, provider, writer ->
        val prefValue = provider()

        var checked by remember {
            mutableStateOf(defaultValue)
        }
        remember(prefValue) {
            if (prefValue != checked)
                checked = prefValue
            onChecked?.invoke(prefValue)
            provider
        }

        FossPreferenceWithDividerSwitch(
            modifier = modifier,
            isChecked = checked,
            title = title,
            icon = icon,
            desc = desc,
            enabled = state,
            onClick = onClick,
            onChecked = {
                checked = it
                writer.invoke(it)
            }
        )
    }
}

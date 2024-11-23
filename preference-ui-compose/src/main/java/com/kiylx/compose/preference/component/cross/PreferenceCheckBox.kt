package com.kiylx.compose.preference.component.cross

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiylx.compose.preference.ui.SamplePreference

@Composable
fun PreferenceCheckBox(
    modifier: Modifier = Modifier,
    title: String,
    desc: String? = null,
    checked: Boolean,
    enabled: Boolean = true,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onChecked: (Boolean) -> Unit = {},
) = SamplePreference(
    modifier = modifier,
    title = title, desc = desc, icon = Any(), enabled = enabled,
    onClick = {
        onChecked(!checked)
    },
    start = {
        Checkbox(checked = checked, enabled = enabled, onCheckedChange = onChecked)
    },
    end = end,
)
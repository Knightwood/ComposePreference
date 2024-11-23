package com.kiylx.compose.preference.component.cross

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.ui.SamplePreference

@Composable
fun PreferenceRadio(
    modifier: Modifier = Modifier,
    title: String,
    desc: String? = null,
    selected: Boolean,
    enabled: Boolean = true,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onSelected: (Boolean) -> Unit = {},
) = SamplePreference(
    modifier = modifier,
    title = title, desc = desc, icon = Any(), enabled = enabled,
    onClick = {
        onSelected(!selected)
    },
    start = {
        RadioButton(
            selected = selected,
            enabled = enabled,
            onClick = { onSelected(!selected) })
    },
    end = end,
)

@Composable
fun PreferenceRadioInvert(
    modifier: Modifier = Modifier,
    title: String,
    desc: String? = null,
    selected: Boolean,
    enabled: Boolean = true,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onSelected: (Boolean) -> Unit = {},
) = SamplePreference(
    modifier = modifier,
    title = title, desc = desc, enabled = enabled,
    onClick = {
        onSelected(!selected)
    },
    end = {
        RadioButton(
            selected = selected,
            enabled = enabled,
            onClick = { onSelected(!selected) }
        )
    },
)
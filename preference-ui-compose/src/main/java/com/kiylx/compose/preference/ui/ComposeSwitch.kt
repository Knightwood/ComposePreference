package com.kiylx.compose.preference.ui

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.kiylx.compose.preference.ui.icons.Check

@Composable
fun ComposeSwitch(
    modifier: Modifier = Modifier,
    isChecked: Boolean,
    enabled: Boolean = true,
    icon: ImageVector = Icons.Default.Check,
    onCheckedChange: ((Boolean) -> Unit)? = null,
) {
    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Switch(
        checked = isChecked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        thumbContent = thumbContent
    )
}
package com.kiylx.compose.preference.component.auto

import android.util.Log
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.ui.ParseIcon
import com.kiylx.compose.preference.ui.harmonizeWithPrimary
import com.kiylx.compose.preference.component.cross.PreferenceCollapseItem as FossPreferenceCollapseItem
import com.kiylx.compose.preference.component.cross.PreferenceItem as FossPreferenceItem
import com.kiylx.compose.preference.component.cross.PreferencesCautionCard as FossPreferencesCautionCard

private const val TAG = "PreferenceItem"

@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    dependenceKey: String?,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    PreferenceNodeBase(dependenceKey = dependenceKey, enabled = enabled) { scope, state ->
        LaunchedEffect(state) {
            Log.d(TAG, "依赖状态:$state ")
        }
        FossPreferenceItem(
            modifier = modifier,
            title = title, icon = icon, desc = desc,
            enabled = state, onClick = onClick,
            end = end,
        )
    }
}

@Composable
fun PreferenceCollapseItem(
    modifier: Modifier = Modifier,
    title: String,
    desc: String? = null,
    enabled: Boolean = true,
    dependenceKey: String?,
    expand: Boolean = false,
    end: @Composable (BoxScope.() -> Unit) = {
        ParseIcon(
            model = if (!expand) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            contentDescription = "icon"
        )
    },
    stateChanged: (expand: Boolean) -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    PreferenceNodeBase(dependenceKey = dependenceKey, enabled = enabled) { scope, state ->
        FossPreferenceCollapseItem(
            modifier = modifier,
            title = title,
            desc = desc,
            enabled = state,
            expand = expand,
            end = end,
            stateChanged = stateChanged,
            content = content
        )
    }
}

@Composable
fun PreferencesCautionCard(
    modifier: Modifier = Modifier,
    boxMarginValues: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp
    ),
    color: Color = MaterialTheme.colorScheme.errorContainer.harmonizeWithPrimary(),
    shape: Shape = RoundedCornerShape(28.dp),
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    dependenceKey: String?,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    PreferenceNodeBase(dependenceKey = dependenceKey, enabled = enabled) { scope, state ->
        FossPreferencesCautionCard(
            modifier = modifier,
            boxMarginValues = boxMarginValues,
            color = color,
            shape = shape,
            title = title,
            icon = icon,
            desc = desc,
            enabled = state,
            end = end,
            onClick = onClick
        )
    }
}
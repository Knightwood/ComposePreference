package com.kiylx.compose.preference.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.kiylx.compose.preference.theme.LocalPreferenceStyle
import com.kiylx.compose.preference.theme.PreferenceTheme
import com.kiylx.compose_lib.pref_component.LocalPrefs
import com.kiylx.compose_lib.pref_component.applyOpacity
import kotlinx.coroutines.CoroutineScope

private const val TAG = "PreferenceLayout"

@Composable
fun PreferenceRow(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit = {},
    start: @Composable (BoxScope.() -> Unit)? = null,
    title: @Composable ColumnScope.() -> Unit,
    description: @Composable (ColumnScope.() -> Unit)? = null,
    end: @Composable (BoxScope.() -> Unit)? = null,
) {
    val boxStyle = PreferenceTheme.boxStyle

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(PreferenceTheme.dimens.boxPaddingValues)
            .heightIn(PreferenceTheme.dimens.heightMin),
        shape = boxStyle.shape,
        color = boxStyle.color,
        enabled = enabled,
        onClick = onClick,
        contentColor = boxStyle.contentColor,
        tonalElevation = boxStyle.tonalElevation,
        shadowElevation = boxStyle.shadowElevation,
        border = boxStyle.border
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            start?.let {
                Box(
                    modifier = Modifier
                        .padding(PreferenceTheme.dimens.startPaddingValues)
                        .sizeIn(PreferenceTheme.dimens.iconSize),
                    content = it
                )
            }
            Column(
                modifier = Modifier
                    .padding(PreferenceTheme.dimens.contentPaddingValues)
                    .weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {
                title()
                description?.invoke(this)
            }
            end?.let {
                Box(
                    modifier = Modifier
                        .padding(PreferenceTheme.dimens.endPaddingValues),
                    content = it
                )
            }
        }
    }
}

@Composable
fun <T : Any> PreferenceNodeBase(
    keyName: String,
    enabled: Boolean,
    dependenceKey: String?,
    defaultValue: T,
    content: @Composable (
        scope: CoroutineScope,
        dependenceEnableState: Boolean,
        valueProvider: () -> T
    ) -> Unit
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState: State<Boolean> = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    val currentValue = pref.flow().collectAsState(defaultValue)
    content(
        scope,
        dependenceState.value,
    ) { currentValue.value }
}
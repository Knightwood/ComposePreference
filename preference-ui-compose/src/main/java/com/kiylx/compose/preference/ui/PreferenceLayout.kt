package com.kiylx.compose.preference.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kiylx.compose.preference.theme.PreferenceTheme
import com.kiylx.compose.preference.theme.fixEnabledColor
import com.kiylx.compose_lib.pref_component.LocalPrefs
import kotlinx.coroutines.CoroutineScope

private const val TAG = "PreferenceLayout"

@Composable
fun SamplePreference(
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    end: @Composable (BoxScope.() -> Unit)? = null,

    start: @Composable BoxScope.() -> Unit = {
        val style = PreferenceTheme.iconStyle
        ParseIcon(
            tint = style.fixEnabledTint(enabled),
            model = icon,
            shape = style.shape,
            paddingValues = style.paddingValues,
            backgroundColor = style.fixEnabledBackgroundColor(enabled),
            contentDescription = "icon"
        )
    },

    titleContent: @Composable ColumnScope.(title: String) -> Unit = {
        Text(
            it,
            style = PreferenceTheme.textStyle.titleStyle.fixEnabledColor(enabled),
        )
    },

    descContent: @Composable ColumnScope.(desc: String) -> Unit = {
        Text(
            it,
            style = PreferenceTheme.textStyle.descriptionTextStyle.fixEnabledColor(enabled),
            maxLines = 1
        )
    },

    onClick: (() -> Unit)?,
) {
    PreferenceRow(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick,
        start = if (icon != null) {
            start
        } else null,
        title = if (title != null) {
            { titleContent(title) }
        } else null,
        description = if (desc != null) {
            { descContent(desc) }
        } else null,
        end = end
    )
}

@Composable
fun PreferenceRow(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: (() -> Unit)?,
    start: @Composable (BoxScope.() -> Unit)? = null,
    title: @Composable (ColumnScope.() -> Unit)? = null,
    description: @Composable (ColumnScope.() -> Unit)? = null,
    end: @Composable (BoxScope.() -> Unit)? = null,
) {
    val boxStyle = PreferenceTheme.boxStyle

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                PreferenceTheme.dimens.boxMarginValues
            ),
        shape = boxStyle.shape,
        color = boxStyle.color,
        contentColor = boxStyle.contentColor,
        tonalElevation = boxStyle.tonalElevation,
        shadowElevation = boxStyle.shadowElevation,
        border = boxStyle.border
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .let {
                    if (onClick == null) {
                        it
                    } else {
                        it.clickable(
                            interactionSource = null,
                            enabled = enabled,
                            onClick = onClick,
                            indication = rippleOrFallbackImplementation(),
                        )
                    }
                }
                .padding(PreferenceTheme.dimens.boxPaddingValues)
                .height(IntrinsicSize.Min)
                .heightIn(PreferenceTheme.dimens.heightMin)
            ,
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
                title?.invoke(this)
                description?.invoke(this)
            }
            end?.let {
                Box(
                    modifier = Modifier.fillMaxHeight()
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
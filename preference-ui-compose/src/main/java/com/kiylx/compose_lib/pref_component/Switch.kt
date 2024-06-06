/*
 * Copyright 2024 [KnightWood]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.kiylx.compose_lib.pref_component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kiylx.compose_lib.pref_component.icons.Check

private const val TAG = "Switch"

/**
 * @param keyName 标识存储偏好值的key的名称，也是标识此组件启用状态的节点名称
 * @param defaultValue 默认值、当前值
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param checkedIcon switch置为true时的图标
 * @param changed "存储的偏好值"初始化或更新后，会通过此参数通知
 */
@Composable
fun PreferenceSwitch(
    modifier: Modifier = Modifier,
    keyName: String,
    defaultValue: Boolean = false,
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    dependenceKey: String? = null,
    checkedIcon: ImageVector = Icons.Default.Check,
    changed: (newValue: Boolean) -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var isChecked by remember {
        mutableStateOf(pref.readValue())
    }
    LaunchedEffect(key1 = isChecked, block = {
        pref.write(isChecked)
        changed(isChecked)
    })

    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        { ->
            Icon(
                imageVector = checkedIcon,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Surface(
        modifier = modifier.toggleable(
            value = isChecked,
            enabled = dependenceState.value,
            onValueChange = {
                isChecked = !isChecked
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(
                icon = icon,
                iconSize = dimens.iconSize,
                paddingValues = dimens.startItem,
                enabled = dependenceState.value
            )
            MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
                PreferenceItemTitleText(
                    text = title,
                    style = textStyle.title,
                    enabled = dependenceState.value, maxLines = 2
                )
                description?.let {
                    PreferenceItemDescriptionText(
                        text = it,
                        style = textStyle.body,
                        enabled = dependenceState.value
                    )
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = null,
                modifier = Modifier.padding(dimens.endItem),
                enabled = dependenceState.value,
                thumbContent = thumbContent
            )
        }
    }
}

/**
 * @param keyName 标识存储偏好值的key的名称，也是标识此组件启用状态的节点名称
 * @param defaultValue 默认值、当前值
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param checkedIcon switch置为true时的图标
 * @param changed "存储的偏好值"初始化或更新后，会通过此参数通知
 * @param onClick switch左侧区域点击事件
 */
@Composable
fun PreferenceSwitchWithDivider(
    modifier: Modifier = Modifier,
    keyName: String,
    defaultValue: Boolean = false,
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    dependenceKey: String? = null,
    isSwitchEnabled: Boolean = enabled,
    checkedIcon: ImageVector = Icons.Default.Check,
    changed: (it: Boolean) -> Unit = {},
    onClick: (() -> Unit) = {},
) {
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var isChecked by remember {
        mutableStateOf(pref.readValue())
    }
    LaunchedEffect(key1 = isChecked, block = {
        pref.write(isChecked)
        changed(isChecked)
    })

    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = checkedIcon,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Surface(
        modifier = modifier.clickable(
            enabled = dependenceState.value,
            onClick = onClick,
            onClickLabel = "open settings"
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(
                icon = icon,
                iconSize = dimens.iconSize,
                paddingValues = dimens.startItem,
                enabled = dependenceState.value
            )
            MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
                PreferenceItemTitleText(
                    text = title,
                    style = textStyle.title,
                    enabled = dependenceState.value
                )
                if (!description.isNullOrEmpty()) PreferenceItemDescriptionText(
                    text = description,
                    enabled = dependenceState.value
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(PreferenceDimenTokens.large_xx.dp)
                    .padding(horizontal = PreferenceDimenTokens.small.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                thickness = 1f.dp
            )
            Switch(
                checked = isChecked,
                onCheckedChange = {
                    isChecked = !isChecked
                },
                modifier = Modifier
                    .padding(dimens.endItem)
                    .semantics {
                        contentDescription = title
                    },
                enabled = (isSwitchEnabled && dependenceState.value),
                thumbContent = thumbContent
            )
        }
    }
}

/**
 * @param keyName 标识存储偏好值的key的名称，也是标识此组件启用状态的节点名称
 * @param defaultValue 默认值、当前值
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param changed "存储的偏好值"初始化或更新后，会通过此参数通知
 */
@Composable
fun PreferenceSwitchWithContainer(
    modifier: Modifier = Modifier,
    keyName: String,
    defaultValue: Boolean = false,
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    dependenceKey: String? = null,
    changed: (it: Boolean) -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var isChecked by remember {
        mutableStateOf(pref.readValue())
    }
    LaunchedEffect(key1 = isChecked, block = {
        pref.write(isChecked)
        changed(isChecked)
    })

    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.boxItem)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                (if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline).let {
                    it.applyOpacity(dependenceState.value)
                }
            )
            .toggleable(
                value = isChecked,
                enabled = dependenceState.value,
            ) {
                isChecked = !isChecked
            }
            .padding(
                horizontal = PreferenceDimenTokens.medium.dp,
                vertical = PreferenceDimenTokens.large.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WrappedIcon(
            icon = icon,
            iconSize = dimens.iconSize,
            paddingValues = dimens.startItem,
            enabled = dependenceState.value,
            tint = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface
        )
        MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
            PreferenceItemTitleText(
                text = title,
                maxLines = 2,
                style = textStyle.title,
                enabled = dependenceState.value,
                color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface
            )
            if (!description.isNullOrEmpty()) PreferenceItemDescriptionText(
                text = description,
                style = textStyle.body,
                enabled = dependenceState.value,
                color = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface
            )
        }
        Switch(
            checked = isChecked,
            onCheckedChange = null,
            enabled = dependenceState.value,
            modifier = Modifier.padding(dimens.endItem),
            thumbContent = thumbContent,
            colors = SwitchDefaults.colors(
                checkedIconColor = MaterialTheme.colorScheme.onPrimary,
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

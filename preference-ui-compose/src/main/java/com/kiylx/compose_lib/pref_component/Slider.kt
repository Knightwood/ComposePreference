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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.CoroutineScope


/**
 * @param keyName 标识存储偏好值的key的名称，也是启用状态的节点名称
 * @param min 最小值
 * @param max 最大值
 * @param steps 将min到max平均分为几份
 * @param defaultValue 默认值、当前值
 * @param enabled 是否启用
 * @param dependenceKey
 *    若为null,则启用状态依照enable值，若不为null,则启用状态依赖dependenceKey指向的节点
 * @param changed "存储的偏好值"初始化或更新后，会通过此参数通知
 */
@Composable
fun PreferenceSlider(
    modifier: Modifier = Modifier,
    keyName: String,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    min: Float = 0f,
    max: Float = 100f,
    steps: Int = 0,
    defaultValue: Float = 0f,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    changed: (newValue: Float) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    val currentValue = pref.flow().collectAsState(defaultValue)

    var progress by remember {
        mutableFloatStateOf(pref.readValue())
    }

    remember(currentValue.value) {
        if (currentValue.value != progress)
            progress = currentValue.value
        progress
    }

    var finished by remember {
        mutableStateOf(false)
    }

    //写入prefs
    LaunchedEffect(key1 = finished, block = {
        if (finished) {
            pref.write(progress)
            changed(progress)
        }
    })

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.boxItem),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Slider(
            value = progress,
            enabled = dependenceState.value,
            modifier = Modifier
                .weight(4f)
                .padding(dimens.mediumBox),
            onValueChange = {
                finished = false
                progress = it
            },
            steps = steps,
            valueRange = min..max,
            onValueChangeFinished = {
                finished = true
            }
        )
        Text(
            modifier = Modifier
                .padding(end = dimens.endItem.end),
            text = String.format("%.2f", progress),
            maxLines = 1,
            style = textStyle.title,
            color = MaterialTheme.colorScheme.onSurface.applyOpacity(dependenceState.value),
            overflow = TextOverflow.Ellipsis
        )
    }
}


private const val TAG = "Slider"
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

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kiylx.compose_lib.pref_component.Typography.preferenceMediumTitle
import kotlinx.coroutines.launch

/**
 * @param keyName 标识存储偏好值的key的名称，也是启用状态的节点名称
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值，若不为null,则启用状态依赖dependenceKey指向的节点
 * @param labels 每个条目的显示名称，而且会按照显示名称在数组中的顺序生成"存储值label"
 * @param left checkbox位于左侧或是右侧
 * @param paddingValues 调整边框的padding
 * @param changed "存储值label"初始化或更新后，会通过此参数通知
 */
@JvmName("PreferenceRadioGroup2")
@Composable
fun PreferenceRadioGroup(
    keyName: String,
    labels: List<String>,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    left: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = Dimens.all.horizontal_start.dp,
        vertical = Dimens.small.dp
    ),
    changed: (newIndex: Int) -> Unit = {},
) {
    val labels2 = labels.mapIndexed { index, s ->
        s to index
    }
    PreferenceRadioGroup(keyName, labels2, enabled, dependenceKey, left, paddingValues, changed)
}

/**
 * @param keyName 标识存储偏好值的key的名称，也是标识此组件启用状态的节点名称
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param labelPairs :Pair<text,int> 包含着每个可选条目的显示文本和"存储值label"
 * @param left checkbox位于左侧或是右侧
 * @param paddingValues 调整边框的padding
 * @param changed "存储的偏好值label"初始化或更新后，会通过此参数通知
 */
@Composable
fun PreferenceRadioGroup(
    keyName: String,
    labelPairs: List<Pair<String, Int>>,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    left: Boolean = false,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = Dimens.all.horizontal_start.dp,
        vertical = Dimens.small.dp
    ),
    changed: (newIndex: Int) -> Unit = {},
) {
    if (labelPairs.isEmpty()) {
        throw IllegalArgumentException("labels cannot empty")
    }
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getReadWriteTool(keyName = keyName, defaultValue = 0)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var selectedPos by remember {
        mutableIntStateOf(labelPairs[0].second)
    }
    LaunchedEffect(key1 = Unit, block = {
        pref.read().collect {
            selectedPos = it
            changed(it)
        }
    })

    fun write(pos: Int) {
        scope.launch {
            pref.write(pos)
        }
    }

    Column {
        if (left) {
            repeat(labelPairs.size) { pos: Int ->
                PreferenceSingleChoiceItem(
                    text = labelPairs[pos].first,
                    enabled = dependenceState.value,
                    selected = (labelPairs[pos].second == selectedPos),
                    paddingValues = paddingValues,
                    onClick = {
                        write(labelPairs[pos].second)
                    }
                )
            }
        } else {
            repeat(labelPairs.size) { pos: Int ->
                PreferenceSingleChoiceItemRight(
                    text = labelPairs[pos].first,
                    enabled = dependenceState.value,
                    selected = (labelPairs[pos].second == selectedPos),
                    paddingValues = paddingValues,
                    onClick = {
                        write(labelPairs[pos].second)
                    }
                )
            }
        }
    }
}

@Composable
fun PreferenceSingleChoiceItem(
    paddingValues: PaddingValues = PaddingValues(
        horizontal = Dimens.all.horizontal_start.dp,
        vertical = Dimens.small.dp
    ),
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.selectable(
            enabled = enabled,
            selected = selected, onClick = onClick
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RadioButton(
                selected = selected,
                enabled = enabled,
                onClick = { onClick.invoke() },
                modifier = Modifier
                    .clearAndSetSemantics { },
            )
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = Dimens.text.end.dp),
                text = text,
                maxLines = 1,
                style = preferenceMediumTitle,
                color = MaterialTheme.colorScheme.onSurface.applyOpacity(enabled),
                overflow = TextOverflow.Ellipsis
            )

        }
    }
}


@Composable
fun PreferenceSingleChoiceItemRight(
    paddingValues: PaddingValues = PaddingValues(
        horizontal = Dimens.medium.dp,
        vertical = Dimens.medium.dp
    ),
    text: String,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.selectable(
            enabled = enabled,
            selected = selected, onClick = onClick
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Dimens.text.start.dp, end = Dimens.text.end.dp),
                text = text,
                maxLines = 1,
                style = preferenceMediumTitle,
                color = MaterialTheme.colorScheme.onSurface.applyOpacity(enabled),
                overflow = TextOverflow.Ellipsis
            )
            RadioButton(
                selected = selected,
                enabled = enabled,
                onClick = { onClick.invoke() },
                modifier = Modifier
                    .clearAndSetSemantics { },
            )
        }
    }
}
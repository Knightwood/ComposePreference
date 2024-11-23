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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch

private const val TAG = "PrefCheckBoxGroup"

/**
 * @param keyName 标识存储偏好值的key的名称，也是启用状态的节点名称
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值，若不为null,则启用状态依赖dependenceKey指向的节点
 * @param labels 每个条目的显示名称，而且会按照显示名称在数组中的顺序生成"存储值label"
 * @param left checkbox位于左侧或是右侧
 * @param paddingValues 调整边框的padding
 * @param changed "存储值label"初始化或更新后，会通过此参数通知
 */
@JvmName("PreferenceCheckBoxGroup2")
@Composable
fun PreferenceCheckBoxGroup(
    modifier: Modifier = Modifier,
    keyName: String,
    labels: List<String>,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    left: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle =PreferenceTheme.normalTextStyle,
    changed: (newIndex: List<Int>) -> Unit = {},
) {
    val labels2 = labels.mapIndexed { index, s ->
        s to index
    }
    PreferenceCheckBoxGroup(modifier,keyName, labels2, enabled, dependenceKey, left, dimens, textStyle,changed)
}

/**
 * @param keyName 标识存储偏好值的key的名称，也是启用状态的节点名称
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值，若不为null,则启用状态依赖dependenceKey指向的节点
 * @param items :Pair<text,int> 包含着每个可选条目的显示文本和"存储值label"
 * @param left checkbox位于左侧或是右侧
 * @param paddingValues 调整边框的padding
 * @param changed "存储的偏好值label"初始化或更新后，会通过此参数通知
 */
@Composable
fun PreferenceCheckBoxGroup(
    modifier: Modifier= Modifier,
    keyName: String,
    items: List<Pair<String, Int>>,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    left: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle =PreferenceTheme.normalTextStyle,
    changed: (selected: List<Int>) -> Unit = {},
) {
    if (items.isEmpty()) {
        throw IllegalArgumentException("labels cannot empty")
    }
    fun getStr(list: List<Int>): String {
        return list.joinToString(",")
    }

    fun genList(s: String): List<Int> {
        return try {
            if (s.isEmpty()) {
                emptyList()
            } else {
                val tmp = s.split(",").map { it.toInt() }
                tmp
            }
        } catch (e: Exception) {
            Log.e(TAG, "genList: $e")
            emptyList()
        }

    }

    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = "")
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    val selectedList = remember {
        mutableStateListOf<Int>()
    }
    //读取prefs
    LaunchedEffect(key1 = Unit, block = {
        pref.flow().collect { s ->
            selectedList.clear()
            selectedList.addAll(genList(s))//根据字符串重新生成list
            changed(selectedList)
        }
    })

    //写入prefs
    fun write() {
        scope.launch {
            pref.write(getStr(selectedList))
        }
    }

    Column(modifier =modifier) {
        repeat(items.size) { pos: Int ->
            val checked by remember {
                derivedStateOf {
                    selectedList.contains(items[pos].second)
                }
            }
            if (left) {
                PreferenceCheckBoxItem(
                    text = items[pos].first,
                    selected = checked,
                    enabled = dependenceState.value,
                    dimens =dimens,
                    textStyle =textStyle,
                    onCheckedChanged = { bol ->
                        if (bol) {
                            selectedList.add(items[pos].second)
                        } else {
                            selectedList.remove(items[pos].second)
                        }
                        write()
                    }
                )
            } else {
                PreferenceCheckBoxItemRight(
                    text = items[pos].first,
                    selected = checked,
                    dimens =dimens,
                    textStyle =textStyle,
                    enabled = dependenceState.value,
                    onCheckedChanged = { bol ->
                        if (bol) {
                            selectedList.add(items[pos].second)
                        } else {
                            selectedList.remove(items[pos].second)
                        }
                        write()
                    }
                )
            }
        }
    }
}

@Composable
fun PreferenceCheckBoxItem(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    dimens: PreferenceDimens =PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    onCheckedChanged: (checked: Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.toggleable(
            value = selected,
            enabled = enabled,
        ) {
            onCheckedChanged(it)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Checkbox(
                checked = selected,
                enabled = enabled,
                onCheckedChange = {
                    onCheckedChanged(it)
                },
                modifier = Modifier
                    .clearAndSetSemantics { },
            )

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = dimens.mediumBox.end),
                text = text,
                maxLines = 1,
                style = textStyle.title,
                color = MaterialTheme.colorScheme.onSurface.applyOpacity(enabled),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun PreferenceCheckBoxItemRight(
    text: String,
    selected: Boolean,
    enabled: Boolean,
    dimens: PreferenceDimens =PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    onCheckedChanged: (checked: Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.toggleable(
            value = selected,
            enabled = enabled,
        ) {
            onCheckedChanged(it)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(dimens.mediumBox),
                text = text,
                maxLines = 1,
                style = textStyle.title,
                color = MaterialTheme.colorScheme.onSurface.applyOpacity(enabled),
                overflow = TextOverflow.Ellipsis
            )
            Checkbox(
                checked = selected,
                enabled = enabled,
                onCheckedChange = {
                    onCheckedChanged(it)
                },
                modifier = Modifier
                    .padding()
                    .clearAndSetSemantics { },
            )
        }
    }
}
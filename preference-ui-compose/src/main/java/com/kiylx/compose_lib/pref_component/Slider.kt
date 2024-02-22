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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


/**
 * @param keyName 标识存储偏好值的key的名称，也是启用状态的节点名称
 * @param min 最小值
 * @param max 最大值
 * @param steps 将min到max平均分为几份
 * @param defaultValue 默认值、当前值
 * @param enabled 是否启用
 * @param dependenceKey
 *     若为null,则启用状态依照enable值，若不为null,则启用状态依赖dependenceKey指向的节点
 * @param changed "存储的偏好值"初始化或更新后，会通过此参数通知
 */
@Composable
fun PreferenceSlider(
    keyName: String,
    min: Float,
    max: Float,
    steps: Int,
    defaultValue: Float,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    changed: (newValue: Float) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getReadWriteTool(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var progress by remember {
        mutableFloatStateOf(defaultValue)
    }
    var first by remember {
        mutableStateOf(true)
    }
    var updateFlag by remember {
        mutableLongStateOf(0L)
    }

    //读取prefs
    LaunchedEffect(key1 = Unit, block = {
        pref.read().collect { s ->
            if (first) {
                progress = s
                first = false
            }
            changed(progress)
        }
    })

    //写入prefs
    LaunchedEffect(key1 = updateFlag, block = {
        pref.write(progress)
    })
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.all.horizontal_start.dp, Dimens.all.vertical_bottom.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Slider(
            value = progress,
            enabled = dependenceState.value,
            modifier = Modifier
                .weight(4f)
                .padding(
                    start = Dimens.text.start.dp,
                    end = Dimens.text.end.dp,
                ),
            onValueChange = {
                progress = it
            },
            steps = steps,
            valueRange = min..max,
            onValueChangeFinished = {
                updateFlag=System.currentTimeMillis()
            }
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(end = Dimens.text.end.dp),
            text = String.format("%.2f", progress),
            maxLines = 1,
            style = Typography.preferenceMediumTitle,
            color = MaterialTheme.colorScheme.onSurface.applyOpacity(dependenceState.value),
            overflow = TextOverflow.Ellipsis
        )
    }
}
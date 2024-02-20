package com.kiylx.compose_lib.pref_component

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import com.kiylx.compose_lib.pref_component.Typography.preferenceMediumTitle
import kotlinx.coroutines.launch

private const val TAG = "PrefCheckBoxGroup"


/**
 * @param labels :Pair<id,text> pair包含着id值和文本
 */
@Composable
fun PreferenceCheckBoxGroup(
    keyName: String,
    labels: List<String>,
    enabled: Boolean = true,
    dependenceKey:String?=null,
    left:Boolean =true,
    paddingValues: PaddingValues = PaddingValues(
        horizontal = Dimens.all.horizontal.dp,
        vertical = Dimens.small.dp
    ),
    changed: (newIndex: List<Int>) -> Unit = {},
) {
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
    val pref =prefStoreHolder.getReadWriteTool(keyName = keyName, defaultValue = "")
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(keyName,enabled,dependenceKey).enableStateFlow.collectAsState()

    val selectedList = remember {
        mutableStateListOf<Int>();
    }
    //读取prefs
    LaunchedEffect(key1 = Unit, block = {
        pref.read().collect { s ->
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

    Column {
        repeat(labels.size) { pos: Int ->
            val checked by remember {
                derivedStateOf {
                    selectedList.contains(pos)
                }
            }
            if (left) {
                PreferenceCheckBoxItem(
                    text = labels[pos],
                    selected = checked,
                    enabled = dependenceState.value,
                    paddingValues=paddingValues,
                    onCheckedChanged = { bol ->
                        if (bol) {
                            selectedList.add(pos)
                        } else {
                            selectedList.remove(pos)
                        }
                        write()
                    }
                )
            }else{
                PreferenceCheckBoxItemRight(
                    text = labels[pos],
                    selected = checked,
                    enabled = dependenceState.value,
                    paddingValues=paddingValues,

                    onCheckedChanged = { bol ->
                        if (bol) {
                            selectedList.add(pos)
                        } else {
                            selectedList.remove(pos)
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
    paddingValues: PaddingValues = PaddingValues(
        horizontal = Dimens.all.horizontal.dp,
        vertical = Dimens.small.dp
    ),
    onCheckedChanged: (newValue: Boolean) -> Unit
) {
    var checked = selected
    Surface(
        modifier = Modifier.toggleable(
            value = checked,
            enabled = enabled,
        ) {
            checked = it
            onCheckedChanged(checked)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Checkbox(
                checked = checked,
                enabled=enabled,
                onCheckedChange = {
                    checked = it
                    onCheckedChanged(checked)
                },
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
                color = MaterialTheme.colorScheme.onSurface.applyOpacity(enabled) ,
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
    paddingValues: PaddingValues = PaddingValues(
        horizontal = Dimens.all.horizontal.dp,
        vertical = Dimens.small.dp
    ),
    onCheckedChanged: (newValue: Boolean) -> Unit
) {
    var checked = selected
    Surface(
        modifier = Modifier.toggleable(
            value = checked,
            enabled = enabled,
        ) {
            checked = it
            onCheckedChanged(checked)
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues = paddingValues),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = Dimens.text.start.dp, end = Dimens.text.end.dp),
                text = text,
                maxLines = 1,
                style = preferenceMediumTitle,
                color = MaterialTheme.colorScheme.onSurface.applyOpacity(enabled) ,
                overflow = TextOverflow.Ellipsis
            )
            Checkbox(
                    checked = checked,
            enabled=enabled,
            onCheckedChange = {
                checked = it
                onCheckedChanged(checked)
            },
            modifier = Modifier
                .padding()
                .clearAndSetSemantics { },
            )
        }
    }
}
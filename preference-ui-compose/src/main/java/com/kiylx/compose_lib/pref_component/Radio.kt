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
 * @param labels 按照label名称的顺序生成存储值
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
 * @param labelPairs :Pair<text,id> pair包含着文本和存储值
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
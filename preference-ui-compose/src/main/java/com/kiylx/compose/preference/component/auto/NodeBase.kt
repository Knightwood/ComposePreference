package com.kiylx.compose.preference.component.auto

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun <T : Any> PreferenceNodeBase(
    keyName: String,
    enabled: Boolean,
    dependenceKey: String?,
    defaultValue: T,
    content: @Composable (
        scope: CoroutineScope,
        depState: Boolean,
        valueProvider: () -> T,
        writer: (T) -> Unit,
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
        scope, dependenceState.value,
        { currentValue.value },
        { scope.launch { pref.write(it) } }
    )
}

@Composable
fun PreferenceNodeBase(
    enabled: Boolean,
    dependenceKey: String?,
    content: @Composable (
        scope: CoroutineScope,
        depState: Boolean,
    ) -> Unit
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(
            dependenceKey,
            enabled
        ).enableStateFlow.collectAsState()

    content(
        scope,
        dependenceState.value,
    )


}
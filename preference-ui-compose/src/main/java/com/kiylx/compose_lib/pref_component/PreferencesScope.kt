package com.kiylx.compose_lib.pref_component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kiylx.libx.pref_component.core.DependenceNode
import com.kiylx.libx.pref_component.core.PreferenceHolder

val LocalPrefs = compositionLocalOf<PreferenceHolder> { error("没有提供值！") }


@Composable
fun PreferencesScope(
    holder: PreferenceHolder,
    content: @Composable () -> Unit
) {
    val preferenceStore by remember {
        mutableStateOf(holder)
    }
    CompositionLocalProvider(LocalPrefs provides preferenceStore) {
        Surface {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {
                content()
            }
        }
    }
}
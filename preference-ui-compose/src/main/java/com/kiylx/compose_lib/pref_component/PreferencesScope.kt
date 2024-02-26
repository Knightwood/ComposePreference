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

/**
 * 在此方法 content中构建偏好值页面
 *
 * @param holder preference读写持有者,查看[PreferenceHolder]
 * @param content compose函数
 */
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
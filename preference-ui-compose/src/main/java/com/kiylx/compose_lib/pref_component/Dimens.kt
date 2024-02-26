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

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.kiylx.common.lib_materialcolorutilities.blend.Blend

fun Color.applyOpacity(enabled: Boolean): Color {
    return if (enabled) this else this.copy(alpha = 0.62f)
}

@Composable
fun Color.harmonizeWith(other: Color) =
    Color(Blend.harmonize(this.toArgb(), other.toArgb()))

@Composable
fun Color.harmonizeWithPrimary(): Color =
    this.harmonizeWith(other = MaterialTheme.colorScheme.primary)

object Dimens {

    const val small_ss = 2
    const val small_s = 4
    const val small = 8
    const val medium = 12
    const val large = 16
    const val large_x = 24
    const val large_xx = 36

    object all {
        var horizontal_start = medium
        var horizontal_end = medium
        var vertical_top = medium
        var vertical_bottom = small_ss
    }
    object title {
        var horizontal_start = medium
        var horizontal_end = medium
        var vertical_top = large
        var vertical_bottom = small_ss
    }

    //头部的icon相关
    object icon {
        var size = large_x

        //padding
        var start = medium
        var end = medium
        var top = large
        var bottom = large

    }

    //中间的文字相关
    object text {
        //padding
        var start = medium
        var end = medium
        var top = 0
        var bottom = 0
    }

    //最后面的按钮相关
    object end {
        var start = large_x
        var end = small
    }
}

object Typography {

    val preferenceLargeTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge.copy(
            fontSize = 24.sp
        )

    val preferenceSmallTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleMedium

    val preferenceMediumTitle: TextStyle
        @Composable
        get() = MaterialTheme.typography.titleLarge.copy(
            fontSize = 20.sp
        )

    val preferenceDescription: TextStyle
        @Composable
        get() = MaterialTheme.typography.bodyMedium
}
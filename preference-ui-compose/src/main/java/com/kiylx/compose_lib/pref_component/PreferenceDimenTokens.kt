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
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
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

object PreferenceDimenTokens {

    const val small_ss = 2
    const val small_s = 4
    const val small = 8
    const val medium = 12
    const val large = 16
    const val large_x = 24
    const val large_xx = 36
}

object PreferenceTypographyTokens {
    //<editor-fold desc="title">

    //22sp
    val titleLarge: TextStyle
        @Composable
        @ReadOnlyComposable
        get() = MaterialTheme.typography.titleLarge

    //20sp
    val titleMedium: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)

    //16sp
    val titleSmall: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.titleMedium
    //</editor-fold>

    //<editor-fold desc="body">

    //16sp
    val bodyLarge: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.bodyLarge

    //14sp
    val bodyMedium: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.bodyMedium

    //12sp
    val bodySmall: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.bodySmall

    //</editor-fold>

    //<editor-fold desc="label">

    //14sp
    val labelLarge: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.labelLarge

    //12sp
    val labelMedium: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.labelMedium

    //11sp
    val labelSmall: TextStyle
        @ReadOnlyComposable
        @Composable
        get() = MaterialTheme.typography.labelSmall

    //</editor-fold>

}
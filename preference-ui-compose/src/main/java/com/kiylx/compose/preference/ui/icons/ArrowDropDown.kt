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

package com.kiylx.compose.preference.ui.icons
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.ArrowDropDown: ImageVector
    get() {
        if (_arrowDropDown != null) {
            return _arrowDropDown!!
        }
        _arrowDropDown = materialIcon(name = "Filled.ArrowDropDown") {
            materialPath {
                moveTo(7.0f, 10.0f)
                lineToRelative(5.0f, 5.0f)
                lineToRelative(5.0f, -5.0f)
                close()
            }
        }
        return _arrowDropDown!!
    }

private var _arrowDropDown: ImageVector? = null

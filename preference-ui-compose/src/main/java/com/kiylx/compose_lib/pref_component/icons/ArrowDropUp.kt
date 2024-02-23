package com.kiylx.compose_lib.pref_component.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.ArrowDropUp: ImageVector
    get() {
        if (_arrowDropUp != null) {
            return _arrowDropUp!!
        }
        _arrowDropUp = materialIcon(name = "Filled.ArrowDropUp") {
            materialPath {
                moveTo(7.0f, 14.0f)
                lineToRelative(5.0f, -5.0f)
                lineToRelative(5.0f, 5.0f)
                close()
            }
        }
        return _arrowDropUp!!
    }

private var _arrowDropUp: ImageVector? = null

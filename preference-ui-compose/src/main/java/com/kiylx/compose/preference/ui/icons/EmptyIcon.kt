package com.kiylx.compose.preference.ui.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Filled.EmptyIcon: ImageVector
    get() {
        if (_empty_icon != null) {
            return _empty_icon!!
        }
        _empty_icon = materialIcon(name = "Filled.ErrorOutline") {
            materialPath {

            }
        }
        return _empty_icon!!
    }

private var _empty_icon: ImageVector? = null

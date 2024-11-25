package com.kiylx.compose.preference.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


/**
 * icon组件
 *
 * @param modifier icon外层surface的modifier
 * @param iconModifier icon的modifier
 * @param model ImageVector 或者 Painter 或者 resourceId
 * @param contentDescription
 * @param shape
 * @param paddingValues
 * @param backgroundColor
 * @param enabled
 * @param tint
 */
@Composable
fun ParseIcon(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    shape: Shape = CircleShape,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled)
) {
    Surface(
        modifier = modifier.wrapContentSize(),
        color = backgroundColor,
        shape = shape,
    ) {
        when (model) {
            is ImageVector -> {
                Icon(
                    imageVector = model,
                    contentDescription = contentDescription,
                    modifier = iconModifier.padding(paddingValues),
                    tint = tint
                )
            }

            is Painter -> {
                Icon(
                    painter = model,
                    contentDescription = contentDescription,
                    modifier = iconModifier.padding(paddingValues),
                    tint = tint
                )
            }

            is Int -> {
                Icon(
                    painter = painterResource(id = model),
                    modifier = iconModifier.padding(paddingValues),
                    contentDescription = contentDescription,
                    tint = tint
                )
            }
        }
    }

}

@Preview()
@Composable
fun IconPreview() {
    Surface(modifier = Modifier.size(500.dp, 500.dp)) {
        Column(modifier = Modifier.padding(24.dp)) {
            ParseIcon(
                model = Icons.Filled.Settings,
                paddingValues = PaddingValues(8.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentDescription = "test",
            )
        }

    }
}
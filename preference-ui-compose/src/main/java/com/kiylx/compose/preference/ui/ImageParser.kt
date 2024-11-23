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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiylx.compose_lib.pref_component.applyOpacity

class ImageParser {
}

@Composable
fun SmallIcon(
    modifier: Modifier = Modifier,
    model: Any,
    size: Dp = 24.dp,
    contentDescription: String? = null,
    enabled: Boolean = true,
) {
    ParseIcon(
        modifier = modifier
            .padding(8.dp)
            .size(size),
        model = model,
        contentDescription = contentDescription,
        enabled = enabled,
    )
}

@Composable
fun CircleIcon(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    shape: Shape = CircleShape,
    paddingValues: PaddingValues = PaddingValues(8.dp),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onPrimary.applyOpacity(enabled)
) {
    ParseIcon(
        modifier = modifier,
        model = model,
        paddingValues = paddingValues,
        tint = tint,
        shape = shape,
        backgroundColor = backgroundColor,
        contentDescription = contentDescription,
    )
}

@Composable
fun ParseIcon(
    outModifier: Modifier = Modifier,
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String? = null,
    shape: Shape = CircleShape,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    backgroundColor: Color = Color.Transparent,
    enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled)
) {
    Surface(
        modifier = outModifier.wrapContentSize(),
        color = backgroundColor,
        shape = shape,
    ) {
        when (model) {
            is ImageVector -> {
                Icon(
                    imageVector = model,
                    contentDescription = contentDescription,
                    modifier = modifier.padding(paddingValues),
                    tint = tint
                )
            }

            is Painter -> {
                Icon(
                    painter = model,
                    contentDescription = contentDescription,
                    modifier = modifier.padding(paddingValues),
                    tint = tint
                )
            }

            is Int -> {
                Icon(
                    painter = painterResource(id = model),
                    modifier = modifier.padding(paddingValues),
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
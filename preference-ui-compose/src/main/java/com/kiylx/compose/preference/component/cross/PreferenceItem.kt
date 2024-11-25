package com.kiylx.compose.preference.component.cross

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.theme.Preferences
import com.kiylx.compose.preference.ui.ParseIcon
import com.kiylx.compose.preference.ui.SamplePreference
import com.kiylx.compose.preference.ui.harmonizeWithPrimary


@Composable
fun PreferenceSubTitle(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(
        start = 8.dp,
        end = 8.dp,
        top = 24.dp,
        bottom = 0.dp
    ),
    color: Color = Color.Transparent,
    title: String,
    icon: Any? = null,
) {
    Preferences.CopyTheme(
        dimenProvider = { copy(boxPaddingValues = paddingValues) },
        boxStyleProvider = { copy(color = color) },
    ) {
        SamplePreference(
            modifier = modifier,
            title = title,
            titleContent = {
                Text(
                    title,
                    style = Preferences.textStyle.categoryTextStyle,
                )
            },
            icon = icon, desc = null,
            enabled = false,
            onClick = null,
            end = null,
        )
    }
}

@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onClick: () -> Unit = {},
) = SamplePreference(
    modifier = modifier,
    title = title, icon = icon, desc = desc,
    enabled = enabled, onClick = onClick,
    end = end,
)

@Composable
fun PreferenceCollapseItem(
    modifier: Modifier = Modifier,
    title: String,
    desc: String? = null,
    enabled: Boolean = true,
    expand: Boolean = false,
    end: @Composable (BoxScope.() -> Unit) = {
        ParseIcon(
            modifier = Modifier.align(Alignment.Center),
            model = if (!expand) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
            contentDescription = "icon"
        )
    },
    stateChanged: (expand: Boolean) -> Unit = {},
    content: @Composable BoxScope.() -> Unit
) {
    Column(
        modifier = modifier
            .animateContentSize()
    ) {
        SamplePreference(
            modifier = modifier,
            title = title, icon = null, desc = desc,
            enabled = enabled, onClick = { stateChanged(!expand) },
            end = end,
        )
        AnimatedVisibility(visible = expand && enabled) {
            Box(content = content)
        }
    }


}

@Composable
fun PreferencesCautionCard(
    modifier: Modifier = Modifier,
    boxMarginValues: PaddingValues = PaddingValues(
        start = 16.dp,
        end = 16.dp
    ),
    color: Color = MaterialTheme.colorScheme.errorContainer.harmonizeWithPrimary(),
    shape: Shape = RoundedCornerShape(28.dp),
    title: String,
    icon: Any? = null,
    desc: String? = null,
    enabled: Boolean = true,
    end: @Composable (BoxScope.() -> Unit)? = null,
    onClick: () -> Unit = {},
) {
    Preferences.CopyTheme(
        dimenProvider = { copy(boxMarginValues = boxMarginValues) },
        boxStyleProvider = { copy(color = color, shape = shape) }
    ) {
        SamplePreference(
            modifier = modifier,
            title = title, icon = icon, desc = desc,
            enabled = enabled, onClick = onClick,
            end = end,
        )
    }

}

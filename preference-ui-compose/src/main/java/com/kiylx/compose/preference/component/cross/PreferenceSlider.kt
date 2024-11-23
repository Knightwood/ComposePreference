package com.kiylx.compose.preference.component.cross

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.ui.SamplePreference

@Composable
fun PreferenceSlider(
    modifier: Modifier = Modifier,
    icon: Any? = null,
    min: Float = 0F,
    max: Float = 100F,
    value: Float = min,
    steps: Int = 0,
    desc: String? = null,
    enabled: Boolean = true,
    colors: SliderColors = SliderDefaults.colors(),
    end: @Composable (BoxScope.(value: Float) -> Unit) = {
        Text(
            text = it.toString(),
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 12.dp)
                .fillMaxHeight()
                .size(width = 46.dp, height = 24.dp)
        )
    },
    onValueChanged: (value: Float) -> Unit,
    changeFinished: () -> Unit={},
) {
    SamplePreference(
        modifier = modifier,
        title = "",
        titleContent = {
            Slider(
                value = value,
                enabled = enabled,
                onValueChange = onValueChanged,
                colors = colors,
                steps = steps,
                valueRange = min..max,
                onValueChangeFinished = changeFinished,
            )
        },
        icon = icon, desc = desc, enabled = enabled, onClick = null,
        end = {
            end(value)
        },
    )
}
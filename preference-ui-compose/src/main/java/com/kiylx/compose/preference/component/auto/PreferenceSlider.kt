package com.kiylx.compose.preference.component.auto

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.component.cross.PreferenceSlider as FossPreferenceSlider

@Composable
fun PreferenceSlider(
    modifier: Modifier = Modifier,
    keyName: String,
    dependenceKey: String?,
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
    onValueChanged: (value: Float) -> Unit = {},
    changeFinished: () -> Unit = {},
) {
    PreferenceNodeBase<Float>(
        dependenceKey = dependenceKey,
        keyName = keyName, defaultValue = value,
        enabled = enabled
    ) { scope, state, provider, writer ->
        val prefValue = provider()

        var progress by remember {
            mutableStateOf(value)
        }
        remember(prefValue) {
            if (prefValue != progress)
                progress = prefValue
            onValueChanged.invoke(prefValue)
            provider
        }

        FossPreferenceSlider(
            modifier = modifier,
            icon = icon,
            min = min,
            max = max,
            value = progress,
            steps = steps,
            desc = desc,
            enabled = state,
            colors = colors,
            end = end,
            onValueChanged = {
                progress = it
            },
            changeFinished = {
                writer.invoke(progress)
                changeFinished()
            }
        )
    }

}
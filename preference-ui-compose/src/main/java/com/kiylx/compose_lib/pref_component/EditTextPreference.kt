package com.kiylx.compose_lib.pref_component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun EditTextPreference(
    keyName: String,
    defaultValue: String = "",
    title: String,
    icon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    changed: (it: String) -> Unit = {},

    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    prefix: @Composable (() -> Unit)? = null,
    suffix: @Composable (() -> Unit)? = null,
    supportingText: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors()
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getReadWriteTool(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var text by remember { mutableStateOf("") }
    LaunchedEffect(key1 = Unit, block = {
        pref.read().collect {
            text = it
            changed(it)
        }
    })

    fun write(checked: String) {
        scope.launch {
            pref.write(checked)
        }
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.all.horizontal_start.dp, vertical = Dimens.medium.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
                write(it)
            },
            label = { Text(text = title) },
            enabled = dependenceState.value,

            textStyle = textStyle,
            placeholder = placeholder,
            trailingIcon = trailingIcon,
            prefix = prefix,
            suffix = suffix,
            supportingText = supportingText,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            maxLines = maxLines,
            singleLine = singleLine,
            minLines = minLines,
            interactionSource = interactionSource,
            colors = colors,
            shape = shape,
            modifier = Modifier.padding(start = Dimens.small.dp, end = Dimens.small.dp),
            leadingIcon = { JustIcon(icon = icon) },
        )
    }


}
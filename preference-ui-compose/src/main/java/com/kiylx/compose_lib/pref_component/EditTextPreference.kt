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

import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

/**
 * If apart from input text change you also want to observe the
 * cursor location, selection range, or IME composition use the
 * OutlinedTextField overload with the [TextFieldValue] parameter instead.
 *
 * @param keyName Identify the storage of preference values
 * @param defaultValue the input text to be shown in the text field
 * @param title the optional label to be displayed inside the text field
 *     container. The default text style for internal [Text] is
 *     [Typography.bodySmall] when the text field is in focus and
 *     [Typography.bodyLarge] when the text field is not in focus
 * @param icon the optional leading icon to be displayed at the beginning
 *     of the text field container
 * @param enabled controls the enabled state of this text field. When
 *     `false`, this component will not respond to user input, and it will
 *     appear visually disabled and disabled to accessibility services. be
 *     modified. However, a user can focus it and copy text from it.
 *     Read-only text fields are usually used to display pre-filled forms
 *     that a user cannot edit.
 * @param dependenceKey The name of the node on which the Enabled State
 *     depends
 * @param changed the callback that is triggered when the input service
 *     updates the text. An updated text comes as a parameter of the
 *     callback
 * @param textStyle the style to be applied to the input text. Defaults to
 *     [LocalTextStyle].
 * @param placeholder the optional placeholder to be displayed when the
 *     text field is in focus and the input text is empty. The default text
 *     style for internal [Text] is [Typography.bodyLarge]
 * @param trailingIcon the optional trailing icon to be displayed at the
 *     end of the text field container
 * @param prefix the optional prefix to be displayed before the input text
 *     in the text field
 * @param suffix the optional suffix to be displayed after the input text
 *     in the text field
 * @param supportingText the optional supporting text to be displayed below
 *     the text field
 * @param isError indicates if the text field's current value is in error.
 *     If set to true, the label, bottom indicator and trailing icon by
 *     default will be displayed in error color
 * @param visualTransformation transforms the visual representation of the
 *     input [value] For example, you can use
 *     [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation]
 *     to create a password text field. By default, no visual
 *     transformation is applied.
 * @param keyboardOptions software keyboard options that contains
 *     configuration such as [KeyboardType] and [ImeAction]
 * @param keyboardActions when the input service emits an IME action, the
 *     corresponding callback is called. Note that this IME action may be
 *     different from what you specified in [KeyboardOptions.imeAction]
 * @param singleLine when `true`, this text field becomes a single
 *     horizontally scrolling text field instead of wrapping onto multiple
 *     lines. The keyboard will be informed to not show the return key as
 *     the [ImeAction]. Note that [maxLines] parameter will be ignored as
 *     the maxLines attribute will be automatically set to 1.
 * @param maxLines the maximum height in terms of maximum number of visible
 *     lines. It is required that 1 <= [minLines] <= [maxLines]. This
 *     parameter is ignored when [singleLine] is true.
 * @param minLines the minimum height in terms of minimum number of visible
 *     lines. It is required that 1 <= [minLines] <= [maxLines]. This
 *     parameter is ignored when [singleLine] is true.
 * @param interactionSource the [MutableInteractionSource] representing the
 *     stream of [Interaction]s for this text field. You can create and
 *     pass in your own `remember`ed instance to observe [Interaction]s and
 *     customize the appearance / behavior of this text field in different
 *     states.
 * @param shape defines the shape of this text field's border
 * @param colors [TextFieldColors] that will be used to resolve the colors
 *     used for this text field in different states. See
 *     [OutlinedTextFieldDefaults.colors].
 */
@Composable
fun OutlinedEditTextPreference(
    keyName: String,
    defaultValue: String = "",
    title: String,
    icon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
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
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
    changed: (it: String) -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var text by remember { mutableStateOf(pref.readValue()) }

    LaunchedEffect(key1 = text, block = {
        pref.write(text)
        changed(text)
    })

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PreferenceDimenTokens.medium.dp, vertical = PreferenceDimenTokens.small.dp)
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
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
            modifier = Modifier.padding(start = PreferenceDimenTokens.small.dp, end = PreferenceDimenTokens.small.dp),
            leadingIcon = { JustIcon(icon = icon) },
        )
    }


}

/**
 * If apart from input text change you also want to observe the cursor location, selection range,
 * or IME composition use the TextField overload with the [TextFieldValue] parameter instead.
 *
 * @param keyName Identify the storage of preference values
 * @param dependenceKey The name of the node on which the Enabled State depends
 * @param defaultValue the input text to be shown in the text field
 * @param changed the callback that is triggered when the input service updates the text. An
 * updated text comes as a parameter of the callback
 * @param enabled controls the enabled state of this text field. When `false`, this component will
 * not respond to user input, and it will appear visually disabled and disabled to accessibility
 * services.
 * be modified. However, a user can focus it and copy text from it. Read-only text fields are
 * usually used to display pre-filled forms that a user cannot edit.
 * @param textStyle the style to be applied to the input text. Defaults to [LocalTextStyle].
 * @param title the optional label to be displayed inside the text field container. The default
 * text style for internal [Text] is [Typography.bodySmall] when the text field is in focus and
 * [Typography.bodyLarge] when the text field is not in focus
 * @param placeholder the optional placeholder to be displayed when the text field is in focus and
 * the input text is empty. The default text style for internal [Text] is [Typography.bodyLarge]
 * @param icon the optional leading icon to be displayed at the beginning of the text field
 * container
 * @param trailingIcon the optional trailing icon to be displayed at the end of the text field
 * container
 * @param prefix the optional prefix to be displayed before the input text in the text field
 * @param suffix the optional suffix to be displayed after the input text in the text field
 * @param supportingText the optional supporting text to be displayed below the text field
 * @param isError indicates if the text field's current value is in error. If set to true, the
 * label, bottom indicator and trailing icon by default will be displayed in error color
 * @param visualTransformation transforms the visual representation of the input [value]
 * For example, you can use
 * [PasswordVisualTransformation][androidx.compose.ui.text.input.PasswordVisualTransformation] to
 * create a password text field. By default, no visual transformation is applied.
 * @param keyboardOptions software keyboard options that contains configuration such as
 * [KeyboardType] and [ImeAction]
 * @param keyboardActions when the input service emits an IME action, the corresponding callback
 * is called. Note that this IME action may be different from what you specified in
 * [KeyboardOptions.imeAction]
 * @param singleLine when `true`, this text field becomes a single horizontally scrolling text field
 * instead of wrapping onto multiple lines. The keyboard will be informed to not show the return key
 * as the [ImeAction]. Note that [maxLines] parameter will be ignored as the maxLines attribute will
 * be automatically set to 1.
 * @param maxLines the maximum height in terms of maximum number of visible lines. It is required
 * that 1 <= [minLines] <= [maxLines]. This parameter is ignored when [singleLine] is true.
 * @param minLines the minimum height in terms of minimum number of visible lines. It is required
 * that 1 <= [minLines] <= [maxLines]. This parameter is ignored when [singleLine] is true.
 * @param interactionSource the [MutableInteractionSource] representing the stream of [Interaction]s
 * for this text field. You can create and pass in your own `remember`ed instance to observe
 * [Interaction]s and customize the appearance / behavior of this text field in different states.
 * @param shape defines the shape of this text field's container
 * @param colors [TextFieldColors] that will be used to resolve the colors used for this text field
 * in different states. See [TextFieldDefaults.colors].
 */
@Composable
fun FilledEditTextPreference(
    keyName: String,
    defaultValue: String = "",
    title: String,
    icon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
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
    shape: Shape = TextFieldDefaults.shape,
    colors: TextFieldColors = TextFieldDefaults.colors(),
    changed: (it: String) -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getSingleDataEditor(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(
        keyName,
        enabled,
        dependenceKey
    ).enableStateFlow.collectAsState()

    var text by remember { mutableStateOf(pref.readValue()) }

    LaunchedEffect(key1 = text, block = {
        pref.write(text)
        changed(text)
    })

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = PreferenceDimenTokens.medium.dp, vertical = PreferenceDimenTokens.small.dp)
    ) {
        TextField(
            value = text,
            onValueChange = {
                text = it
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
            modifier = Modifier.padding(start = PreferenceDimenTokens.small.dp, end = PreferenceDimenTokens.small.dp),
            leadingIcon = { JustIcon(icon = icon) },
        )
    }


}

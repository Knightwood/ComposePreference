package com.kiylx.compose_lib.pref_component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.kiylx.compose_lib.R
import kotlinx.coroutines.launch


/**
 * @param changed 将会得到新值
 * @param enabled 状态，若存在dependenceKey，自身的状态则依据dependenceKey指定的依赖，而不是enable参数
 * 若不存在dependenceKey，自身和其依赖者都会依据enable参数
 * @param dependenceKey 依赖于哪个key的状态，若为null，自身显现出来的状态依据enable参数
 */
@Composable
fun PreferenceSwitch(
    keyName: String,
    defaultValue: Boolean = false,
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    checkedIcon: ImageVector = Icons.Outlined.Check,
    changed: (newValue: Boolean) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getReadWriteTool(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(keyName, enabled, dependenceKey).enableState

    var isChecked by remember {
        mutableStateOf(defaultValue)
    }
    LaunchedEffect(key1 = Unit, block = {
        pref.read().collect {
            isChecked = it
            changed(it)
        }
    })

    fun write(checked: Boolean) {
        scope.launch {
            pref.write(checked)
        }
    }

    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = checkedIcon,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Surface(
        modifier = Modifier.toggleable(
            value = isChecked,
            enabled = dependenceState.value,
            onValueChange = { checked ->
                write(checked)
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.all.horizontal.dp, Dimens.all.vertical.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(
                icon = icon,
                enabled = dependenceState.value
            )
            MediumTextContainer(icon = icon) {
                PreferenceItemTitleText(text = title, enabled = dependenceState.value, maxLines = 2)
                description?.let {
                    PreferenceItemDescriptionText(
                        text = it,
                        enabled = dependenceState.value
                    )
                }
            }
            Switch(
                checked = isChecked,
                onCheckedChange = null,
                modifier = Modifier.padding(start = Dimens.end.start.dp, end = Dimens.end.end.dp),
                enabled = dependenceState.value,
                thumbContent = thumbContent
            )
        }
    }
}

/**
 * @param onClick 点击除了switch之外的部分，例如点击后，触发跳转另一个页面
 * @param changed 将得到switch的新值
 *
 */
@Composable
fun PreferenceSwitchWithDivider(
    keyName: String,
    defaultValue: Boolean = false,
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    isSwitchEnabled: Boolean = enabled,
    checkedIcon: ImageVector = Icons.Outlined.Check,
    changed: (it: Boolean) -> Unit = {},
    onClick: (() -> Unit) = {},
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getReadWriteTool(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(keyName, enabled, dependenceKey).enableState

    var isChecked by remember {
        mutableStateOf(defaultValue)
    }
    LaunchedEffect(key1 = Unit, block = {
        pref.read().collect {
            isChecked = it
            changed(it)
        }
    })

    fun write(checked: Boolean) {
        scope.launch {
            pref.write(checked)
        }
    }

    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = checkedIcon,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Surface(
        modifier = Modifier.clickable(
            enabled = dependenceState.value,
            onClick = onClick,
            onClickLabel = stringResource(id = R.string.open_settings)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.all.horizontal.dp, Dimens.all.vertical.dp)
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(icon = icon, enabled = dependenceState.value)
            MediumTextContainer(icon = icon) {
                PreferenceItemTitleText(text = title, enabled = dependenceState.value)
                if (!description.isNullOrEmpty()) PreferenceItemDescriptionText(
                    text = description,
                    enabled = dependenceState.value
                )
            }
            VerticalDivider(
                modifier = Modifier
                    .height(Dimens.large_xx.dp)
                    .padding(horizontal = Dimens.small.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                thickness = 1f.dp
            )
            Switch(
                checked = isChecked,
                onCheckedChange = { checked ->
                    write(checked)
                },
                modifier = Modifier
                    .padding(start = Dimens.end.start.dp, end = Dimens.end.end.dp)
                    .semantics {
                        contentDescription = title
                    },
                enabled = (isSwitchEnabled && dependenceState.value),
                thumbContent = thumbContent
            )
        }
    }
}

@Composable
fun PreferenceSwitchWithContainer(
    keyName: String,
    defaultValue: Boolean = false,
    title: String,
    icon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    changed: (it: Boolean) -> Unit = {},
) {
    val scope = rememberCoroutineScope()
    val prefStoreHolder = LocalPrefs.current
    val pref = prefStoreHolder.getReadWriteTool(keyName = keyName, defaultValue = defaultValue)
    //注册自身节点，并且获取目标节点的状态
    val dependenceState = prefStoreHolder.getDependence(keyName, enabled, dependenceKey).enableState

    var isChecked by remember {
        mutableStateOf(defaultValue)
    }
    LaunchedEffect(key1 = Unit, block = {
        pref.read().collect {
            isChecked = it
            changed(it)
        }
    })
    fun write(checked: Boolean) {
        scope.launch {
            pref.write(checked)
        }
    }

    val thumbContent: (@Composable () -> Unit)? = if (isChecked) {
        {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.all.horizontal.dp, vertical = Dimens.all.vertical.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                (if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline).let {
                    it.applyOpacity(dependenceState.value)
                }
            )
            .toggleable(
                value = isChecked,
                enabled = dependenceState.value,
            ) { checked ->
                write(checked)
            }
            .padding(horizontal = Dimens.medium.dp, vertical = Dimens.large.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WrappedIcon(
            icon = icon,
            enabled = dependenceState.value,
            tint = if (isChecked) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.surface
        )
        MediumTextContainer(icon = icon) {
            with(MaterialTheme) {
                PreferenceItemTitleText(
                    text = title,
                    maxLines = 2,
                    enabled = dependenceState.value,
                    style = Typography.preferenceMediumTitle,
                    color = if (isChecked) colorScheme.onPrimary else colorScheme.surface
                )
            }
        }
        Switch(
            checked = isChecked,
            onCheckedChange = null,
            enabled = dependenceState.value,
            modifier = Modifier.padding(start = 12.dp, end = 6.dp),
            thumbContent = thumbContent,
            colors = SwitchDefaults.colors(
                checkedIconColor = MaterialTheme.colorScheme.onPrimary,
                checkedThumbColor = MaterialTheme.colorScheme.primary,
                checkedTrackColor = MaterialTheme.colorScheme.onPrimary,
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}
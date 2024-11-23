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

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kiylx.compose.preference.ui.icons.ArrowDropDown
import com.kiylx.compose.preference.ui.icons.ArrowDropUp
import com.kiylx.compose.preference.ui.icons.Translate

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun PreferenceLayout(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    hasIcon: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    dependenceKey: String? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
    startContent: @Composable RowScope.(paddingValues: PaddingValues, enabled: Boolean) -> Unit = { _, _ -> },
    endContent: @Composable RowScope.(paddingValues: PaddingValues, enabled: Boolean) -> Unit = { _, _ -> },
    mediumContent: @Composable ColumnScope.(enabled: Boolean) -> Unit = { _ -> }
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(
            dependenceKey,
            enabled
        ).enableStateFlow.collectAsState()
    Surface(
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onClickLabel = onClickLabel,
            enabled = dependenceState.value,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            startContent(
                dimens.startItem,
                dependenceState.value
            )
            MediumTextContainer(
                icon = hasIcon,
                paddingValues = dimens.mediumBox
            ) {
                mediumContent(dependenceState.value)
            }
            endContent(
                dimens.endItem,
                dependenceState.value
            )
        }
    }

}


/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param enabled 是否启用
 * @param icon 左侧图标.如果为null,则不显示图标，如果传入值无法解析为图标,则显示为[Spacer]
 * @param endIcon 右侧图标 .如果为null,则不显示图标，如果传入值无法解析为图标,则显示为[Spacer]
 * @param dependenceKey
 *    若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param onLongClickLabel onLongClick 操作的语义/辅助功能标签
 * @param onLongClick 长按事件
 * @param onClickLabel onClick 操作的语义/辅助功能标签
 * @param onClick 点击事件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItem(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    enabled: Boolean = true,
    icon: Any? = null,
    endIcon: Any? = null,
    endIconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,

    containerShape: Shape = RectangleShape,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    containerContentColor: Color = contentColorFor(containerColor),
    containerTonalElevation: Dp = 0.dp,
    containerShadowElevation: Dp = 0.dp,
    containerBorder: BorderStroke? = null,

    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    dependenceKey: String? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(
            dependenceKey,
            enabled
        ).enableStateFlow.collectAsState()
    Surface(
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onClickLabel = onClickLabel,
            enabled = dependenceState.value,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick
        ),
        shape = containerShape,
        color = containerColor,
        contentColor = containerContentColor,
        tonalElevation = containerTonalElevation,
        shadowElevation = containerShadowElevation,
        border = containerBorder,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(
                icon = icon,
                iconSize = dimens.iconSize,
                paddingValues = dimens.startItem,
                enabled = dependenceState.value
            )
            MediumTextContainer(
                icon = icon,
                paddingValues = dimens.mediumBox
            ) {
                PreferenceItemTitleText(
                    text = title,
                    style = textStyle.title,
                    enabled = dependenceState.value, maxLines = 2
                )
                if (description != null) {
                    PreferenceItemDescriptionText(
                        text = description,
                        style = textStyle.body,
                        enabled = dependenceState.value
                    )
                }
            }
            WrappedIcon(
                icon = endIcon,
                iconSize = dimens.iconSize,
                paddingValues = dimens.endItem,
                enabled = dependenceState.value,
                tint = endIconTint,
            )
        }
    }

}


/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标.如果为null,则不显示图标，如果传入值无法解析为图标,则显示为[Spacer]
 * @param endIcon 右侧图标 .如果为null,则不显示图标，如果传入值无法解析为图标,则显示为[Spacer]
 * @param enabled 是否启用
 * @param dependenceKey
 *    若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param onLongClickLabel onLongClick 操作的语义/辅助功能标签
 * @param onLongClick 长按事件
 * @param onClickLabel onClick 操作的语义/辅助功能标签
 * @param onClick 点击事件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItemVariant(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: Any? = null,
    endIcon: Any? = null,
    enabled: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.smallTextStyle,
    dependenceKey: String? = null,
    onLongClickLabel: String? = null,
    onLongClick: () -> Unit = {},
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(
            dependenceKey,
            enabled
        ).enableStateFlow.collectAsState()
    Surface(
        modifier = modifier.combinedClickable(
            enabled = dependenceState.value,
            onClick = onClick,
            onClickLabel = onClickLabel,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(
                icon = icon, iconSize = dimens.iconSize,
                paddingValues = dimens.startItem, enabled = dependenceState.value
            )
            MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
                with(MaterialTheme) {
                    PreferenceItemTitleText(
                        text = title,
                        maxLines = 1,
                        enabled = dependenceState.value,
                        style = textStyle.title,
                        color = colorScheme.onSurface.applyOpacity(enabled = dependenceState.value)
                    )
                    description?.let {
                        PreferenceItemDescriptionText(
                            text = it,
                            enabled = dependenceState.value,
                            color = colorScheme.onSurfaceVariant.applyOpacity(enabled = dependenceState.value),
                            maxLines = 2, overflow = TextOverflow.Ellipsis,
                            style = textStyle.body,
                        )
                    }
                }
            }
            WrappedIcon(
                icon = endIcon,
                iconSize = dimens.iconSize,
                paddingValues = dimens.endItem,
                enabled = enabled
            )
        }
    }

}

/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param dependenceKey
 *    若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param enabled 是否启用
 * @param close 默认是打开、关闭状态
 * @param stateChanged 展开、关闭状态事件通知
 * @param content 折叠内容
 */
@Composable
fun PreferenceCollapseBox(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: Any? = null,
    dependenceKey: String? = null,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    enabled: Boolean = true,
    close: Boolean = false,
    stateChanged: (isOpen: Boolean) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    var isOpen by remember {
        mutableStateOf(close)
    }
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(
            dependenceKey,
            enabled
        ).enableStateFlow.collectAsState()

    Surface(
        modifier = modifier.toggleable(isOpen, dependenceState.value, null) {
            isOpen = it
            stateChanged(it)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WrappedIcon(
                    icon = icon, iconSize = dimens.iconSize,
                    paddingValues = dimens.startItem, enabled = dependenceState.value
                )
                MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
                    PreferenceItemTitleText(
                        text = title,
                        enabled = dependenceState.value,
                        style = textStyle.title,
                        maxLines = 2
                    )
                    if (description != null) PreferenceItemDescriptionText(
                        text = description,
                        style = textStyle.body,
                        enabled = dependenceState.value
                    )
                }
                JustIcon(
                    icon = if (!isOpen) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                    enabled = dependenceState.value,
                    modifier = Modifier
                        .padding(end = PreferenceDimenTokens.large.dp)
                        .size(PreferenceDimenTokens.large_x.dp),
                )
            }
            AnimatedVisibility(visible = isOpen && dependenceState.value) {
                Column {
                    content()
                    HorizontalDivider(modifier = Modifier.padding(horizontal = PreferenceDimenTokens.medium.dp))
                }
            }
        }

    }

}


/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param onClick 点击事件
 */
@Composable
fun PreferencesCautionCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    icon: Any? = null,
    onClick: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            //外层padding
            .padding(dimens.boxItemVert)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.errorContainer.harmonizeWithPrimary())
            .clickable { onClick() }
            //内层padding
            .padding(
                horizontal = PreferenceDimenTokens.medium.dp,
                vertical = PreferenceDimenTokens.large.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WrappedIcon(
            icon = icon,
            iconSize = dimens.iconSize,
            paddingValues = dimens.startItem,
            enabled = true
        )
        MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
            with(MaterialTheme) {
                PreferenceItemTitleText(
                    text = title,
                    maxLines = 1,
                    style = textStyle.title,
                    color = colorScheme.onErrorContainer.harmonizeWithPrimary()
                )
                description?.let {
                    PreferenceItemDescriptionText(
                        text = it,
                        color = colorScheme.onErrorContainer.harmonizeWithPrimary(),
                        maxLines = 2, overflow = TextOverflow.Ellipsis,
                        style = textStyle.body,

                        )
                }
            }
        }
    }


}

/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param backgroundColor 背景色
 * @param contentColor 内容颜色
 * @param onClick 点击事件
 */
@Composable
fun PreferencesHintCard(
    modifier: Modifier = Modifier,
    title: String = "Title ".repeat(2),
    description: String? = "Description text ".repeat(3),
    icon: Any? = Icons.Default.Translate,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.normalTextStyle,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                dimens.boxItemVert
            )
            .clip(MaterialTheme.shapes.extraLarge)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(
                horizontal = PreferenceDimenTokens.medium.dp,
                vertical = PreferenceDimenTokens.large.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WrappedIcon(
            icon = icon,
            iconSize = dimens.iconSize,
            paddingValues = dimens.startItem,
            tint = contentColor
        )
        MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
            PreferenceItemTitleText(
                text = title,
                maxLines = 1,
                style = textStyle.title,
                color = contentColor
            )
            description?.let {
                PreferenceItemDescriptionText(
                    text = description,
                    color = contentColor,
                    style = textStyle.body,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标.如果为null,则不显示图标，如果传入值无法解析为图标,则显示为[Spacer]
 * @param endIcon 右侧图标 .如果为null,则不显示图标，如果传入值无法解析为图标,则显示为[Spacer]
 * @param dependenceKey
 *    若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param enabled 是否启用
 * @param onLongClickLabel onLongClick 操作的语义/辅助功能标签
 * @param onLongClick 长按事件
 * @param onClickLabel onClick 操作的语义/辅助功能标签
 * @param onClick 点击事件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItemLargeTitle(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: Any? = null,
    endIcon: Any? = null,
    dependenceKey: String? = null,
    enabled: Boolean = true,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.largeTextStyle,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(
            dependenceKey,
            enabled
        ).enableStateFlow.collectAsState()

    Surface(
        modifier = modifier.combinedClickable(
            onClick = onClick,
            onClickLabel = onClickLabel,
            enabled = dependenceState.value,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimens.boxItem),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(
                icon = icon,
                iconSize = dimens.iconSize,
                paddingValues = dimens.startItem,
                enabled = dependenceState.value
            )
            MediumTextContainer(icon = icon, paddingValues = dimens.mediumBox) {
                PreferenceItemTitleText(
                    text = title,
                    style = textStyle.title,
                    enabled = dependenceState.value, maxLines = 1,
                )
                if (description != null) PreferenceItemDescriptionText(
                    text = description,
                    style = textStyle.body,
                    enabled = dependenceState.value
                )
            }
            WrappedIcon(
                icon = endIcon,
                iconSize = dimens.iconSize,
                paddingValues = dimens.endItem,
                enabled = dependenceState.value
            )
        }
    }

}

/**
 * @param title 标题
 * @param color 文本颜色
 * @param dependenceKey
 *    若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param enabled 是否启用
 */
@Composable
fun PreferenceItemSubTitle(
    modifier: Modifier = Modifier,
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    dimens: PreferenceDimens = PreferenceTheme.preferenceDimens,
    textStyle: PreferenceTextStyle = PreferenceTheme.smallTextStyle,
    dependenceKey: String? = null,
    enabled: Boolean = true,
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(
            dependenceKey,
            enabled
        ).enableStateFlow.collectAsState()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimens.boxItem),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MediumTextContainer(paddingValues = dimens.mediumBox) {
            PreferenceItemTitleText(
                text = title,
                modifier = Modifier
                    .fillMaxWidth(),
                enabled = dependenceState.value,
                color = color,
                style = textStyle.title
            )
        }
    }
}

//<editor-fold desc="公共组件方法">


@Composable
fun WrappedIcon(
    icon: Any? = null, enabled: Boolean = true,
    paddingValues: PaddingValues = PreferenceTheme.preferenceDimens.startItem,
    iconSize: Dp = PreferenceTheme.preferenceDimens.iconSize,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    val iconModifier = Modifier
        .padding(paddingValues)
        .size(iconSize)
    JustIcon(
        modifier = iconModifier,
        icon = icon,
        enabled = enabled,
        tint = tint.applyOpacity(enabled)
    )
}

/**
 * @param icon 如果为null,则不显示图标，如果传入值无法解析为图标,则显示为[Spacer]
 */
@Composable
internal fun JustIcon(
    modifier: Modifier = Modifier,
    icon: Any? = null,
    enabled: Boolean = true,
    contentDescription: String? = null,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled)
) {
    when (icon) {
        is ImageVector -> {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }

        is Painter -> {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                modifier = modifier,
                tint = tint
            )
        }

        is Int -> {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = contentDescription,
                modifier = modifier,
            )
        }

        else -> {
            if (icon != null) {
                Spacer(modifier = modifier)
            }
        }
    }
}

@Composable
internal fun PreferenceItemTitleText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 2,
    style: TextStyle = PreferenceTheme.typography.titleMedium,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.onBackground,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        modifier = modifier,
        text = text,
        maxLines = maxLines,
        style = style,
        color = color.applyOpacity(enabled),
        overflow = overflow
    )
}

@Composable
internal fun PreferenceItemDescriptionText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = Int.MAX_VALUE,
    style: TextStyle = PreferenceTheme.typography.bodyMedium,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    overflow: TextOverflow = TextOverflow.Ellipsis
) {
    Text(
        modifier = modifier.padding(top = 2.dp),
        text = text,
        maxLines = maxLines,
        style = style,
        color = color.applyOpacity(enabled),
        overflow = overflow
    )
}

/**
 * 中间的文本，由item的标题和描述组成
 */
@Composable
internal fun RowScope.MediumTextContainer(
    icon: Any? = null,
    paddingValues: PaddingValues = PreferenceTheme.preferenceDimens.mediumBox,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(
                start = if (icon == null) paddingValues.start else 0.dp,
                end = paddingValues.end
            ),
    ) {
        content()
    }
}
//</editor-fold>
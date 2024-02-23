package com.kiylx.compose_lib.pref_component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kiylx.compose_lib.pref_component.Typography.preferenceDescription
import com.kiylx.compose_lib.pref_component.Typography.preferenceMediumTitle
import com.kiylx.compose_lib.pref_component.icons.ArrowDropDown
import com.kiylx.compose_lib.pref_component.icons.ArrowDropUp
import com.kiylx.compose_lib.pref_component.icons.Translate

/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param endIcon 左侧图标
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param onLongClickLabel onLongClick 操作的语义/辅助功能标签
 * @param onLongClick 长按事件
 * @param onClickLabel onClick 操作的语义/辅助功能标签
 * @param onClick 点击事件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItem(
    title: String,
    description: String? = null,
    icon: Any? = null,
    endIcon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(dependenceKey,enabled).enableStateFlow.collectAsState()
    Surface(
        modifier = Modifier.combinedClickable(
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
                .padding(Dimens.title.horizontal_start.dp, Dimens.title.vertical_top.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(icon = icon, enabled = dependenceState.value)
            MediumTextContainer(icon = icon) {
                PreferenceItemTitleText(text = title, enabled = dependenceState.value, maxLines = 2)
                if (description != null) PreferenceItemDescriptionText(
                    text = description,
                    enabled = dependenceState.value
                )
            }
            WrappedIcon(icon = endIcon, enabled = dependenceState.value)
        }
    }

}


/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param endIcon 左侧图标
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param onLongClickLabel onLongClick 操作的语义/辅助功能标签
 * @param onLongClick 长按事件
 * @param onClickLabel onClick 操作的语义/辅助功能标签
 * @param onClick 点击事件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItemVariant(
    title: String,
    description: String? = null,
    icon: Any? = null,
    endIcon: Any? = null,
    enabled: Boolean = true,
    dependenceKey: String? = null,
    onLongClickLabel: String? = null,
    onLongClick: () -> Unit = {},
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(dependenceKey,enabled).enableStateFlow.collectAsState()
    Surface(
        modifier = Modifier.combinedClickable(
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
                .padding(
                    start = Dimens.title.horizontal_start.dp,
                    end = Dimens.title.horizontal_end.dp,
                    top = Dimens.title.vertical_top.dp,
                    bottom = Dimens.title.vertical_top.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(icon = icon, enabled = dependenceState.value)
            MediumTextContainer(icon = icon) {
                with(MaterialTheme) {
                    PreferenceItemTitleText(
                        text = title,
                        maxLines = 1,
                        enabled = dependenceState.value,
                        style = typography.titleMedium,
                        color = colorScheme.onSurface.applyOpacity(enabled = dependenceState.value)
                    )
                    description?.let {
                        PreferenceItemDescriptionText(
                            text = it,
                            enabled = dependenceState.value,
                            color = colorScheme.onSurfaceVariant.applyOpacity(enabled = dependenceState.value),
                            maxLines = 2, overflow = TextOverflow.Ellipsis,
                            style = preferenceDescription,
                        )
                    }
                }
            }
            WrappedIcon(icon = endIcon, enabled = enabled)
        }
    }

}

/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param enabled 是否启用
 * @param close 默认是打开、关闭状态
 * @param stateChanged 展开、关闭状态事件通知
 * @param content 折叠内容
 */
@Composable
fun PreferenceCollapseBox(
    title: String,
    description: String? = null,
    icon: Any? = null,
    dependenceKey: String? = null,
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
        prefStoreHolder.getDependenceNotEmpty(dependenceKey,enabled).enableStateFlow.collectAsState()

    Surface(
        modifier = Modifier.toggleable(isOpen, dependenceState.value, null) {
            isOpen = it
            stateChanged(it)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.title.horizontal_start.dp, Dimens.title.vertical_top.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WrappedIcon(icon = icon, enabled = dependenceState.value)
                MediumTextContainer(icon = icon) {
                    PreferenceItemTitleText(text = title, enabled = dependenceState.value, maxLines = 2)
                    if (description != null) PreferenceItemDescriptionText(
                        text = description,
                        enabled = dependenceState.value
                    )
                }
                JustIcon(
                    icon = if (!isOpen) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                    enabled = dependenceState.value,
                    modifier = Modifier
                        .padding(end = Dimens.large.dp)
                        .size(Dimens.large_x.dp),
                )
            }
            AnimatedVisibility(visible = isOpen && dependenceState.value) {
                Column {
                    content()
                    HorizontalDivider(modifier = Modifier.padding(horizontal = Dimens.medium.dp))
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
    title: String,
    description: String? = null,
    icon: Any? = null,
    onClick: () -> Unit = {},
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.title.horizontal_start.dp,
                vertical = Dimens.title.vertical_top.dp
            )
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.colorScheme.errorContainer.harmonizeWithPrimary())
            .clickable { onClick() }
            .padding(horizontal = Dimens.medium.dp, vertical = Dimens.large.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WrappedIcon(icon = icon, enabled = true)
        MediumTextContainer(icon = icon) {
            with(MaterialTheme) {
                PreferenceItemTitleText(
                    text = title,
                    maxLines = 1,
                    style = typography.titleLarge,
                    color = colorScheme.onErrorContainer.harmonizeWithPrimary()
                )
                description?.let {
                    PreferenceItemDescriptionText(
                        text = it,
                        color = colorScheme.onErrorContainer.harmonizeWithPrimary(),
                        maxLines = 2, overflow = TextOverflow.Ellipsis,
                        style = preferenceDescription,
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
    title: String = "Title ".repeat(2),
    description: String? = "Description text ".repeat(3),
    icon: Any? = Icons.Default.Translate,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = Dimens.title.horizontal_start.dp,
                vertical = Dimens.title.vertical_top.dp
            )
            .clip(MaterialTheme.shapes.extraLarge)
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = Dimens.medium.dp, vertical = Dimens.large.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        WrappedIcon(icon = icon, tint = contentColor)
        MediumTextContainer(icon = icon) {
            PreferenceItemTitleText(
                text = title,
                maxLines = 1,
                style = preferenceMediumTitle,
                color = contentColor
            )
            description?.let {
                PreferenceItemDescriptionText(
                    text = description,
                    color = contentColor,
                    maxLines = 2, overflow = TextOverflow.Ellipsis,
                    style = preferenceDescription,
                )
            }
        }
    }
}

/**
 * @param title 标题
 * @param description 标题下方的描述信息
 * @param icon 左侧图标
 * @param endIcon 左侧图标
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 * @param onLongClickLabel onLongClick 操作的语义/辅助功能标签
 * @param onLongClick 长按事件
 * @param onClickLabel onClick 操作的语义/辅助功能标签
 * @param onClick 点击事件
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItemLargeTitle(
    title: String,
    description: String? = null,
    icon: Any? = null,
    endIcon: Any? = null,
    dependenceKey: String? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(dependenceKey,enabled).enableStateFlow.collectAsState()

    Surface(
        modifier = Modifier.combinedClickable(
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
                .padding(
                    start = Dimens.medium.dp,
                    end = Dimens.medium.dp,
                    top = Dimens.large.dp,
                    bottom = Dimens.large.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(icon = icon, enabled = dependenceState.value)
            MediumTextContainer(icon = icon) {
                PreferenceItemTitleText(
                    text = title,
                    enabled = dependenceState.value, maxLines = 1,
                    style = Typography.preferenceLargeTitle
                )
                if (description != null) PreferenceItemDescriptionText(
                    text = description,
                    enabled = dependenceState.value
                )
            }
            WrappedIcon(icon = endIcon, enabled = dependenceState.value)
        }
    }

}

/**
 * @param title 标题
 * @param color 文本颜色
 * @param enabled 是否启用
 * @param dependenceKey 若为null,则启用状态依照enable值;若不为null,则启用状态依赖dependenceKey指向的节点
 */
@Composable
fun PreferenceItemSubTitle(
    title: String,
    color: Color = MaterialTheme.colorScheme.primary,
    dependenceKey: String? = null,
    enabled: Boolean = true,
) {
    val prefStoreHolder = LocalPrefs.current
    //不注册自身节点，仅获取目标节点的状态
    val dependenceState =
        prefStoreHolder.getDependenceNotEmpty(dependenceKey,enabled).enableStateFlow.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = Dimens.title.horizontal_start.dp,
                end = Dimens.title.horizontal_end.dp,
                top = Dimens.title.vertical_top.dp,
                bottom = Dimens.title.vertical_bottom.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MediumTextContainer() {
            PreferenceItemTitleText(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimens.large_x.dp,
                        bottom = Dimens.small.dp
                    ),
                enabled = dependenceState.value,
                color = color,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

//<editor-fold desc="公共组件方法">


@Composable
internal fun WrappedIcon(
    icon: Any? = null, enabled: Boolean = true,
    tint: Color = MaterialTheme.colorScheme.onSurfaceVariant.applyOpacity(enabled)
) {
    val iconModifier = Modifier
        .padding(start = Dimens.icon.start.dp, end = Dimens.icon.end.dp)
        .size(Dimens.icon.size.dp)
    JustIcon(modifier = iconModifier, icon = icon, enabled = enabled, tint = tint)
}

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
    }
}

@Composable
internal fun PreferenceItemTitleText(
    modifier: Modifier = Modifier,
    text: String,
    maxLines: Int = 2,
    style: TextStyle = preferenceMediumTitle,
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
    style: TextStyle = preferenceDescription,
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
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .padding(
                start = if (icon == null) Dimens.text.start.dp else 0.dp,
                end = Dimens.text.end.dp
            ),
    ) {
        content()
    }
}
//</editor-fold>
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

/**
 * preference item
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItem(
    title: String,
    description: String? = null,
    icon: Any? = null,
    endIcon: Any? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onClickLabel = onClickLabel,
            enabled = enabled,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.all.horizontal.dp, Dimens.all.vertical.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(icon = icon, enabled = enabled)
            MediumTextContainer(icon = icon) {
                PreferenceItemTitleText(text = title, enabled = enabled, maxLines = 2)
                if (description != null) PreferenceItemDescriptionText(
                    text = description,
                    enabled = enabled
                )
            }
            WrappedIcon(icon = endIcon, enabled = enabled)
        }
    }

}

/**
 * preference item
 */
@Composable
fun CollapsePreferenceItem(
    title: String,
    description: String? = null,
    icon: Any? = null,
    enabled: Boolean = true,
    close: Boolean = false,
    stateChanged: (isOpen: Boolean) -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    var isOpen by remember {
        mutableStateOf(close)
    }
    Surface(
        modifier = Modifier.toggleable(isOpen, enabled, null) {
            isOpen = it
            stateChanged(it)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.all.horizontal.dp, Dimens.all.vertical.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                WrappedIcon(icon = icon, enabled = enabled)
                MediumTextContainer(icon = icon) {
                    PreferenceItemTitleText(text = title, enabled = enabled, maxLines = 2)
                    if (description != null) PreferenceItemDescriptionText(
                        text = description,
                        enabled = enabled
                    )
                }
                if (!isOpen) {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.large_x.dp),
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropUp,
                        contentDescription = null,
                        modifier = Modifier.size(Dimens.large_x.dp),
                    )

                }

            }
            AnimatedVisibility(visible = isOpen) {
                Column {
                    content()
                    HorizontalDivider(modifier =Modifier.padding(horizontal = Dimens.medium.dp))
                }
            }
        }

    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItemVariant(
    modifier: Modifier = Modifier,
    title: String,
    description: String? = null,
    icon: Any? = null,
    endIcon: Any? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: () -> Unit = {},
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier.combinedClickable(
            enabled = enabled,
            onClick = onClick,
            onClickLabel = onClickLabel,
            onLongClick = onLongClick,
            onLongClickLabel = onLongClickLabel
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(Dimens.all.horizontal.dp, Dimens.all.vertical.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(icon = icon, enabled = enabled)
            MediumTextContainer(icon = icon) {
                with(MaterialTheme) {
                    PreferenceItemTitleText(
                        text = title,
                        maxLines = 1,
                        style = typography.titleMedium,
                        color = colorScheme.onSurface.applyOpacity(enabled)
                    )
                    description?.let {
                        PreferenceItemDescriptionText(
                            text = it,
                            color = colorScheme.onSurfaceVariant.applyOpacity(enabled),
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
            .padding(horizontal = Dimens.all.horizontal.dp, vertical = Dimens.all.vertical.dp)
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

@Composable
fun PreferencesHintCard(
    title: String = "Title ".repeat(2),
    description: String? = "Description text ".repeat(3),
    icon: Any? = Icons.Outlined.Translate,
    backgroundColor: Color = MaterialTheme.colorScheme.secondary,
    contentColor: Color = MaterialTheme.colorScheme.onSecondary,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.all.horizontal.dp, vertical = Dimens.all.vertical.dp)
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
 * preference item
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreferenceItemLargeTitle(
    title: String,
    icon: Any? = null,
    enabled: Boolean = true,
    onLongClickLabel: String? = null,
    onLongClick: (() -> Unit)? = null,
    onClickLabel: String? = null,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier.combinedClickable(
            onClick = onClick,
            onClickLabel = onClickLabel,
            enabled = enabled,
            onLongClickLabel = onLongClickLabel,
            onLongClick = onLongClick
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens.all.horizontal.dp, Dimens.all.vertical.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WrappedIcon(icon = icon, enabled = enabled)
            MediumTextContainer(icon = icon) {
                PreferenceItemTitleText(
                    text = title,
                    enabled = enabled, maxLines = 1,
                    style = Typography.preferenceLargeTitle
                )
            }
        }
    }

}

/**
 * 类似小标题
 */
@Composable
fun PreferenceItemSubTitle(
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(Dimens.all.horizontal.dp, Dimens.all.vertical.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MediumTextContainer() {
            Text(
                text = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = Dimens.large_x.dp,
                        bottom = Dimens.medium.dp
                    ),
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
    contentDescription:String?=null,
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
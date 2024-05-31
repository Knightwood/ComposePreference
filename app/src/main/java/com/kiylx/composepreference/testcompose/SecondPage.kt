package com.kiylx.composepreference.testcompose

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kiylx.compose_lib.pref_component.FilledEditTextPreference
import com.kiylx.compose_lib.pref_component.MenuDivider
import com.kiylx.compose_lib.pref_component.MenuEntity
import com.kiylx.compose_lib.pref_component.OutlinedEditTextPreference
import com.kiylx.compose_lib.pref_component.PreferenceCheckBoxGroup
import com.kiylx.compose_lib.pref_component.PreferenceCollapseBox
import com.kiylx.compose_lib.pref_component.PreferenceItem
import com.kiylx.compose_lib.pref_component.PreferenceItemLargeTitle
import com.kiylx.compose_lib.pref_component.PreferenceItemSubTitle
import com.kiylx.compose_lib.pref_component.PreferenceItemVariant
import com.kiylx.compose_lib.pref_component.PreferenceLayout
import com.kiylx.compose_lib.pref_component.PreferenceListMenu
import com.kiylx.compose_lib.pref_component.PreferenceRadioGroup
import com.kiylx.compose_lib.pref_component.PreferenceSlider
import com.kiylx.compose_lib.pref_component.PreferenceSwitch
import com.kiylx.compose_lib.pref_component.PreferenceSwitchWithContainer
import com.kiylx.compose_lib.pref_component.PreferenceSwitchWithDivider
import com.kiylx.compose_lib.pref_component.PreferenceTheme
import com.kiylx.compose_lib.pref_component.PreferenceTypographyTokens
import com.kiylx.compose_lib.pref_component.PreferencesCautionCard
import com.kiylx.compose_lib.pref_component.PreferencesHintCard
import com.kiylx.compose_lib.pref_component.WrappedIcon

@Composable
fun SecondPage() {
    val menus = remember {
        listOf(
            MenuEntity(
                leadingIcon = Icons.Outlined.Edit,
                text = "edit",
                labelKey = 0
            ),
            MenuEntity(
                leadingIcon = Icons.Outlined.Settings,
                text = "Settings",
                labelKey = 1
            ),
            MenuDivider,
            MenuEntity(
                leadingIcon = Icons.Outlined.Email,
                text = "Send Feedback",
                labelKey = 2
            ),
        )
    }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        PreferenceLayout(startContent = { paddings: PaddingValues, enabled: Boolean ->
            WrappedIcon(
                icon = Icons.Default.AccountBalanceWallet,
                paddingValues = paddings,
                enabled = enabled
            )
        }) {
            Text(
                text = "自定义preference",
                style = PreferenceTheme.normalTextStyle.title
            )
        }

        PreferenceItem(
            title = "普通的PreferenceItem",
            description = "可以带图标",
            icon = Icons.Filled.LiveTv,
            endIcon = Icons.Filled.ChevronRight
        )
        PreferenceItem(
            title = "绝大多数可带图标",
            icon = Icons.Filled.Wifi,
            endIcon = Icons.Filled.ChevronRight
        )

        PreferenceItemVariant(
            title = "PreferenceItem变种",
            icon = Icons.Filled.AccountCircle,
            endIcon = Icons.Filled.ChevronRight,
            description = "但是用起来无差别,但是用起来无差别,但是用起来无差别,但是用起来无差别,但是用起来无差别,但是用起来无差别"
        )
        PreferencesCautionCard(title = "PreferencesCautionCard")
        PreferencesHintCard(title = "PreferencesHintCard")

        PreferenceItemLargeTitle(title = "PreferenceItem大标题")
        HorizontalDivider()
        PreferenceItemSubTitle(title = "编辑框Preference")
        OutlinedEditTextPreference(
            title = "outlined编辑框", keyName = "edit11",
            defaultValue = "默认文本",
            icon = Icons.Filled.AccountCircle,
        )

        FilledEditTextPreference(
            defaultValue = "默认文本",
            title = "Filled编辑框",
            keyName = "edit12",
            icon = Icons.Filled.AccountCircle,
            changed = {

            }
        )
        HorizontalDivider()

        PreferenceItemSubTitle(title = "PreferenceItemSubTitle")
        PreferenceSwitch(
            keyName = "bol2",
            title = "普通的开关",
            description = "普通的开关",
        )
        PreferenceCollapseBox(
            title = "可折叠菜单",
            description = "preference描述",
        ) {

            PreferenceSwitch(
                keyName = "bol",
                title = "title",
                description = "description",
                icon = Icons.Filled.AccountCircle
            )
            //依赖keyName为bol的PreferenceSwitch的state
            PreferenceSwitch(
                keyName = "bol3",
                title = "title",
                dependenceKey = "bol",
                description = "description",
                icon = Icons.Filled.CenterFocusWeak
            )

        }

        PreferenceListMenu(
            title = "list菜单",
            keyName = "PreferenceListMenu",
            description = "list菜单介绍",
            list = menus
        ) {
            Log.d(TAG, "menu item labelKey: $it")
        }
        HorizontalDivider()

        PreferenceItemSubTitle(
            title = "各种switch",
        )
        PreferenceSwitchWithContainer(
            keyName = "bol4",
            title = "带Container的Switch",
            description = "描述，描述，描述，描述，描述",
            icon = null
        )

        PreferenceSwitch(
            keyName = "www",
            title = "夜间模式",
            description = "开关描述",
        ) {
            Log.d(TAG, "FirstPage: $it")
            isDarkFlow.tryEmit(it)
        }

        PreferenceSwitchWithDivider(
            keyName = "bol5",
            title = "前部可点击switch",
            description = "description",
            icon = Icons.Filled.CenterFocusWeak
        )
        HorizontalDivider()

        PreferenceItemSubTitle(
            title = "RadioGroup",
        )
        PreferenceRadioGroup(
            keyName = "radioGroup",
            labelPairs = listOf(
                "first" to 3,
                "second" to 1
            ), changed = {
                Log.d(TAG, "radio: ${it}")
            }
        )
        HorizontalDivider()

        PreferenceItemSubTitle(
            title = "CheckBoxGroup",
        )
        PreferenceCheckBoxGroup(
            keyName = "CheckBoxGroup",
            labels = listOf(
                "first",
                "second"
            ), changed = {
                Log.d(TAG, "checkbox: ${it.joinToString(",")}")
            }
        )
        HorizontalDivider()

        PreferenceItemSubTitle(
            title = "PreferenceSlider",
        )
        PreferenceSlider(
            keyName = "slider",
            min = 0f,
            max = 10f, steps = 9, defaultValue = 0f, changed = {
                Log.d(TAG, "slider: $it")
            }
        )
    }
}


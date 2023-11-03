package com.kiylx.composepreference.testcompose

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kiylx.compose_lib.pref_component.CollapsePreferenceItem
import com.kiylx.compose_lib.pref_component.MenuDivider
import com.kiylx.compose_lib.pref_component.PreferenceCheckBoxGroup
import com.kiylx.compose_lib.pref_component.PreferenceItem
import com.kiylx.compose_lib.pref_component.PreferenceItemLargeTitle
import com.kiylx.compose_lib.pref_component.PreferenceItemSubTitle
import com.kiylx.compose_lib.pref_component.PreferenceItemVariant
import com.kiylx.compose_lib.pref_component.PreferenceListMenu
import com.kiylx.compose_lib.pref_component.MenuEntity
import com.kiylx.compose_lib.pref_component.PreferenceRadioGroup
import com.kiylx.compose_lib.pref_component.PreferenceSlider
import com.kiylx.compose_lib.pref_component.PreferenceSwitch
import com.kiylx.compose_lib.pref_component.PreferenceSwitchWithContainer
import com.kiylx.compose_lib.pref_component.PreferenceSwitchWithDivider
import com.kiylx.compose_lib.pref_component.PreferencesCautionCard
import com.kiylx.compose_lib.pref_component.PreferencesHintCard
import com.kiylx.compose_lib.pref_component.PreferencesScope
import com.kiylx.compose_lib.pref_component.core.DependenceNode
import com.kiylx.compose_lib.pref_component.core.prefs.OldPreferenceHolder
import com.kiylx.composepreference.AppCtx


const val TAG = "TestPage1"

@Composable
fun FirstPage() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {

        //1. 使用dataStore存储偏好值
//                  val holder=  DataStorePreferenceHolder.instance(
//                        dataStoreName = "test",
//                        ctx = AppCtx.instance
//                    )
        //1. 使用mmkv存储偏好值
//                   val holder= MMKVPreferenceHolder.instance(MMKV.defaultMMKV())
        //3. 使用sharedprefrence存储偏好值
        val holder = OldPreferenceHolder.instance(
            AppCtx.instance.getSharedPreferences(
                "ddd",
                Context.MODE_PRIVATE
            )
        )
        PreferencesScope(holder = holder) {
            val customNodeName = "customNode"
            //创建一个自定义节点
            val node = holder.registerDependence(customNodeName, true)

            PreferenceItem(title = "PreferenceItem", endIcon = Icons.Filled.ArrowCircleRight)
            PreferenceItemVariant(title = "PreferenceItemVariant")
            PreferencesHintCard(title = "PreferencesHintCard")
            PreferenceItemLargeTitle(title = "PreferenceItemLargeTitle")
            PreferenceItemSubTitle(text = "PreferenceItemSubTitle")
            PreferencesCautionCard(title = "PreferencesCautionCard")

            CollapsePreferenceItem(
                title = "title",
                description = "description"

            ) {

                PreferenceSwitch(
                    keyName = "bol",
                    title = "title",
                    dependenceKey = DependenceNode.rootName,//指定依赖为根结点，这样自身就不会受到影响
                    description = "description",
                    icon = Icons.Filled.AccountCircle
                ) { state ->
                    //这里获取并修改了当前的enable状态，
                    //依赖这个节点的会改变显示状态，
                    //如果当前没有指定依赖，自身也会受到影响
                    holder.getDependence("bol")?.let {
                        it.enableState.value = state
                    }
                }
                //依赖keyName为bol的PreferenceSwitch的state
                PreferenceSwitch(
                    keyName = "bol3",
                    title = "title",
                    dependenceKey = "bol",
                    description = "description",
                    icon = Icons.Filled.CenterFocusWeak
                )

            }
            PreferenceSwitch(
                keyName = "bol2",
                title = "title",
                dependenceKey = DependenceNode.rootName,//指定依赖为根结点，这样自身就不会受到影响
                description = "description",
            ) {
                node.enableState.value = it
            }
            PreferenceListMenu(
                title = "PreferenceListMenu",
                keyName = "PreferenceListMenu",
                dependenceKey = customNodeName,
                list = listOf(
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
            ) {
                Log.d(TAG, "menu item labelKey: $it")
            }

            PreferenceSwitchWithContainer(
                keyName = "bol4",
                dependenceKey = customNodeName,
                title = "Title ".repeat(2),
                icon = null
            )
            PreferenceSwitchWithDivider(
                keyName = "bol5",
                title = "title",
                dependenceKey = customNodeName,
                description = "description",
                icon = Icons.Filled.CenterFocusWeak
            )

            PreferenceRadioGroup(
                keyName = "radioGroup",
                dependenceKey = customNodeName,
                labels = listOf(
                    "first",
                    "second"
                ), changed = {
                    Log.d(TAG, "radio: ${it}")
                }
            )
            PreferenceCheckBoxGroup(
                keyName = "CheckBoxGroup",
                dependenceKey = customNodeName,
                labels = listOf(
                    "first",
                    "second"
                ), changed = {
                    Log.d(TAG, "checkbox: ${it.joinToString(",")}")
                }
            )
            PreferenceSlider(
                keyName = "slider",
                dependenceKey = customNodeName, //依赖key为customNode的状态
                min = 0f,
                max = 10f, steps = 9, value = 0f, changed = {
                    Log.d(TAG, "slider: $it")
                }
            )
        }


    }
}




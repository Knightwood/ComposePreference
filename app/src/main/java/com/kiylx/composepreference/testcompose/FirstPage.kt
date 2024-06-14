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

package com.kiylx.composepreference.testcompose

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CenterFocusWeak
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.kiylx.compose_lib.pref_component.PreferenceCollapseBox
import com.kiylx.compose_lib.pref_component.OutlinedEditTextPreference
import com.kiylx.compose_lib.pref_component.FilledEditTextPreference
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
import com.kiylx.composepreference.AppCtx
import com.kiylx.libx.pref_component.core.DependenceNode
import com.kiylx.libx.pref_component.mmkv_util.MMKVPreferenceHolder
import com.kiylx.libx.pref_component.preference_util.OldPreferenceHolder
import com.tencent.mmkv.MMKV


const val TAG = "TestPage1"

@Composable
fun FirstPage() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val menu = remember {
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
        //1. 使用dataStore存储偏好值
//        val holder = remember {
//            DataStorePreferenceHolder.instance(
//                dataStoreName = "test",
//                ctx = AppCtx.instance
//            )
//        }

        //2. 使用mmkv存储偏好值
        val holder = remember {
            MMKVPreferenceHolder.instance(MMKV.defaultMMKV())
        }
        //3. 使用sharedprefrence存储偏好值
        /*val holder = remember {
            OldPreferenceHolder.instance(
                AppCtx.instance.getSharedPreferences(
                    "ddd",
                    Context.MODE_PRIVATE
                )
            )
        }*/
        PreferencesScope(holder = holder) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
            ) {

//            val holder =LocalPrefs.current
                val customNodeName = "customNode"
                //创建一个自定义节点
                val node = holder.registerDependence(customNodeName, true)
                PreferencesCautionCard(title = "PreferencesCautionCard")
                PreferencesHintCard(title = "PreferencesHintCard")

                PreferenceItemLargeTitle(title = "PreferenceItem大标题")
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

                HorizontalDivider()
                PreferenceItemSubTitle(title = "编辑框Preference")
                OutlinedEditTextPreference(
                    title = "outlined编辑框", keyName = "edit11",
                    defaultValue = "默认文本",
                    icon = Icons.Filled.AccountCircle,
                ) {
                    Log.d(TAG, "outlined输入框: $it")
                }

                FilledEditTextPreference(
                    defaultValue = "默认文本",
                    title = "Filled编辑框",
                    keyName = "edit12",
                    icon = Icons.Filled.AccountCircle,
                    changed = {
                        Log.d(TAG, "filled输入框: $it ")
                    }
                )
                HorizontalDivider()


                PreferenceItemSubTitle(title = "PreferenceItemSubTitle")
                PreferenceSwitch(
                    keyName = "bol2",
                    title = "启用下面全部",
                    dependenceKey = DependenceNode.rootName,//指定依赖为根结点，这样自身就不会受到影响
                    description = "关闭开关以禁用下面内容",
                ) {
                    node.enableStateFlow.tryEmit(it)
                }
                PreferenceCollapseBox(
                    title = "可折叠菜单",
                    description = "preference描述",
                    dependenceKey = customNodeName,
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
                            it.enableStateFlow.tryEmit(state)
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

                PreferenceListMenu(
                    title = "list菜单",
                    keyName = "PreferenceListMenu",
                    description = "list菜单介绍",
                    dependenceKey = customNodeName,
                    list = menu
                ) {
                    Log.d(TAG, "menu item labelKey: $it")
                }
                HorizontalDivider()

                PreferenceItemSubTitle(
                    title = "各种switch",
                    dependenceKey = customNodeName,
                )
                PreferenceSwitchWithContainer(
                    keyName = "bol4",
                    title = "带Container的Switch",
                    description = "描述，描述，描述，描述，描述",
                    dependenceKey = customNodeName,
                    icon = null
                )

                PreferenceSwitch(
                    keyName = "www",
                    title = "夜间模式",
                    dependenceKey = customNodeName,//指定依赖为根结点，这样自身就不会受到影响
                    description = "开关描述",
                ) {
                    Log.d(TAG, "FirstPage: $it")
                    isDarkFlow.tryEmit(it)
                }

                PreferenceSwitchWithDivider(
                    keyName = "bol5",
                    title = "前部可点击switch",
                    dependenceKey = customNodeName,
                    description = "description",
                    icon = Icons.Filled.CenterFocusWeak
                )
                HorizontalDivider()

                PreferenceItemSubTitle(
                    title = "RadioGroup",
                    dependenceKey = customNodeName,
                )
                PreferenceRadioGroup(
                    keyName = "radioGroup",
                    dependenceKey = customNodeName,
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
                    dependenceKey = customNodeName,
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
                HorizontalDivider()

                PreferenceItemSubTitle(
                    title = "PreferenceSlider",
                    dependenceKey = customNodeName,
                )
                PreferenceSlider(
                    keyName = "slider",
                    dependenceKey = customNodeName, //依赖key为customNode的状态
                    min = 0f,
                    max = 10f, steps = 9, defaultValue = 0f, changed = {
                        Log.d(TAG, "slider: $it")
                    }
                )
            }
        }
    }
}




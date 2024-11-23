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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material.icons.filled.ExposurePlus2
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.TipsAndUpdates
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kiylx.compose.preference.component.cross.PreferenceItem
import com.kiylx.compose.preference.component.cross.PreferenceSubTitle
import com.kiylx.compose.preference.component.cross.PreferenceSwitch
import com.kiylx.compose.preference.theme.PreferenceIconStyle
import com.kiylx.compose.preference.theme.PreferenceTheme
import com.kiylx.composepreference.ui.theme.ComposeTestTheme
import kotlinx.coroutines.flow.MutableStateFlow

var isDarkFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
var LocalTheme = compositionLocalOf { false }

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"

    @Composable
    fun TransparentSystemBars() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val systemUiController = rememberSystemUiController()
        val useDarkIcons = !isSystemInDarkTheme()
        SideEffect {
            systemUiController.setSystemBarsColor(
                color = Color.Transparent,
                darkIcons = useDarkIcons,
                isNavigationBarContrastEnforced = false,
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var selected by remember {
                mutableStateOf(0)
            }
            val isD = isDarkFlow.collectAsState()
            MaterialTheme
            CompositionLocalProvider(LocalTheme provides isD.value) {
                ComposeTestTheme(
                    darkTheme = LocalTheme.current
                ) {
                    TransparentSystemBars()
                    Scaffold(bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = selected == 0,
                                onClick = { selected = 0 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.ExposurePlus1,
                                        contentDescription = "one"
                                    )
                                },
                                label = { Text("First") })

                            NavigationBarItem(
                                selected = selected == 1,
                                onClick = { selected = 1 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.ExposurePlus2,
                                        contentDescription = "two"
                                    )
                                },
                                label = { Text("Second") })
                        }
                    }) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                                .padding(it)
                            //同时添加状态栏和导航栏高度对应的上下 padding
                        ) {
                            if (selected == 0) {
                                Column(
                                    modifier = Modifier.verticalScroll(rememberScrollState())
                                ) {
                                    PreferenceTheme.SetTheme(
                                        iconStyle = PreferenceIconStyle(
                                            paddingValues = PaddingValues(8.dp),
                                            tint = MaterialTheme.colorScheme.onPrimary,
                                            backgroundColor = MaterialTheme.colorScheme.primary,
                                        )
                                    ) {
                                        PreferenceItem(
                                            title = "账户",
                                            icon = Icons.Outlined.AccountCircle,
                                            desc = "本地、FreshRSS",
                                        )
                                        PreferenceItem(
                                            title = "颜色和样式",
                                            icon = Icons.Outlined.Palette,
                                            desc = "主题、色调样式、字体大小",
                                        )
                                        PreferenceItem(
                                            title = "交互",
                                            icon = Icons.Outlined.TouchApp,
                                            desc = "初始页面、触感反馈",
                                        )
                                        PreferenceItem(
                                            title = "语言",
                                            desc = "中文(中国)",
                                            icon = Icons.Outlined.Language,
                                        )
                                        PreferenceItem(
                                            title = "故障排除",
                                            icon = Icons.Outlined.BugReport,
                                            desc = "错误报告、调试工具",
                                        )
                                        PreferenceItem(
                                            enabled = false,
                                            title = "提示和支持",
                                            desc = "关于、开源",
                                            icon = Icons.Outlined.TipsAndUpdates,
                                        )
                                        PreferenceSubTitle(
                                            modifier = Modifier.padding(top = 8.dp),
                                            title = "其他"
                                        )
                                        PreferenceItem(
                                            title = "谷歌账户",
                                            icon = Icons.Outlined.BugReport,
                                            desc = "登录谷歌账户，同步设置",
                                        )
                                        var checked by remember { mutableStateOf(false) }
                                        PreferenceSwitch(
                                            isChecked = checked,
                                            title = "同步",
                                            desc = "同步您的账户数据"
                                        ) {
                                            checked = it
                                        }
                                    }
                                }
                                //自动存储偏好值
//                                FirstPage()
                            } else {

                                //仅使用ui界面，不自动存储偏好值
                                //SecondPage()
                            }
                        }
                    }

                }
            }

        }
    }
}
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExposurePlus1
import androidx.compose.material.icons.filled.ExposurePlus2
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
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
                                label = { Text("First") })
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
                                //自动存储偏好值
                                FirstPage()
                            } else {
                                //仅使用ui界面，不自动存储偏好值
                                SecondPage()
                            }
                        }
                    }

                }
            }

        }
    }
}
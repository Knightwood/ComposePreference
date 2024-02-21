package com.kiylx.composepreference.testcompose

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kiylx.composepreference.ui.theme.ComposeTestTheme
import com.kiylx.libx.pref_component.core.PreferenceHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

var isDarkFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
var LocalTheme = compositionLocalOf { false }

class TestNavActivity : AppCompatActivity() {
    val TAG = "TestNavActivity"

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
            val isD = isDarkFlow.collectAsState()
            CompositionLocalProvider(LocalTheme provides isD.value) {
                ComposeTestTheme(
                    darkTheme = LocalTheme.current
                ) {
                    TransparentSystemBars()
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .systemBarsPadding() //同时添加状态栏和导航栏高度对应的上下 padding
                    ) {
                        FirstPage()
                    }
                }
            }

        }
    }
}
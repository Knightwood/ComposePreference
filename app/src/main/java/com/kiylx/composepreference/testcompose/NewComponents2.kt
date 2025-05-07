package com.kiylx.composepreference.testcompose

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStoreFile
import com.kiylx.compose.preference.component.auto.PreferenceCollapseItem
import com.kiylx.compose.preference.component.auto.PreferenceItem
import com.kiylx.compose.preference.component.auto.PreferenceSwitch
import com.kiylx.compose.preference.component.auto.PreferenceSwitchWithContainer
import com.kiylx.compose.preference.component.auto.PreferencesCautionCard
import com.kiylx.compose.preference.component.auto.SetTheme
import com.kiylx.compose.preference.component.cross.PreferenceItem
import com.kiylx.compose.preference.component.cross.PreferencesHintCard
import com.kiylx.compose.preference.theme.PreferenceDimen
import com.kiylx.compose.preference.theme.PreferenceIconStyle
import com.kiylx.compose.preference.theme.Preferences
import com.kiylx.compose.preference.theme.defaultPreferenceBoxStyle
import com.kiylx.composepreference.AppCtx
import com.kiylx.libx.pref_component.core.DependenceNode
import com.kiylx.libx.pref_component.datastore_util.DataStorePreferenceHolder
import com.kiylx.libx.pref_component.datastore_util.getDataStore
import getting
import gettingNullable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Composable
fun NewComponents2(ctx: Context) {
    //1. 使用dataStore存储偏好值
    val holder = remember {
        val ds = getDataStore(ctx, "test.pb")
        DataStorePreferenceHolder.instance(ds)
    }

    //2. 使用mmkv存储偏好值
//        val holder = remember {
//            MMKVPreferenceHolder.instance(MMKV.defaultMMKV())
//        }
    //3. 使用sharedprefrence存储偏好值
//        val holder = remember {
//            OldPreferenceHolder.instance(
//                AppCtx.instance.getSharedPreferences(
//                    "ddd",
//                    Context.MODE_PRIVATE
//                )
//            )
//        }
    val customNodeName = "customNode"
    //创建一个自定义节点
    val node = holder.registerDependence(customNodeName, true)
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Preferences.SetTheme(
            holder = holder,
            iconStyle = PreferenceIconStyle(
                paddingValues = PaddingValues(8.dp),
                tint = MaterialTheme.colorScheme.onPrimary,
                backgroundColor = MaterialTheme.colorScheme.primary,
            )
        ) {

            Column {
                PreferencesHintCard(
                    title = "调整您的设置信息",
                    desc = "账户、翻译、帮助信息等",
                )

                PreferenceSwitch(
                    defaultValue = false,
                    title = "使用新特性",
                    desc = "实验功能，可能不稳定",
                    dependenceKey = DependenceNode.rootName,
                    keyName = "s1"
                ) { state ->
                    //这里获取并修改了当前的enable状态，
                    //依赖这个节点的会改变显示状态，
                    //如果当前没有指定依赖，自身也会受到影响
                    scope.launch {
                        holder.getDependence("s1")?.setEnabled(state)
                    }
                }
                PreferenceItem(
                    dependenceKey = "s1",
                    title = "关联组件",
                    icon = Icons.Outlined.AccountCircle
                )

                PreferenceSwitchWithContainer(
                    title = "调整您的设置信息",
                    desc = "账户、翻译、帮助信息等",
                    defaultValue = false,
                    keyName = "b2",
                    dependenceKey = DependenceNode.rootName,
                    icon = Icons.Outlined.AccountCircle,
                ) {
                    scope.launch {
                        node.setEnabled(it)
                    }
                }
                PreferenceItem(
                    modifier = Modifier,
                    title = "账户",
                    icon = Icons.Outlined.AccountCircle,
                    dependenceKey = customNodeName,
                    desc = "本地、谷歌",
                )
                var expand by remember { mutableStateOf(false) }
                PreferenceCollapseItem(
                    expand = expand,
                    title = "附加内容",
                    dependenceKey = customNodeName,
                    stateChanged = { expand = !expand })
                {
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        PreferenceItem(
                            title = "动画",
                            icon = Icons.Outlined.TouchApp,
                            desc = "动画反馈、触感反馈",
                        )
                        PreferenceItem(
                            title = "语言",
                            desc = "中文(zh)",
                            icon = Icons.Outlined.Language,
                        )
                    }
                }
                PreferencesCautionCard(
                    title = "调整您的设置信息",
                    desc = "账户、翻译、帮助信息等",
                    dependenceKey = customNodeName,
                    icon = Icons.Outlined.AccountCircle,
                )

            }
        }
    }
}

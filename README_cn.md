# compose material design 3 ，Preference界面组件


版本 [![](https://jitpack.io/v/Knightwood/compose-material3-preference.svg)](https://jitpack.io/#Knightwood/compose-material3-preference)

```css
dependencies {
       implementation 'com.github.Knightwood:compose-material3-preference:Tag'
}
```


<img src="README.assets/Screenshot_2023-11-03-20-26-55-611_com.kiylx.composepreference.debug.jpg" style="zoom: 33%;" /> <img src="README.assets/Screenshot_2023-11-03-20-27-00-301_com.kiylx.composepreference.debug.jpg" style="zoom: 33%;" />



![](README.assets/Screenrecorder-2023-11-03-20-20-52-372.gif)

## 支持的存储偏好值的工具

有三种可用的存储偏好值的工具

1. DataStore
2. MMKV
3. SharedPreference

但是注意，sharedpreference不支持存储double，mmkv不支持set<string>类型，他们所支持的有所差异。



## preference的界面组件

要构建一个preference界面，需要使用`PreferencesScope`包裹preference的界面组件（PreferencesScope内部使用了column，因此可以放置任意的compose函数，如果现有的preference组件无法满足需要，可以放置任意compose函数构建界面），且向`PreferencesScope`传入上面支持的三种工具之一（当然你也可以自己继承接口定制额外的存储方式，例如数据库和文件）

示例代码：

```kotlin
//使用PreferencesScope 包裹 preference的compose函数，并且传入存储偏好值的设置工具

//1. 可以使用DataStore存储偏好值
// val holder = DataStorePreferenceHolder.instance(
	dataStoreName = "test",ctx =AppCtx.instance
)
    
2. 可以使用MMKV存储偏好值
// val holder = MMKVPreferenceHolder.instance(MMKV.defaultMMKV())

//3. 使用SharedPreference存储偏好值
val  holder =  OldPreferenceHolder.instance(
     AppCtx.instance.getSharedPreferences("ddd",Context.MODE_PRIVATE)
    )

PreferencesScope(holder=holder) {
    //这里就可以使用一些compose函数构造界面，或者其他的compose函数
    PreferenceItem(title = "PreferenceItem")
    PreferenceItemVariant(title = "PreferenceItemVariant")
    PreferencesHintCard(title = "PreferencesHintCard")
    PreferenceItemLargeTitle(title = "PreferenceItemLargeTitle")
    PreferenceItemSubTitle(text = "PreferenceItemSubTitle")
    PreferencesCautionCard(title = "PreferencesCautionCard")
    PreferenceSwitch(
        keyName = "bol",
        title = "title",
        description = "description"
    )
    //可折叠的preference组件
    CollapsePreferenceItem(
        title = "title",
        description = "description"
    ) {
        PreferenceSwitch(
            keyName = "bol2",
            title = "title",
            description = "description",
            icon = Icons.Filled.CenterFocusWeak
        )

    }
    PreferenceSwitchWithDivider(
        keyName = "bol2",
        title = "title",
        description = "description",
        icon = Icons.Filled.CenterFocusWeak
    )
    PreferenceSwitchWithContainer(
        keyName = "bol2",
        title = "Title ".repeat(2),
        icon = null
    )
    PreferenceRadioGroup(
        keyName = "radioGroup",
        labels = listOf(
            "first",
            "second"
        ),
        left = false,//这样可以把radio放到右边，text放在左边，CheckBox同理
        changed = {
            Log.d(TAG, "radio: ${it}")
        }
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
    PreferenceSlider(
        keyName = "slider", min = 0f,
        max = 10f, steps = 9, value = 0f, changed = {
            Log.d(TAG, "slider: $it")
        }
    )
    //下拉菜单
    PreferenceListMenu(
        title = "PreferenceListMenu",
        keyName = "PreferenceListMenu",
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
            MenuDivider,//分割线
            MenuEntity(
                leadingIcon = Icons.Outlined.Email,
                text = "Send Feedback",
                labelKey = 2
            ),
        )
    )
}


```
## 依赖和置灰

* enable使用: preference组件传入enable为false的同时，指定dependenceKey为DependenceNode.rootName，可以置灰组件，使之无法相应事件。

* 依赖的使用：例如：有三个开关：a,b,c 



当开关a切换为off时，将b和c置灰。

原理：我们使用一个mutablestate保存enable状态，b和c都观察这个状态，当开关a为off时修改这个状态，b和c就会因为观察这个状态而重组，从而达到目的。



每一个preference可组合函数，都会根据自身的keyName生成一个这样的状态，并将状态保存在上面的holder中，所以要达到开关a为off时禁用b和c，有两种方式：

1. 自己注册一个状态节点（例如为node1），然后将b，c的dependenceKey指定为node1的name，然后修改这个node1状态
2. 将b，c的dependenceKey指定为a的keyName，然后获取a的节点状态并进行修改，但是要注意，开关a需要指定dependenceKey为其他，否则a也会受到影响



第一种方式例子：
```kotlin
PreferencesScope(holder = holder) {

    val node = holder.registerDependence("customNode", true)// 1

//PreferenceItem可组合函数
    PreferenceSwitchWithDivider(
        keyName = "bol3",
        title = "title",
        dependenceKey = "customNode", // 2
        description = "description",
        icon = Icons.Filled.CenterFocusWeak
    )
    PreferenceSwitch(
        keyName = "bol2",
        title = "title",
        description = "description",
        icon = Icons.Filled.CenterFocusWeak
    ) {
        node.enableState.value = it //3 修改节点状态
    }
}

```
1. 代码1处创建一个了一个自定义状态节点，enable状态为true，并将节点命名为"customNode"
2. 代码2处表示这个PreferenceItem可组合函数的enable状态依赖于1处创建的名为"customNode"的节点状态
3. 代码3处根据switch修改了"customNode"的enable状态，此时，依赖此node的可组合函数都会收到影响



第二种方式例子

```kotlin
PreferencesScope(holder = holder) {
    //switch A
    PreferenceSwitch(
        keyName = "bol",
        title = "title",
        dependenceKey = DependenceNode.rootName,//指定依赖为根结点，这样自身就不会受到影响
        description = "description"
    ) { state ->
        //这里获取并修改了当前的enable状态，
        //依赖这个节点的会改变显示状态，
        //如果当前没有指定依赖，自身也会受到影响
        holder.getDependence("bol")?.let {
            it.enableState.value = state
        }
    }
    //switch B
    PreferenceSwitch(
        keyName = "bol2",
        title = "title",
        dependenceKey = "bol", //依赖key为bol的状态
        description = "description",
        icon = Icons.Filled.CenterFocusWeak
    )
}
```
这个例子中没有new一个node，却能达到效果。

这是因为preference可组合函数会根据自身的keyName和enable参数（ switch A 传入的keyName为"bol"，enable默认为true），生成一个node保存起来。 可以通过调用holder.getDependence(key name)得到状态节点。

switch B 依赖于switch A 注册的enable状态，当A通过getDependence方法获取到节点状态并做出修改时，
switch B 就会重组从而置灰。

但我们发现，switch A却没有因为修改状态被置灰，这是因为 switch A 把自己的dependence指定为了一个默认的内置状态节点，所以switch A会受到DependenceNode.rootName节点影响
却不会受到自身节点状态的影响。
若希望switch A收到自身节点状态的影响，只需要switch A不指定dependenceKey，保持它为null即可。

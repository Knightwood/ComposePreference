import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.kiylx.libx.pref_component.datastore_util.getKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

class DataStoreDelegate<T>(
    private val scope: CoroutineScope,
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<T>,
    private val defaultValue: T,
) : ReadWriteProperty<Any?, T> {
    private var _value: T = defaultValue

    init {
        scope.launch {
            _value = dataStore.data.map { preferences ->
                preferences[key]
            }.firstOrNull() ?: defaultValue
        }
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return _value // 这里需要根据实际情况获取Flow的值
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        _value = value//同步更改，异步写入datastore
        scope.launch {
            dataStore.edit { preferences ->
                if (value != null) {
                    preferences[key] = value
                } else {
                    preferences.remove(key)
                }
            }
        }
    }
}


class DataStoreProvider<T>(
    private val dataStore: DataStore<Preferences>,
    private val defaultValue: T,
    private val scope: CoroutineScope,
    private val type: KClass<*>
) {
    companion object {
        inline fun <reified T> of(dataStore: DataStore<Preferences>, defaultValue: T, scope: CoroutineScope): DataStoreProvider<T> {
            return DataStoreProvider(dataStore, defaultValue, scope, T::class)
        }

    }

    operator fun provideDelegate(thisRef: Any?, property: kotlin.reflect.KProperty<*>): DataStoreDelegate<T> {
        return DataStoreDelegate(scope, dataStore, dataStore.getKey<T>(property.name, type), defaultValue)
    }
}

/**
 * ```
 * //1. 需要有一个协程作用域
 * val scope = CoroutineScope(Dispatchers.IO)
 * //2. 你可以将属性委托给datastore，变量名就是key的名称
 * var username by dataStore.getting(11, scope)
 *
 * //3. 对数据读写就可以存储到datastore
 * MaterialTheme {
 *     //可以在compose中观察数据变化
 *     val va = dataStore.asDataFlow<Int>("username").collectAsState(initial = 11)
 *     Column {
 *         Button(onClick = {
 *             val randoms = Random.nextInt(0, 11)
 *             //赋值就会将数据写入datastore
 *             username = randoms
 *             //访问变量就可以得到刚刚写入的数据
 *             println(username)
 *         }) {
 *             Text("Random")
 *         }
 *         Text("value:${va.value}")
 *     }
 * }
 *
 * ```
 */
inline fun <reified T> DataStore<Preferences>.getting(defaultValue: T, scope: CoroutineScope) =
    DataStoreProvider.of(this, defaultValue, scope)

inline fun <reified T> DataStore<Preferences>.gettingNullable(scope: CoroutineScope) =
    DataStoreProvider.of<T?>(this, null, scope)

package com.kiylx.libx.pref_component.preference_util.delegate

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * 使用
 *
 * ```
 * class PrefsHelper(prefs: SharedPreferences) {
 *     var isFinish by prefs.boolean("isFinish")
 *     var name by prefs.string("name")
 *     var age by prefs.int("age")
 * }
 * ```
 *
 * 注意，实际使用过程中 PrefsHelper 应该是单例。
 */
class Prefs private constructor(){
    companion object{

    }
}

private inline fun <T> SharedPreferences.delegate(
    key: String? = null,
    defaultValue: T,
    crossinline getter: SharedPreferences.(String, T) -> T,
    crossinline setter: Editor.(String, T) -> Editor
): ReadWriteProperty<Any, T> =
    object : ReadWriteProperty<Any, T> {
        override fun getValue(thisRef: Any, property: KProperty<*>): T =
            this@delegate.getter(key ?: property.name, defaultValue)!!

        override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
            this@delegate.edit().setter(key ?: property.name, value).apply()
        }
    }

fun SharedPreferences.int(key: String? = null, defValue: Int = 0): ReadWriteProperty<Any, Int> {
    return delegate(key, defValue, SharedPreferences::getInt, Editor::putInt)
}

fun SharedPreferences.long(key: String? = null, defValue: Long = 0): ReadWriteProperty<Any, Long> {
    return delegate(key, defValue, SharedPreferences::getLong, Editor::putLong)
}

fun SharedPreferences.float(
    key: String? = null,
    defValue: Float = 0f
): ReadWriteProperty<Any, Float> {
    return delegate(key, defValue, SharedPreferences::getFloat, Editor::putFloat)
}

fun SharedPreferences.boolean(
    key: String? = null,
    defValue: Boolean = false
): ReadWriteProperty<Any, Boolean> {
    return delegate(key, defValue, SharedPreferences::getBoolean, Editor::putBoolean)
}


fun SharedPreferences.stringSet(
    key: String? = null,
    defValue: Set<String> = emptySet()
): ReadWriteProperty<Any, Set<String>> {
    return delegate(key, defValue, { _, _ ->
        this.getStringSet(key, defValue) ?: defValue
    }, Editor::putStringSet)

}

fun SharedPreferences.string(
    key: String? = null,
    defValue: String = ""
): ReadWriteProperty<Any, String> {
    return delegate(key, defValue, { _, _ ->
        this.getString(key, defValue) ?: defValue
    }, Editor::putString)
}


/** 
 * 获取sharedpreference的快捷方法 
 */
public fun getPreference(context: Context, name: String): SharedPreferences {
    return context.getSharedPreferences(name, Context.MODE_PRIVATE)
}

/** 
 * 删除某个偏好值
 */
public fun SharedPreferences.removePreference(name: String) {
    edit().remove(name).apply()
}
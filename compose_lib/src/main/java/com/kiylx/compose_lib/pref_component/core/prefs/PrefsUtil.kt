package com.kiylx.compose_lib.pref_component.core.prefs

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SharedPreferencesUtil<T>(
    val sp: SharedPreferences,
    var key: String,
    var defaultValue: T,
    var getter: SharedPreferences.(String, T) -> T,
    var setter: Editor.(String, T) -> Editor
) {

    fun read(): T {
        return sp.getter(key, defaultValue)
    }

    fun write(data: T) {
        sp.edit().setter(key, data).apply()
    }
}

private fun <T> SharedPreferences.delegate(
    key: String,
    defaultValue: T,
    getter: SharedPreferences.(String, T) -> T,
    setter: Editor.(String, T) -> Editor
): SharedPreferencesUtil<T> = SharedPreferencesUtil(this, key, defaultValue, getter, setter)

fun SharedPreferences.int(
    key: String,
    defValue: Int = 0,

    ): SharedPreferencesUtil<Int> {
    return delegate(key, defValue, SharedPreferences::getInt, Editor::putInt)
}

fun SharedPreferences.long(
    key: String,
    defValue: Long = 0,

    ): SharedPreferencesUtil<Long> {
    return delegate(key, defValue, SharedPreferences::getLong, Editor::putLong)
}

fun SharedPreferences.float(
    key: String,
    defValue: Float = 0f,
): SharedPreferencesUtil<Float> {
    return delegate(key, defValue, SharedPreferences::getFloat, Editor::putFloat)
}

fun SharedPreferences.boolean(
    key: String,
    defValue: Boolean = false,

    ): SharedPreferencesUtil<Boolean> {
    return delegate(key, defValue, SharedPreferences::getBoolean, Editor::putBoolean)
}


fun SharedPreferences.stringSet(
    key: String,
    defValue: Set<String> = emptySet(),
): SharedPreferencesUtil<Set<String>> {
    return delegate(key, defValue, { _, _ ->
        this.getStringSet(key, defValue) ?: defValue
    }, Editor::putStringSet)

}

fun SharedPreferences.string(
    key: String,
    defValue: String = "",

    ): SharedPreferencesUtil<String> {

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
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

package com.kiylx.libx.pref_component.preference_util

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

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

fun SharedPreferences.intRW(
    key: String,
    defValue: Int = 0,

    ): SharedPreferencesUtil<Int> {
    return delegate(key, defValue, SharedPreferences::getInt, Editor::putInt)
}

fun SharedPreferences.longRW(
    key: String,
    defValue: Long = 0,

    ): SharedPreferencesUtil<Long> {
    return delegate(key, defValue, SharedPreferences::getLong, Editor::putLong)
}

fun SharedPreferences.floatRW(
    key: String,
    defValue: Float = 0f,
): SharedPreferencesUtil<Float> {
    return delegate(key, defValue, SharedPreferences::getFloat, Editor::putFloat)
}

fun SharedPreferences.booleanRW(
    key: String,
    defValue: Boolean = false,

    ): SharedPreferencesUtil<Boolean> {
    return delegate(key, defValue, SharedPreferences::getBoolean, Editor::putBoolean)
}


fun SharedPreferences.stringSetRW(
    key: String,
    defValue: Set<String> = emptySet(),
): SharedPreferencesUtil<Set<String>> {
    return delegate(key, defValue, { _, _ ->
        this.getStringSet(key, defValue) ?: defValue
    }, Editor::putStringSet)

}

fun SharedPreferences.stringRW(
    key: String,
    defValue: String = "",

    ): SharedPreferencesUtil<String> {

    return delegate(key, defValue, { _, _ ->
        this.getString(key, defValue) ?: defValue
    }, Editor::putString)

}
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

package com.kiylx.libx.pref_component.datastore_util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.reflect.KClass

/**
 * 批量添加或编辑，参数使用 key to value 生成[Preferences.Pair]
 */
suspend inline fun <reified T> DataStore<Preferences>.putAll(vararg pairs: Preferences.Pair<T>) {
    this.updateData {
        it.toMutablePreferences().putAll(*pairs)
        it
    }
}

inline fun <reified T> DataStore<Preferences>.asDataFlow(
    key: Preferences.Key<T>,
    defaultValue: T
): Flow<T> {
    return this.data.map { preferences ->
        // No type safety.
        preferences[key] ?: defaultValue
    }
}

inline fun <reified T> DataStore<Preferences>.asDataFlow(
    key: String,
): Flow<T?> {
    val key1 = getKey<T?>(key)
    return this.data.map { preferences ->
        // No type safety.
        preferences[key1]
    }
}

fun <T> DataStore<Preferences>.getKey(keyName: String, cls: KClass<*>): Preferences.Key<T> {
    return (when (cls) {
        Int::class -> {
            intPreferencesKey(keyName)
        }

        Boolean::class -> {
            booleanPreferencesKey(keyName)
        }

        String::class -> {
            stringPreferencesKey(keyName)
        }

        Double::class -> {
            doublePreferencesKey(keyName)
        }

        Float::class -> {
            floatPreferencesKey(keyName)
        }

        Long::class -> {
            longPreferencesKey(keyName)
        }

        Set::class -> {
            stringSetPreferencesKey(keyName)
        }

        else -> {
            throw IllegalArgumentException("not support")

        }
    }) as Preferences.Key<T>

}

inline fun <reified T> DataStore<Preferences>.getKey(keyName: String): Preferences.Key<T> {
    return getKey(keyName, T::class)
}

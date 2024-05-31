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
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.kiylx.libx.pref_component.core.IPreferenceEditor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 提供偏好值的读写，datastore实现功能版本
 */
class DataStoreEditor<T>(
    val keyName: String,
    val defaultValue: T,
    val dataStore: DataStore<Preferences>
) : IPreferenceEditor<T> {
    var key: Preferences.Key<T> = (when (defaultValue) {
        is Int -> {
            intPreferencesKey(keyName)
        }

        is Boolean -> {
            booleanPreferencesKey(keyName)
        }

        is String -> {
            stringPreferencesKey(keyName)
        }

        is Double -> {
            doublePreferencesKey(keyName)
        }

        is Float -> {
            floatPreferencesKey(keyName)
        }

        is Long -> {
            longPreferencesKey(keyName)
        }

        is Set<*> -> {
            stringSetPreferencesKey(keyName)
        }

        else -> {
            throw IllegalArgumentException("not support")
        }
    }) as Preferences.Key<T>
    private var data: T = defaultValue
    private val flow: Flow<T> = dataStore.data.map { preferences ->
        // No type safety.
        val tmp = preferences[key] ?: defaultValue
        data = tmp
        tmp
    }

    override fun read(): Flow<T> {
        return flow
    }

    override fun readValue(): T {
        return data
    }

    override suspend fun write(data: T) {
        dataStore.edit {
            it[key] = data
        }
    }

}
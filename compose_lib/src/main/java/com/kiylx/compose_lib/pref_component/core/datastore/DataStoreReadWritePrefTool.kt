package com.kiylx.compose_lib.pref_component.core.datastore

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
import com.kiylx.compose_lib.pref_component.core.IPreferenceReadWrite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 提供偏好值的读写，datastore实现功能版本
 */
class DataStoreReadWritePrefTool<T>(
    val keyName: String,
    val defaultValue: T,
    val dataStore: DataStore<Preferences>
) : IPreferenceReadWrite<T> {

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

    override fun read(): Flow<T> {
        return dataStore.data.map { preferences ->
            // No type safety.
            preferences[key] ?: defaultValue
        }
    }

    override suspend fun write(data: T) {
        dataStore.edit {
            it[key] = data
        }
    }

}
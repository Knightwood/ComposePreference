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

import android.content.Context
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okio.Path.Companion.toPath


fun getDataStore(
    context: Context,
    fileName: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
    migrations: List<DataMigration<Preferences>> = emptyList(),
): DataStore<Preferences> = DataStoreConfig.getDataStore(
    corruptionHandler = corruptionHandler,
    migrations = migrations,
    coroutineScope = coroutineScope,
    path = {
        context.filesDir.resolve(fileName).absolutePath
    }
)


internal object DataStoreConfig {
    private lateinit var dataStore: DataStore<Preferences>

    fun checkFileName(name: String): String {
        return if (!name.endsWith(".preferences_pb")) {
            throw IllegalArgumentException("should be end with \".preferences_pb\"")
        } else {
            name
        }
    }

    /**
     * Gets the singleton DataStore instance, creating it if necessary.
     */
    fun getDataStore(
        corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
        coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        migrations: List<DataMigration<Preferences>> = emptyList(),
        path: () -> String,
    ): DataStore<Preferences> {
        return synchronized(this::class.java) {
            if (DataStoreConfig::dataStore.isInitialized) {
                dataStore
            } else {
                PreferenceDataStoreFactory
                    .createWithPath(
                        corruptionHandler = corruptionHandler,
                        scope = coroutineScope,
                        migrations = migrations,
                        produceFile = {
                            path().toPath()
                        }
                    )
                    .also {
                        dataStore = it
                    }
            }
        }
    }
}

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

package com.kiylx.libx.pref_component.datastore_util;

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.kiylx.libx.pref_component.core.IPreferenceEditor
import com.kiylx.libx.pref_component.core.PreferenceHolder

/**
 * 向界面提供、管理PreferenceProvider
 */
class DataStorePreferenceHolder internal constructor(
    dataStoreName: String,
    private val ctx: Context,
) : PreferenceHolder() {
    private val Context.myDataStore by preferencesDataStore(dataStoreName)
    fun dataStore() = ctx.myDataStore

    override fun <T : Any> getSingleDataEditor(
        keyName: String,
        defaultValue: T,
    ): DataStoreEditor<T> {
        return (hashMap[keyName] as? DataStoreEditor<T>) ?: let {
            val tmp = DataStoreEditor(keyName, defaultValue, dataStore())
            hashMap[keyName] = tmp
            tmp
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        var ps: DataStorePreferenceHolder? = null

        fun instance(
            dataStoreName: String,
            ctx: Context
        ): DataStorePreferenceHolder {
            return ps ?: synchronized(this) {
                ps ?: DataStorePreferenceHolder(dataStoreName, ctx.applicationContext).also {
                    ps = it
                }
            }
        }
    }
}
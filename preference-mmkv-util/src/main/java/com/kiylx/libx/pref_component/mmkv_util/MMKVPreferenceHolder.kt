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

package com.kiylx.libx.pref_component.mmkv_util;

import android.app.Application
import android.content.Context
import com.kiylx.libx.pref_component.core.IPreferenceReadWrite
import com.kiylx.libx.pref_component.core.PreferenceHolder
import com.tencent.mmkv.MMKV

/**
 * 向界面提供、管理PreferenceProvider
 */
class MMKVPreferenceHolder internal constructor(
    private val mmkv: MMKV
) : PreferenceHolder() {

    override fun <T : Any> getReadWriteTool(
        keyName: String,
        defaultValue: T,
    ): IPreferenceReadWrite<T> {
        return hashMap[keyName]?.let {
            it as IPreferenceReadWrite<T>
        } ?: let {
            val tmp = MMKVReadWritePrefTool(mmkv, keyName, defaultValue)
            hashMap[keyName] = tmp
            tmp
        }
    }

    companion object {
        @Volatile
        var ps: MMKVPreferenceHolder? = null
        fun instance(
            mmkv: MMKV
        ): MMKVPreferenceHolder {
            return ps ?: synchronized(this) {
                ps ?: MMKVPreferenceHolder(mmkv).also { ps = it }
            }
        }
    }
}
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

package com.kiylx.libx.pref_component.preference_util;

import android.content.SharedPreferences
import com.kiylx.libx.pref_component.core.IPreferenceEditor
import com.kiylx.libx.pref_component.core.PreferenceHolder

/**
 * 向界面提供、管理PreferenceProvider
 */
class OldPreferenceHolder internal constructor(
    private val sp: SharedPreferences
) : PreferenceHolder() {

    override fun <T : Any> getSingleDataEditor(
        keyName: String,
        defaultValue: T,
    ): IPreferenceEditor<T> {
        return hashMap[keyName]?.let {
            it as IPreferenceEditor<T>
        } ?: let {
            val tmp = SPEditor(sp, keyName, defaultValue)
            hashMap[keyName] = tmp
            tmp
        }
    }

    companion object {
        @Volatile
        var ps: PreferenceHolder? = null
        fun instance(
            sp: SharedPreferences
        ): PreferenceHolder {
            return ps ?: synchronized(this) {
                ps ?: OldPreferenceHolder(sp)
                    .also { ps = it }
            }
        }
    }
}
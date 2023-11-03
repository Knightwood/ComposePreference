package com.kiylx.compose_lib.pref_component.core.mmkv;

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.kiylx.compose_lib.pref_component.core.PreferenceHolder
import com.kiylx.compose_lib.pref_component.core.IPreferenceReadWrite
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
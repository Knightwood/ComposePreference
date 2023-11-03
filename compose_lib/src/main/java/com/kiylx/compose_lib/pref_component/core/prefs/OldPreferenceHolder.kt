package com.kiylx.compose_lib.pref_component.core.prefs;

import android.content.SharedPreferences
import com.kiylx.compose_lib.pref_component.core.PreferenceHolder
import com.kiylx.compose_lib.pref_component.core.IPreferenceReadWrite
import com.tencent.mmkv.MMKV

/**
 * 向界面提供、管理PreferenceProvider
 */
class OldPreferenceHolder internal constructor(
    private val sp: SharedPreferences
) : PreferenceHolder() {

    override fun <T : Any> getReadWriteTool(
        keyName: String,
        defaultValue: T,
    ): IPreferenceReadWrite<T> {
        return hashMap[keyName]?.let {
            it as IPreferenceReadWrite<T>
        } ?: let {
            val tmp = OldReadWritePrefTool(sp, keyName, defaultValue)
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
package com.kiylx.compose_lib.pref_component.core.datastore;

import android.app.Application
import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.kiylx.compose_lib.pref_component.core.PreferenceHolder
import com.kiylx.compose_lib.pref_component.core.datastore.DataStoreReadWritePrefTool
import com.kiylx.compose_lib.pref_component.core.IPreferenceReadWrite

/**
 * 向界面提供、管理PreferenceProvider
 */
class DataStorePreferenceHolder internal constructor(
    dataStoreName: String,
    private val ctx: Application,
) : PreferenceHolder() {
    private val Context.myDataStore by preferencesDataStore(dataStoreName)
    private fun dataStore() = ctx.myDataStore

    override fun <T : Any> getReadWriteTool(
        keyName: String,
        defaultValue: T,
    ): IPreferenceReadWrite<T> {
        return hashMap[keyName]?.let {
            it as IPreferenceReadWrite<T>
        } ?: let {
            val tmp = DataStoreReadWritePrefTool(keyName, defaultValue, dataStore())
            hashMap[keyName] = tmp
            tmp
        }
    }

    companion object {
        @Volatile
        var ps: DataStorePreferenceHolder? = null
        fun instance(
            dataStoreName: String,
            ctx: Application
        ): DataStorePreferenceHolder {
            return ps ?: synchronized(this) {
                ps ?: DataStorePreferenceHolder(dataStoreName, ctx).also { ps = it }
            }
        }
    }
}
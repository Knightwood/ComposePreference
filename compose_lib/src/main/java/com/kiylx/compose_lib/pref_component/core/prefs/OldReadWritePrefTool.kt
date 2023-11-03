package com.kiylx.compose_lib.pref_component.core.prefs

import android.content.SharedPreferences
import com.kiylx.compose_lib.pref_component.core.IPreferenceReadWrite
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 提供偏好值的读写，MMsp实现功能版本
 */
class OldReadWritePrefTool<T : Any>(
    private val sp: SharedPreferences,
    val keyName: String,
    val defaultValue: T,
) : IPreferenceReadWrite<T> {
    val TAG="prefs_tool"

    private val flow: MutableSharedFlow<T> = MutableSharedFlow<T>(1)
    var readWrite:SharedPreferencesUtil<T> =  (when (defaultValue) {
        is Int -> {
            sp.int(keyName, defaultValue)
        }

        is Boolean -> {
            sp.boolean(keyName, defaultValue)
        }

        is String -> {
            sp.string(keyName, defaultValue)
        }

        is Float -> {
            sp.float(keyName, defaultValue)
        }

        is Long -> {
            sp.long(keyName, defaultValue)
        }

        is Set<*> -> {
            sp.stringSet(keyName)
        }

        else -> {
            throw IllegalArgumentException("not support")
        }
    }) as SharedPreferencesUtil<T>

    init {
        flow.tryEmit(readWrite.read())
    }

    override fun read(): Flow<T> {
        return flow
    }

    override suspend fun write(data: T) {
        readWrite.write(data)
        flow.emit(data)
    }
}
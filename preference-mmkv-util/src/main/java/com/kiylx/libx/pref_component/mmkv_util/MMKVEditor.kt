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

package com.kiylx.libx.pref_component.mmkv_util

import com.kiylx.libx.pref_component.core.IPreferenceEditor
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 提供偏好值的读写，MMKV实现功能版本
 */
class MMKVEditor<T : Any>(
    val kv: MMKV,
    val keyName: String,
    val defaultValue: T,
) : IPreferenceEditor<T> {
    val TAG="mmkv_tool"

    private val flow: MutableSharedFlow<T> = MutableSharedFlow<T>(1)
    var readWrite:MmkvUtil<T> =  (when (defaultValue) {
        is Int -> {
            kv.intRW(keyName, defaultValue)
        }

        is Boolean -> {
            kv.boolRW(keyName, defaultValue)
        }

        is String -> {
            kv.strRW(keyName, defaultValue)
        }

        is Double -> {
            kv.doubleRW(keyName, defaultValue)
        }

        is Float -> {
            kv.floatRW(keyName, defaultValue)
        }

        is Long -> {
            kv.longRW(keyName, defaultValue)
        }

        else -> {
            throw IllegalArgumentException("not support")
        }
    }) as MmkvUtil<T>

    init {
        flow.tryEmit(readWrite.read())
    }

    override fun read(): Flow<T> {
        return flow
    }

    override fun readValue(): T {
        return readWrite.read()
    }

    override suspend fun write(data: T) {
        readWrite.write(data)
        flow.emit(data)
    }
}
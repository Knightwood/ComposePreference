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

import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * mmkv的读写工具，非委托版本
 */
class MmkvUtil<T>(
    val mmkv: MMKV,
    var key: String,
    var defaultValue: T,
    var getter: MMKV.(String, T) -> T,
    var setter: MMKV.(String, T) -> Boolean
) {

    fun read(): T {
        return mmkv.getter(key, defaultValue)
    }

    fun write(data: T) {
        mmkv.setter(key, data)
    }
}

fun <T> MMKV.readWriteUtil(
    key: String,
    defaultValue: T,
    getter: MMKV.(String, T) -> T,
    setter: MMKV.(String, T) -> Boolean
): MmkvUtil<T> = MmkvUtil(this, key, defaultValue, getter, setter)

fun MMKV.strRW(
    key: String,
    defValue: String = "",
): MmkvUtil<String> {
    return readWriteUtil<String>(key, defValue, getter = { key1, def ->
        return@readWriteUtil this.decodeString(key1, def) ?: def
    }, MMKV::encode)
}

fun MMKV.intRW(
    key: String,
    defValue: Int = 0,
): MmkvUtil<Int> {
    return readWriteUtil<Int>(key, defValue, MMKV::decodeInt, MMKV::encode)
}

fun MMKV.boolRW(
    key: String,
    defValue: Boolean = false,
): MmkvUtil<Boolean> {
    return readWriteUtil(key, defValue, MMKV::decodeBool, MMKV::encode)
}

fun MMKV.longRW(
    key: String,
    defValue: Long = 0L,
): MmkvUtil<Long> {
    return readWriteUtil(key, defValue, MMKV::decodeLong, MMKV::encode)
}

fun MMKV.floatRW(
    key: String,
    defValue: Float = 0F,
): MmkvUtil<Float> {
    return readWriteUtil(key, defValue, MMKV::decodeFloat, MMKV::encode)
}

fun MMKV.doubleRW(
    key: String,
    defValue: Double = 0.0,
): MmkvUtil<Double> {
    return readWriteUtil(key, defValue, MMKV::decodeDouble, MMKV::encode)
}

fun MMKV.bytesRW(
    key: String,
    defValue: ByteArray = byteArrayOf(),
): MmkvUtil<ByteArray> {
    return readWriteUtil(key, defValue, getter = { key1, def ->
        return@readWriteUtil this.decodeBytes(key1, def) ?: def
    }, MMKV::encode)
}

inline fun <reified T : Parcelable> MMKV.parcelableRW(
    key: String,
    defValue: T,
): MmkvUtil<T> {
    return readWriteUtil(key, defValue, getter = { key1, def ->
        return@readWriteUtil this.decodeParcelable(key1, T::class.java, def) ?: def
    }, MMKV::encode)
}



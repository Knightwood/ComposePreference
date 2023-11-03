package com.kiylx.compose_lib.pref_component.core.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

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

fun MMKV.strM(
    key: String,
    defValue: String = "",
): MmkvUtil<String> {
    return readWriteUtil<String>(key, defValue, getter = { key1, def ->
        return@readWriteUtil this.decodeString(key1, def) ?: def
    }, MMKV::encode)
}

fun MMKV.intM(
    key: String,
    defValue: Int = 0,
): MmkvUtil<Int> {
    return readWriteUtil<Int>(key, defValue, MMKV::decodeInt, MMKV::encode)
}

fun MMKV.boolM(
    key: String,
    defValue: Boolean = false,
): MmkvUtil<Boolean> {
    return readWriteUtil(key, defValue, MMKV::decodeBool, MMKV::encode)
}

fun MMKV.longM(
    key: String,
    defValue: Long = 0L,
): MmkvUtil<Long> {
    return readWriteUtil(key, defValue, MMKV::decodeLong, MMKV::encode)
}

fun MMKV.floatM(
    key: String,
    defValue: Float = 0F,
): MmkvUtil<Float> {
    return readWriteUtil(key, defValue, MMKV::decodeFloat, MMKV::encode)
}

fun MMKV.doubleM(
    key: String,
    defValue: Double = 0.0,
): MmkvUtil<Double> {
    return readWriteUtil(key, defValue, MMKV::decodeDouble, MMKV::encode)
}

fun MMKV.bytesM(
    key: String,
    defValue: ByteArray = byteArrayOf(),
): MmkvUtil<ByteArray> {
    return readWriteUtil(key, defValue, getter = { key1, def ->
        return@readWriteUtil this.decodeBytes(key1, def) ?: def
    }, MMKV::encode)
}

inline fun <reified T : Parcelable> MMKV.parcelableM(
    key: String,
    defValue: T,
): MmkvUtil<T> {
    return readWriteUtil(key, defValue, getter = { key1, def ->
        return@readWriteUtil this.decodeParcelable(key1, T::class.java, def) ?: def
    }, MMKV::encode)
}



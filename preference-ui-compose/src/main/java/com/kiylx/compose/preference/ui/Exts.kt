package com.kiylx.compose.preference.ui

import androidx.compose.ui.Modifier
import kotlin.contracts.ExperimentalContracts

/**
 * 先条件判断，如果为真，则对传入参数调用then并返回调用then的结果。如果为假，直接返回传入参数。
 *  相当于：
 *  ```
 * return if(true) then(this) else this
 * ```
 * @param predicate
 * @param then
 * @return
 */
@OptIn(ExperimentalContracts::class)
@SinceKotlin("1.1")
public inline fun <T> T.takeIf(
    predicate: () -> Boolean,
    then: T.() -> T
): T {
    kotlin.contracts.contract {
        callsInPlace(predicate, kotlin.contracts.InvocationKind.EXACTLY_ONCE)
    }
    return if (predicate()) this.then() else this
}
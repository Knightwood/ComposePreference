package com.kiylx.libx.pref_component.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class PreferenceHolder {
    //记录每个偏好值的key与其对应的偏好值读写工具
    val hashMap: HashMap<String, Any> = hashMapOf()

    //记录每个key对应的enable状态
    val dependenceTree: HashMap<String, DependenceNode> = hashMapOf()

    init {
        //放入默认的公共依赖的根节点
        dependenceTree[DependenceNode.rootName] = DependenceNode(
            enable = true,
            keyName = DependenceNode.rootName
        )
    }

    abstract fun <T : Any> getReadWriteTool(
        keyName: String,
        defaultValue: T,
    ): IPreferenceReadWrite<T>

    /**
     * 将注册者自身(preference compose function)的状态记录下来，并返回注册者依赖的节点状态
     * @param currentKey 注册者自身的key
     * @param currentState 注册者自身的状态
     * @param targetKey 注册者要依赖于哪个节点的key，如果为null，则依赖于根节点状态
     * @return 返回依赖的节点的状态,若targetKey为null，返回自身节点状态
     */
    fun getDependence(
        currentKey: String,
        currentState: Boolean,
        targetKey: String? = null
    ): DependenceNode {
        if (!dependenceTree.contains(currentKey)) {
            val node = DependenceNode(currentState, currentKey)
            dependenceTree.putIfAbsent(currentKey, node)
        }
        return targetKey?.let {
            dependenceTree[it]
        } ?: dependenceTree[currentKey]!!
    }

    /**
     * 注册并返回key的状态
     */
    fun registerDependence(
        key: String,
        state: Boolean,
    ): DependenceNode {
        if (!dependenceTree.contains(key)) {
            val node = DependenceNode(state, key)
            dependenceTree.putIfAbsent(key, node)
        }
        return dependenceTree[key]!!
    }

    /**
     * 获取某个key对应的状态
     */
    fun getDependence(
        key: String
    ): DependenceNode? {
        return dependenceTree[key]
    }

    /**
     * 获取某个key对应的状态,如不存在，返回默认启用状态
     */
    fun getDependenceNotEmpty(
        key: String?,
        enable: Boolean=true,
    ): DependenceNode {
        return dependenceTree[key] ?: DependenceNode(enable, "")
    }


}

class DependenceNode(
    enable: Boolean,
    val keyName: String,
) {
    val enableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(enable)

//    val enableState = mutableStateOf(enable)

    companion object {
        const val rootName = "Pref_Dependence_Node_Root"
    }
}
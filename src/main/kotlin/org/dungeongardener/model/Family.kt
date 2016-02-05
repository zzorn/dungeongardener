package org.dungeongardener.model

import java.util.*

/**
 * Used to express something that exists in a family hierarchy
 */
open class Family<T>(val name: String,
                     val parent: Family<T>? = null) {

    private var _types: MutableList<T> = ArrayList()

    private var _childFamilies: MutableList<Family<T>> = ArrayList()

    fun addType(type: T) {
        _types.add(type)
    }

    init {
        if (parent != null) parent._childFamilies.add(this)
    }

    val childFamilies: List<Family<T>>
        get() = _childFamilies

    val types: List<T>
        get() = _types

}
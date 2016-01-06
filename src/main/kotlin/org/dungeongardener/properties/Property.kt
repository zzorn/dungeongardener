package org.dungeongardener.properties

import org.flowutils.Symbol

/**
 *
 */
interface Property<T> {

    val name: Symbol
    val desc: String
    val type: Class<T>

    /**
     * In case of primitive types, and if a default value is available, the
     * value to use by default for new instances.  If null, no default value is specified.
     */
    val defaultValue: T?

    fun getValue(host: Object): T
    fun setValue(host: Object, value: T)

    val range: Range
    val hidden: Boolean
}
package org.dungeongardener.properties

import org.flowutils.Symbol
import kotlin.reflect.KType

/**
 *
 */
interface Property<T> {

    val name: Symbol
    val desc: String?
    val type: KType
    val javaType: Class<T>

    /**
     * In case of primitive types, and if a default value is available, the
     * value to use by default for new instances.  If null, no default value is specified.
     */
    val defaultValue: T?

    fun getValue(host: Object): T
    fun setValue(host: Object, value: T)

    val range: Range?
    val hidden: Boolean
}
package org.dungeongardener.properties

import org.flowutils.Symbol

/**
 *
 */
interface DynamicPropertied : Propertied {

    fun addProperty(property: Property<*>)
    fun removeProperty(name: Symbol)

    fun get(name: Symbol): Any?
    fun set(name: Symbol, value: Any?)

    fun get(name: String): Any? = get(Symbol.get(name))
    fun set(name: String, value: Any?) = set(Symbol.get(name), value)

    // TODO: Store dynamic properties, and also return any fixed var-based properties in getProperties.
}
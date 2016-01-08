package org.dungeongardener.properties

import org.flowutils.Symbol

/**
 * Something that provides its own properties.
 */
interface Propertied {

    fun getProperties(): List<Property<*>>

    fun getPropery(name: Symbol): Property<*>?
    fun getPropery(name: String): Property<*>? = getPropery(Symbol.get(name))

}

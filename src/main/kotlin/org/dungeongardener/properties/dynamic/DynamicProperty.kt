package org.dungeongardener.properties.dynamic

import org.dungeongardener.properties.Property
import org.dungeongardener.properties.Range
import org.flowutils.Symbol

/**
 * Dynamically created property.
 */
class DynamicProperty<T : Any>(
        override val name: Symbol,
        override val javaType: Class<T>,
        override val desc: String? = null,
        override val defaultValue: T? = null,
        override val range: Range? = null,
        override val hidden: Boolean = false) : Property<T> {

    override fun getValue(host: Any): T {
        return (host as DynamicPropertied).get(name) as T
    }

    override fun setValue(host: Any, value: T) {
        (host as DynamicPropertied).set(name, value)
    }

    override fun toString(): String {
        return "$name: ${javaType.toString()} = $defaultValue // $desc"
    }
}
package org.dungeongardener.properties.dynamic

import org.dungeongardener.properties.Range
import org.flowutils.Symbol
import java.util.*

/**
 * Class with dynamic properties.
 */
open class DynamicPropertied {

    private val properties = LinkedHashMap<Symbol, DynamicProperty<Any>>()
    private val propertyValues = HashMap<Symbol, Any?>()

    fun <T : Any>addProperty(name: String, type: Class<T>, initialValue: T? = null, desc: String? = null, range: Range? = null): DynamicProperty<T> {
        return addProperty(DynamicProperty<T>(Symbol.get(name), type, desc, initialValue, range))
    }

    fun <T : Any>addProperty(property: DynamicProperty<T>): DynamicProperty<T> {
        if (properties.containsKey(property.name)) throw IllegalArgumentException("A property with the name ${property.name} already exists")

        properties.put(property.name, property as DynamicProperty<Any>)
        propertyValues.put(property.name, property.defaultValue)

        return property
    }

    fun removeProperty(name: Symbol) {
        properties.remove(name)
        propertyValues.remove(name)
    }

    operator fun get(name: Symbol): Any? = propertyValues.get(name)
    operator fun set(name: Symbol, value: Any?) = propertyValues.set(name, value)

    operator fun get(name: String): Any? = get(Symbol.get(name))
    operator fun set(name: String, value: Any?) = set(Symbol.get(name), value)

    fun getProperty(name: Symbol): DynamicProperty<Any>? = properties.get(name)
    fun getProperty(name: String): DynamicProperty<Any>? = properties.get(Symbol.get(name))

    fun getDynamicProperties(): Collection<DynamicProperty<Any>> = properties.values

}
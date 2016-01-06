package org.dungeongardener.properties

/**
 * Factory for creating object instances.
 * Exposes properties for each property of the object, except the properties
 * are function types?
 * TODO: Or something that is easier to serialize, a limited scripting language for
 * property value calculation (arithmetics, references), object construction)
 */
interface Factory<T> : Propertied { // TODO: Should this extend propertied, or just have a map from property (id) to a parsed function to calculate it?

    /**
     * Unique ID for this factory, could e.g. contain a path.
     * TODO: Figure out how to use.  If we need to serialize a factory, we need to be able to
     * refer to factories it uses by some id
     */
    val id: String

    /**
     * The type of objects created by this factory.
     */
    val type: Class<T>

    /**
     * Creates new instance, using the current property values to calculate the instance property values.
     */
    fun createInstance(): T

}
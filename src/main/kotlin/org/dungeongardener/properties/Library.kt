package org.dungeongardener.properties

/**
 * Keeps track of a number of factories
 */
interface Library {

    /**
     * All registered factories for the specified type.
     */
    fun <T> getFactories(type: Class<T>): List<Factory<T>>

    /**
     * Adds a factory based on creating an instance of the class using a no arg constructor,
     * and then exposing the properties in the class as factory properties.
     */
    fun addPropertyFactory(type: Class<*>)

    /**
     * Adds a factory based on creating an instance of the class using the class constructor,
     * and then exposing the constructor parameters as factory properties.
     * This can be used to create a factory for immutable classes.
     */
    fun addConstructorFactory(type: Class<*>)

    fun addFactory(factory: Factory<*>)
    fun removeFactory(factory: Factory<*>)

}
package org.dungeongardener.properties

import java.util.*
import kotlin.reflect.KMutableProperty



/**
 * Marks a property as not visible in editors and the like
 */
@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class Hidden

/**
 * User readable description for the property.
 */
@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class Desc(val description: String)

/**
 * Minimum and maximum value for a property, to use for UI sliders and such.
 * @param min minimum recommended value to enter
 * @param max maximum recommended value to enter
 * @param center center position for the slider.
 * @param logarithmic Indicates that the range for this property should use a logarithmic scale or slider
 */
@Target(AnnotationTarget.PROPERTY)
@MustBeDocumented
annotation class Range(val min: Double = 0.0, val max: Double = 1.0, val center: Double = 0.0, val logarithmic: Boolean = false)
// TODO: Create RangedNum delegate that uses a Range annotation or passed in min/max to clamp a numerical value to the range.

private val propertiesForClasses = object : ThreadLocal<MutableMap<Class<*>, MutableList<Property<*>>>>() {
    override fun initialValue(): MutableMap<Class<*>, MutableList<Property<*>>> {
        return HashMap()
    }
}

public fun Any.getProperties(): List<Property<*>> {

    val javaClass = this.javaClass

    // Get property list
    var properties = propertiesForClasses.get()[javaClass]

    if (properties == null) {
        // Create new list
        properties = ArrayList()
        propertiesForClasses.get()[javaClass] = properties

        // Fill in properties
        for (member in javaClass.kotlin.members) {
            if (member is KMutableProperty && member.annotations.none { it is Hidden}) {
                properties.add(KotlinProperty(member))
            }
        }
    }

    // Return list of non hidden properties of the class
    return properties
}


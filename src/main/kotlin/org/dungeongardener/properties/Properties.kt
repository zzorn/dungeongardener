package org.dungeongardener.properties

import org.dungeongardener.properties.dynamic.DynamicPropertied
import org.dungeongardener.properties.poko.KotlinProperty
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

private val propertiesForClasses = object : ThreadLocal<MutableMap<Class<Any>, MutableList<Property<Any>>>>() {
    override fun initialValue(): MutableMap<Class<Any>, MutableList<Property<Any>>> {
        return HashMap()
    }
}

public fun Any.getProperties(): List<Property<Any>> {

    val javaClass = this.javaClass

    // Get property list
    var properties: MutableList<Property<Any>>? = propertiesForClasses.get()[javaClass]

    if (properties == null) {
        // Create new list
        properties = ArrayList<Property<Any>>()
        val props: MutableList<Property<Any>> = properties
        propertiesForClasses.get()[javaClass] = props

        // Fill in properties
        for (member in javaClass.kotlin.members) {
            if (member is KMutableProperty && member.annotations.none { it is Hidden}) {
                props.add(KotlinProperty(member as KMutableProperty<Any>))
            }
        }

        // Sort in alphabetical order by name
        props.sortBy { it.name.toString() }
    }

    if (this is DynamicPropertied) {
        // Add dynamic properties provided by the class
        properties = ArrayList(properties)
        properties.addAll(this.getDynamicProperties())
    }

    // Return list of non hidden properties of the class
    return properties
}


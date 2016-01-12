package org.dungeongardener

import org.dungeongardener.properties.Desc
import org.dungeongardener.properties.Range
import org.dungeongardener.properties.getProperties
import org.junit.Test

/**
 *
 */
class TestKotlinProps {

    data class TestClass(@Desc("The testfoo prop, super awesome!") var foo: String="testfoo",
                         @Range(-1.0, 1.0) var bar: Int = 42)

    @Test
    fun testTestProps() {
        val t = TestClass()
        val props = t.getProperties()
        props.forEach { println ("* " + it) }
        println("Current value: " + props.first().getValue(t))
        // TODO: props.first().setValue(t, "2")
    }
}
package org.dungeongardener

import org.dungeongardener.properties.Desc
import org.dungeongardener.properties.Range
import org.dungeongardener.properties.getProperties
import org.junit.Assert
import org.junit.Test

/**
 *
 */
class TestKotlinProps {

    data class TestClass(@Desc("The testfoo prop, super awesome!") var foo: String="testfoo",
                         @Range(-1.0, 1.0) var bar: Int = 42,
                         @Desc("zipzap") var zap: Boolean = false)

    @Test
    fun testTestProps() {
        val t = TestClass()
        val props = t.getProperties()
        props.forEach { println ("* " + it) }
        val p = props.first()
        Assert.assertEquals(42, p.getValue(t))
        p.setValue(t, 21)
        Assert.assertEquals(21, p.getValue(t))
    }
}

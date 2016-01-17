package org.dungeongardener

import org.dungeongardener.properties.Desc
import org.dungeongardener.properties.Range
import org.dungeongardener.properties.dynamic.DynamicPropertied
import org.dungeongardener.properties.getProperties
import org.flowutils.Symbol
import org.junit.Assert
import org.junit.Test

/**
 *
 */
class TestKotlinProps {

    data class TestClass(@Desc("The testfoo prop, super awesome!") var foo: String="testfoo",
                         @Range(-1.0, 1.0) var bar: Int = 42,
                         @Desc("zipzap") var zap: Boolean = false)

    class TestDynClass(@Desc("Foodoo") var foodoo: String = "foo") : DynamicPropertied()

    @Test
    fun testTestProps() {
        val t = TestClass()
        val props = t.getProperties()
        //props.forEach { println ("* " + it) }
        val p = props.first()
        Assert.assertEquals(42, p.getValue(t))
        p.setValue(t, 21)
        Assert.assertEquals(21, p.getValue(t))
    }

    @Test
    fun testDynamicProps() {
        val t = TestDynClass()

        t.addProperty("FoFoFo", Int::class.java, 5)
        t.addProperty("Barabar", String::class.java, "asdddf")

        Assert.assertEquals(5, t.get("FoFoFo"))
        Assert.assertEquals("foo", t.foodoo)

        t["Barabar"] = "asd"
        t[Symbol.get("FoFoFo")] = 7

        Assert.assertEquals("asd", t["Barabar"])
        Assert.assertEquals(7, t.get("FoFoFo"))
    }
}

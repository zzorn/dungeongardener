package org.dungeongardener

import org.dungeongardener.util.SimpleContext
import org.dungeongardener.util.genlang.GenLang
import org.dungeongardener.util.genlang.nodes.Expression
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 *
 */
class NumExprTest {

    @Test
    fun testNumExpr() {

        check(0.0, "0")
        check(1.0, " 1")
        check(3.0, " 3 ")
        check(13.0, "13 ")
        check(-113.0, "-113")
        check(13.5, "13.5")
        check(-2314.23, "-2314.23")
        check(3.0, "1+2")
        check(3.0, "1 + 2")
        check(10.0, "1 + 2 + 7")
        check(7.0, "2 *3 + 1")
        check(9.0, "2 * (3 + 1) #casablanca\n+1")
        check(-28.1, "2*-(9+2.5*2)---0.1")
        check(15.0, "3 + foo * 6")
        check(15.0, "max(7, 7+7)+1")

        val lang = GenLang()
        val context = SimpleContext()
        for (i in 1..10)
            println("Dice test 3d6: " + lang.expression.parseFirst<Expression>(" 3D6").evaluate(context))
        for (i in 1..10)
            println("Gauss test: " + lang.expression.parseFirst<Expression>("clamp(gauss(0, 2), 0, 5) + 1000").evaluate(context))
    }

    private fun check(expected: Double, expression: String) {
        val lang = GenLang()
        val context = SimpleContext(mapOf("foo" to 2.0))
        assertEquals(expected, lang.expression.parseFirst<Expression>(expression, debugOutput = false).evaluate(context), 0.00001)
    }


}
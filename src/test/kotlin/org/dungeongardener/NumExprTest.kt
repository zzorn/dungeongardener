package org.dungeongardener

import org.dungeongardener.util.numberexpr.NumExprLanguage
import org.dungeongardener.util.numberexpr.SimpleNumContext
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 *
 */
class NumExprTest {

    @Test
    fun testNumExpr() {

        check(0.0, "0")
        check(1.0, "1")
        check(3.0, "3")
        check(13.0, "13")
        check(-113.0, "-113")
        check(13.5, "13.5")
        check(2314.23, "2314.23")
        check(3.0, "1 + 2")
        check(10.0, "1 + 2 + 7")
        check(7.0, "2 *3 + 1")
    }

    private fun check(expected: Double, expression: String) {
        val lang = NumExprLanguage()
        val context = SimpleNumContext()
        assertEquals(expected, lang.parseFirst(expression, debugOutput = true).evaluate(context), 0.00001)
    }


}
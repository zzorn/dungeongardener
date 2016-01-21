package org.dungeongardener

import org.dungeongardener.util.randomnumber.NumExprParser
import org.junit.Test

/**
 *
 */
class TestNumberExpr {


    @Test
    fun testParse() {
        val parser = NumExprParser()
        val context = parser.parseFileContent(
        """
            foo = 5
            bar = foo + 3
            zap = 1D4
            gop = 3d6 + 5 * random(0, 1) + zap + bar
        """)

        println(context.get("gop"))
        println(context.evaluate("gop"))

    }

}
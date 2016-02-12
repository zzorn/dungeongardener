package org.dungeongardener

import org.dungeongardener.util.genlang.GenLang
import org.dungeongardener.util.genlang.nodes.ImportContext
import org.junit.Assert
import org.junit.Test
import java.io.File

/**
 *
 */
class TestImports {

    @Test
    fun testImports() {
        val importContext = ImportContext(File("src/test/res"))
        importContext.registerLanguage(GenLang())

        val context = importContext.createContext("testimport.genlang")

        Assert.assertEquals("foo", context.evaluateExpression("bar", context))
        Assert.assertEquals(42, context.evaluateExpression("theAnswer", context))
    }

}
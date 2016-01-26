package org.dungeongardener

import org.dungeongardener.util.parser.parsers.Sequence
import org.dungeongardener.util.parser.parsers.StringParser
import org.junit.Test

/**
 *
 */
class TestParser {

    @Test
    fun testParser() {
        val parser = Sequence(StringParser("foo"), StringParser("bar"))
        parser.parse("fooba")
    }

}
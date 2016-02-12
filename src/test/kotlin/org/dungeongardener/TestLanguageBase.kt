package org.dungeongardener

import org.dungeongardener.util.parser.FunctionRegistry
import org.dungeongardener.util.parser.LanguageBase
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.result.ParseSuccess
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Math.*

/**
 * Tests for functionality provided by LanguageBase
 */
class TestLanguageBase {

    class TestLanguageBase(): LanguageBase<Any>("test") {

        init {
            addBlockComment("/**", "**/")
            addBlockComment("COMMENT_COMMENCE", "COMMENT_TERMINATUS", true)
            addEndOfLineComment("IGNORE THE REST OF THIS LINE", true)
            addEndOfLineComment("#")
        }

        val ab = zeroOrMore(ws, oneOrMore(any(+"a", +"b")).generatesMatchedText())
        val l1 = listParser(oneOrMore("a").generatesMatchedText())
        val l2 = listParserWithoutStartEndMarkers(oneOrMore("a").generatesMatchedText(), elementSeparator = oneOrMore("b"))
        val m1 = mapParser(oneOrMore(any("a", "b")).generatesMatchedText(), double)
        val m2 = mapParserWithoutStartEndMarkers(oneOrMore(any("a", "b")).generatesMatchedText(), double)
        val m3 = mapParserWithoutStartEndMarkers(oneOrMore(any("a", "b")).generatesMatchedText(), double, elementSeparator = autoMatch, keyValueSeparator = autoMatch)

        val stringParser: Parser = ws + quotedString
        override val parser: Parser = +"foo"
        override val fileParser: Parser = +"bar"
    }

    val lang = TestLanguageBase()

    @Test
    fun testComments() {
        assertEquals(listOf("a", "ba", "abba"), (lang.ab.parse("a ba abba") as ParseSuccess).results)
        assertEquals(listOf("a", "ba", "abba"), (lang.ab.parse("a ba #baaabaa\nabba") as ParseSuccess).results)
        assertEquals(listOf("a", "ba", "abba"), (lang.ab.parse("a ba /** babba **/ abba") as ParseSuccess).results)
        assertEquals(listOf("a", "ba", "abba"), (lang.ab.parse("a baCOMMENT_COMMENCE babba COMMENT_TERMINATUSabba") as ParseSuccess).results)
        assertEquals(listOf("a", "ba", "abba"), (lang.ab.parse("a baIGNORE THE REST OF THIS LINE aaaaaaa\n  abbaIGNORE THE REST OF THIS LINE") as ParseSuccess).results)
    }

    @Test
    fun testPrimitives() {
        val sInpt = "\"fo\no \\n bar \\t\\tfoo\\\"\\\\\\\"\""
        val sExp = "fo\no \n bar \t\tfoo\"\\\""
        assertEquals(sExp, lang.quotedString.parseFirst<Any>(sInpt))

        assertEquals(0, lang.positiveInteger.parseFirst<Any>("0"))
        assertEquals(1, lang.positiveInteger.parseFirst<Any>("1"))
        assertEquals(1234, lang.positiveInteger.parseFirst<Any>("1234"))

        assertEquals(125, lang.integer.parseFirst<Any>("125"))
        assertEquals(-125, lang.integer.parseFirst<Any>("-125"))
        assertEquals(-125, lang.integer.parseFirst<Any>("- 125"))

        assertEquals(-125.0, lang.double.parseFirst<Any>("-125") as Double, 0.000001)
        assertEquals(-125.32, lang.double.parseFirst<Any>("- 125.32") as Double, 0.000001)
        assertEquals(-4e-23, lang.double.parseFirst<Any>("-4e-23") as Double, 0.000001)
        assertEquals(-4e-23, lang.double.parseFirst<Any>("-4E-23") as Double, 0.000001)
        assertEquals(-4.4e-23, lang.double.parseFirst<Any>("- 4.4E-23") as Double, 0.000001)

        assertEquals("F", lang.identifier.parseFirst<String>("F"))
        assertEquals("Foo", lang.identifier.parseFirst<String>("Foo"))
        assertEquals("Foo0", lang.identifier.parseFirst<String>("Foo0"))
        assertEquals("_", lang.identifier.parseFirst<String>("_"))
        assertEquals("__bar", lang.identifier.parseFirst<String>("__bar"))
        assertEquals("Arguhan_3b2", lang.identifier.parseFirst<String>("Arguhan_3b2"))

        assertEquals("foobar", lang.stringParser.parseFirst<String>("   \"foobar\"  "))
    }

    @Test
    fun testLists() {
        val expected = listOf("a", "aa", "aaa", "aaaa", "aaaaa", "a")
        assertEquals(expected, lang.l1.parseFirst<Any>("( a, aa,  aaa,aaaa,aaaaa \n ,a  \n\n)"))
        assertEquals(expected, lang.l2.parseFirst<Any>("ab aabbbbbaaabaaaabaaaaa \n b  a  \n\n", debugOutput = false))
    }

    @Test
    fun testMaps() {
        val expectedMap = mapOf("a" to 1.0, "b" to 2.0, "ab" to 1.5, "ba" to -1.0, "abba" to 0.1e-4, "babba" to -100E100)
        assertEquals(expectedMap, lang.m1.parseFirst<Any>("{ a: 1.0, b: 2.0 , ab  : \n 1.5 , ba : - 1 ,abba:0.1e-4,babba:-100E100}"))
        assertEquals(expectedMap, lang.m2.parseFirst<Any>("a: 1.0, b: 2.0 , ab  : \n 1.5 , ba : - 1 ,abba:0.1e-4,babba:-100E100  "))
        assertEquals(expectedMap, lang.m3.parseFirst<Any>("a 1.0 b 2.0ab1.5 ba \n-\n 1 abba 0.1e-4babba-100E100"))
    }

    @Test
    fun testOperators() {
        val numberParser = lang.integer + lang.ws
        val mathParser = lang.operatorParser<Int>(numberParser,
                "*" to {a, b -> a*b},
                "/" to {a, b -> a/b},
                "-" to {a, b -> a-b},
                "+" to {a, b -> a+b}
                )

        assertEquals(1 + 3 * 4 + 4/2-2+3+3*4*4*8, mathParser.parseFirst<Int>("1 + 3 * 4 + 4/2-2+3+3*4*4*8"))
    }

    @Test
    fun testFunctions1() {

        val functions = FunctionRegistry()
        functions.addDirectFun<Double>("sqrt", 1) { sqrt(it[0]) }
        functions.addDirectFun<Double>("abs", 1) { abs(it[0]) }

        val funcParser = lang.functionParser(functions, lang.double + lang.ws)

        assertEquals(sqrt(9.0), funcParser.parseFirst<Double>("sqrt ( 9.0 )"), 0.00001)
        assertEquals(abs(-9.0), funcParser.parseFirst<Double>("abs(-9)"), 0.00001)

        functions.addDirectFun<Double>("sin", 1, { sin(it[0]) })
        assertEquals(sin(1234.0), funcParser.parseFirst<Double>("sin (1234.0)"), 0.00001)
    }

    @Test
    fun testFunctions2() {
        val functions = FunctionRegistry()
        functions.addDirectFun<Double>("sum", 2) { it[0] + it[1] }
        functions.addDirectFun<Double>("mul", 2) { it[0] * it[1] }

        val funcParser = lang.functionParser(functions, lang.double + lang.ws)

        assertEquals(4.0 + 9.0, funcParser.parseFirst<Double>("sum(4,9)"), 0.00001)
        assertEquals(3.0 * 5.0, funcParser.parseFirst<Double>("mul  (  3.0e0  , 5  )"), 0.00001)

        functions.addDirectFun<Double>("div", 2) { it[0] / it[1] }
        assertEquals(4.0 / 3.0, funcParser.parseFirst<Double>("div(4, 3)"), 0.00001)
    }

}
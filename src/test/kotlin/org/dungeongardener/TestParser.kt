package org.dungeongardener

import org.dungeongardener.util.parser.Multiplicity.ZERO_OR_MORE
import org.dungeongardener.util.parser.Parser
import org.dungeongardener.util.parser.parsers.*
import org.dungeongardener.util.parser.result.ParseSuccess
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 *
 */
class TestParser {

    @Test
    fun testStringParser() {
        val parser = StringParser("foo")

        checkParsing(parser, true, "foo")
        checkParsing(parser, true, "foobar")
        checkParsing(parser, false, "fo")
        checkParsing(parser, false, "abc")
        checkParsing(parser, false, "")
        checkParsing(parser, false, " foo")
    }

    @Test
    fun testSequence() {
        val parser = Sequence(StringParser("foo"), StringParser("bar"))

        checkParsing(parser, true, "foobar")
        checkParsing(parser, true, "foobar ")
        checkParsing(parser, true, "foobarbar")
        checkParsing(parser, false, "fooba")
    }

    @Test
    fun testNestedSequence() {
        val parser = Sequence("foo".parser, Sequence("zap".parser, Optional("zip".parser)), "bar".parser)

        checkParsing(parser, true, "foozapzipbar")
        checkParsing(parser, true, "foozapbar")
        checkParsing(parser, false, "foozapzip")
        checkParsing(parser, false, "foozapzipba")
        checkParsing(parser, false, "foobar")
    }

    @Test
    fun testChoice() {
        val parser = AnyOf(StringParser("foo"), StringParser("bar"), "zip".parser)

        checkParsing(parser, true, "foo")
        checkParsing(parser, true, "bar ")
        checkParsing(parser, true, "zip")
        checkParsing(parser, false, "zurg")
        checkParsing(parser, false, "zurgfoo")
        checkParsing(parser, false, " ")
    }

    @Test
    fun testEndOfInput() {
        val parser = Sequence(StringParser("foo"), StringParser("bar"), EndOfInput())

        checkParsing(parser, true, "foobar")
        checkParsing(parser, false, "foobar ")
        checkParsing(parser, false, "bar")
        checkParsing(parser, false, "foo")
        checkParsing(parser, false, "fooba")
        checkParsing(parser, false, "foobarbar")
    }

    @Test
    fun testNot() {
        val parser = Sequence(AnyOf("foo", "bar"), Not("zap"), Optional("bup"), EndOfInput())

        checkParsing(parser, true, "foobup")
        checkParsing(parser, true, "foo")
        checkParsing(parser, true, "foobup")
        checkParsing(parser, true, "bar")
        checkParsing(parser, false, "foozap")
        checkParsing(parser, false, "barzap")
        checkParsing(parser, false, "barzapbup")
        checkParsing(parser, false, "asdasd")
    }

    @Test
    fun testAnd() {
        val parser = Sequence(AnyOf("foo", "bar"), And("zap"), Optional("bup"))

        checkParsing(parser, false, "foo")
        checkParsing(parser, false, "foobup")
        checkParsing(parser, false, "barz")
        checkParsing(parser, false, "barbup")
        checkParsing(parser, true, "foozap")
        checkParsing(parser, true, "barzap")
        checkParsing(parser, true, "barzapbup")
        checkParsing(parser, true, "barzapdsfsdf")
        checkParsing(parser, false, "asdasd")
    }

    @Test
    fun testIgnoreCase() {
        val parser = Sequence(OneOrMore(AnyOf(StringParser("foo", true), StringParser("BAR", false))), EndOfInput())

        checkParsing(parser, true, "foo")
        checkParsing(parser, true, "fooBAR")
        checkParsing(parser, true, "FooBARBAR")
        checkParsing(parser, true, "FOOBARBAR")
        checkParsing(parser, true, "FooBAR")
        checkParsing(parser, true, "BARFoOBARBAR")
        checkParsing(parser, true, "BAR")
        checkParsing(parser, false, "bar")
        checkParsing(parser, false, "foobar")
        checkParsing(parser, false, "FOOBARBAr")
        checkParsing(parser, false, "asdasd")
    }

    @Test
    fun testFooBarFooBar() {
        val fooOrBar = AnyOf(StringParser("foo") generates {1}, StringParser("bar") generates {2})
        val parser = Sequence(fooOrBar, fooOrBar, EndOfInput())

        checkResult(parser, "foobar", 1, 2)
        checkResult(parser, "barbar", 2, 2)
        checkResult(parser, "barfoo", 2, 1)
        checkResult(parser, "foofoo", 1, 1)
        checkParsing(parser, false, "foobar ")
        checkParsing(parser, false, "bar")
        checkParsing(parser, false, "foo")
        checkParsing(parser, false, "fooba")
        checkParsing(parser, false, "foobarbar")
    }

    @Test
    fun testValueGeneration() {
        val parser = "foo".parser generates {it.text }

        checkResult(parser, "foo", "foo")
        checkResult(parser, "foobar", "foo")
        checkParsing(parser, false, "fo")
    }

    @Test
    fun testValueGeneration2() {
        val parser = Sequence("foo".parser, "bar".parser) generates { println("testtext: '${it.text}' "); it.text }

        checkResult(parser, "foobarbar", "foobar")
        checkParsing(parser, false, "foo")
    }

    @Test
    fun testValueGeneration3() {
        val parser = OneOrMore(AnyOf("foo".parser, "bar".parser)) generates {it.text }

        checkResultWithSameReturn(parser, "foo")
        checkResultWithSameReturn(parser, "bar")
        checkResultWithSameReturn(parser, "foobar")
        checkResultWithSameReturn(parser, "foofoo")
        checkResultWithSameReturn(parser, "barbarbar")
        checkResultWithSameReturn(parser, "foobarfoobar")
        checkParsing(parser, false, "")
        checkParsing(parser, false, "ofo")
    }

    @Test
    fun testValueGeneration4() {
        val p1 = OneOrMore(AnyOf("foo".parser, "bar".parser))
        val p2 = OneOrMore(Sequence(p1, Optional("zip".parser))) generates {it.text }

        checkResultWithSameReturn(p2, "foozipfoo")
        checkResultWithSameReturn(p2, "foobarzipfoo")
        checkResultWithSameReturn(p2, "foobarzipfoozipbarzip")
        checkResultWithSameReturn(p2, "barzipfoozipbarfoozip")
        checkResult(p2, "foobarzipfoo", "foobarzipfoo")
        checkParsing(p2, false, "zip")
        checkParsing(p2, false, "zipbar")
    }

    @Test
    fun testValueGeneration5() {
        val parser = ZeroOrMore(AnyOf("foo".parser, "bar".parser)) generates {it.text }

        checkResultWithSameReturn(parser, "")
        checkResultWithSameReturn(parser, "foo")
        checkResultWithSameReturn(parser, "bar")
        checkResultWithSameReturn(parser, "foobar")
        checkResultWithSameReturn(parser, "foofoo")
        checkResultWithSameReturn(parser, "barbarbar")
        checkResultWithSameReturn(parser, "foobarfoobar")
    }

    @Test
    fun testValueGeneration6() {
        val p1 = AnyOf("0".parser, "1".parser)
        val parser = ZeroOrMore(AnyOf(p1, "foo".parser, "bar".parser)) generates {it.text }

        checkResultWithSameReturn(parser, "")
        checkResultWithSameReturn(parser, "1foo")
        checkResultWithSameReturn(parser, "bar011")
        checkResultWithSameReturn(parser, "foobar")
        checkResultWithSameReturn(parser, "foo1foo")
        checkResultWithSameReturn(parser, "0101011bar1bar111bar0001")
        checkResultWithSameReturn(parser, "foobarfoobar")
    }

    @Test
    fun testValueGeneration7() {
        val parser = Sequence("foo".parser, Sequence("bar".parser, "bur".parser)) generates {it.text }

        checkResult(parser, "foobarbur", "foobarbur")
        checkParsing(parser, false, "foobar")
    }

    @Test
    fun testExpressionParsing() {

        val whitespace = CharParser(" \t\n", ZERO_OR_MORE).named("whitespace")

        val digit = CharParser('0'..'9').named("digit")

        val number =
                Sequence(
                        Optional("-"),
                        OneOrMore(digit),
                        whitespace
                ).named("number").generates {
                    Integer.parseInt(it.text.trim())
                }

        val expression = LazyParser()

        val parens =
                Sequence(
                        "(".unaryPlus(),
                        whitespace,
                        expression,
                        ")".unaryPlus(),
                        whitespace
                ).named("parens")

        val term = AnyOf(number, parens).named("term")

        expression.parser = Sequence(
                term,
                Optional(
                        Sequence(
                                "+".unaryPlus(),
                        whitespace,
                        term).generates { it.pop<Int>() + it.pop<Int>() }
                ),
                whitespace
        ).named("expression")

        val inputLine = Sequence(
                whitespace,
                expression,
                EndOfInput()
        ).named("inputLine")


        checkParsing(whitespace, true, "")
        checkParsing(whitespace, true, " ")
        checkParsing(whitespace, true, "   ")
        checkParsing(whitespace, true, "\n")
        checkParsing(whitespace, true, "  \t \n ")
        checkResult(inputLine, "1", 1)
        checkResult(inputLine, "10", 10)
        checkResult(inputLine, "-120", -120)
        checkResult(inputLine, " 1203456798 ", 1203456798)
        checkResult(inputLine, "1+1", 2)
        checkResult(inputLine, "1+2", 3)
        checkResult(inputLine, "1+-2", -1)
        checkResult(inputLine, "1+(3+2)", 6)
        checkResult(inputLine, "(1+(3+2))", 6)
        checkResult(inputLine, "((1+3)+2)", 6)
        checkResult(inputLine, "((1+-3)+-12)", -14)
        checkResult(inputLine, "1+ 2", 3)
        checkResult(inputLine, "  1  \n + 2 ", 3)
        checkResult(inputLine, "(1 + 3) + 2", 6)
        checkResult(inputLine, "1 + (3 + 2)", 6)
        checkResult(inputLine, "-1 + (-3 + -2)", -6)
        checkParsing(inputLine, false, "foobar")
        checkParsing(inputLine, false, "1 +")
        checkParsing(inputLine, false, "+1")
        checkParsing(inputLine, false, "122f")
        checkParsing(inputLine, false, "- 111")
        checkParsing(inputLine, false, "1 + (2 + 3 + 4)")
        checkParsing(inputLine, false, "(1 + 4")
        checkParsing(inputLine, false, "   \n      (  1     \n                 +                4")
    }


    @Test
    fun testLeftRecursiveParsing() {
        val p = LazyParser()
        p.parser = Sequence(Optional(p), +"a")
        val parser = p.generatesMatchedText()

        checkParsing(parser, false, "")
        checkParsing(parser, false, "b")
        checkResultWithSameReturn(parser, "a")
        checkResultWithSameReturn(parser, "aa")
        checkResultWithSameReturn(parser, "aaaaaaaaaaaaaaa")
        checkParsing(parser, true, "a")
        checkParsing(parser, true, "aaaa")
        checkParsing(parser, false, "b")
        checkParsing(parser, false, "baaa")
        checkParsing(parser, false, " aa")

    }

    @Test
    fun testIndirectLeftRecursiveParsing() {
        val parser1 = LazyParser()
        val parser2 = Sequence(Optional(parser1), +"c")
        val parser3 = Sequence(parser2, +"b")
        parser1.parser = Sequence(parser3, +"a")
        val parser = parser1.generatesMatchedText()

        checkResultWithSameReturn(parser, "cba")
        checkResultWithSameReturn(parser, "cbacba")
        checkResultWithSameReturn(parser, "cbacbacbacba")
        checkParsing(parser, true, "cba")
        checkParsing(parser, true, "cbacbacba")
        checkParsing(parser, false, "abc")
        checkParsing(parser, false, "c")
        checkParsing(parser, false, "cb")
        checkParsing(parser, false, "cbba")
        checkParsing(parser, false, " cba")
        checkParsing(parser, false, "abba")

    }

    @Test
    fun testRightRecursiveParsing() {
        val p = LazyParser()
        p.parser = Sequence(+"a", Optional(p))
        val parser = p.generatesMatchedText()

        checkParsing(parser, false, "")
        checkParsing(parser, false, "b")
        checkResultWithSameReturn(parser, "a")
        checkResultWithSameReturn(parser, "aa")
        checkResultWithSameReturn(parser, "aaaaaaaaaaaaaaa")
        checkParsing(parser, true, "a")
        checkParsing(parser, true, "aaaa")
        checkParsing(parser, false, "b")
        checkParsing(parser, false, "baaa")
        checkParsing(parser, false, " aa")

    }

    @Test
    fun testIndirectRightRecursiveParsing() {
        val parser1 = LazyParser()
        val parser2 = Sequence(+"c", Optional(parser1))
        val parser3 = Sequence(+"b", parser2)
        parser1.parser = Sequence(+"a", parser3)
        val parser = parser1.generatesMatchedText()

        checkResultWithSameReturn(parser, "abc")
        checkResultWithSameReturn(parser, "abcabc")
        checkResultWithSameReturn(parser, "abcabcabcabc")
        checkParsing(parser, true, "abc")
        checkParsing(parser, false, "cba")
        checkParsing(parser, false, "a")
        checkParsing(parser, false, "ab")
        checkParsing(parser, false, "aabc")
        checkParsing(parser, false, "abba")

    }

    private fun checkParsing(parser: Parser, shouldSucceed: Boolean, testString: String) {
        assertEquals(shouldSucceed, parser.parse(testString, "parsing test").success)
    }

    private fun checkResult(parser: Parser, testString: String, vararg expectedResult: Any) {
        val result = parser.parse(testString, "parsing test")
        // DEBUG: if (result is ParsingFail) println("Failed: $result")
        assertEquals(true, result.success)
        assertEquals(expectedResult.toList(), (result as ParseSuccess).results.toList())
    }

    private fun checkResultWithSameReturn(parser: Parser, testString: String) {
        checkResult(parser, testString, testString)
    }

}
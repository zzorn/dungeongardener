package org.dungeongardener

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
        val parser = Sequence("foo".parser, "bar".parser) generates {it.text }

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
        val whitespace = ZeroOrMoreCharParser(" \t\n").named("whitespace")
        val digit = CharParser("0123456789").named("digit")
        val number = Sequence(Optional("-".parser), OneOrMore(digit), whitespace).named("number") generates {Integer.parseInt(it.text.trim())}
        val expression = LazyParser()
        val parens = Sequence("(".parser, whitespace, expression, ")".parser, whitespace).named("parens")
        val term = AnyOf(number, parens).named("term")
        expression.parser = Sequence(
                term,
                Optional(
                        Sequence(
                        "+".parser,
                        whitespace,
                        term).generates { it.pop<Int>() + it.pop<Int>() }
                ),
                whitespace).named("expression")
        val inputLine = Sequence(whitespace, expression, EndOfInput()).named("inputLine")


        checkParsing(whitespace, true, "")
        checkParsing(whitespace, true, " ")
        checkParsing(whitespace, true, "   ")
        checkParsing(whitespace, true, "\n")
        checkParsing(whitespace, true, "  \t \n ")
        checkResult(inputLine, "1", 1)
        checkResult(inputLine, "10", 10)
        checkResult(inputLine, "-120", -120)
        checkResult(inputLine, " 1 ", 1)
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
    }


    private fun checkParsing(parser: Parser, shouldSucceed: Boolean, testString: String) {
        assertEquals(shouldSucceed, parser.parse(testString).success)
    }

    private fun checkResult(parser: Parser, testString: String, vararg expectedResult: Any) {
        val result = parser.parse(testString)
        assertEquals(true, result.success)
        assertEquals(expectedResult.toList(), (result as ParseSuccess).results.toList())
    }

    private fun checkResultWithSameReturn(parser: Parser, testString: String) {
        checkResult(parser, testString, testString)
    }

}
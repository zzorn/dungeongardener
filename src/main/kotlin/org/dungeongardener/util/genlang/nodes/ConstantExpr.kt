package org.dungeongardener.util.genlang.nodes

import org.dungeongardener.util.Context

/**
 * Stores a constant value.
 */
class ConstantExpr(val value: Any) : Expression {

    override fun <T> evaluate(context: Context): T {
        return value as T
    }

    override fun toString(): String {
        return when (value) {
            is String -> "\"${escapeString(value)}\""
            is Char -> "\'${escapeString(""+value)}\'"
            is Number -> value.toString()
            is Boolean -> value.toString()
            else -> value.toString() // NOTE: This may break generation of code, depending on what kind of constant it is
        }
    }

    private fun escapeString(value: String): String {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\t", "\\t")
    }
}
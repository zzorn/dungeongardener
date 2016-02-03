package org.dungeongardener.util.parser

import java.util.*

/**
 * Holds various data that needs to be cached during parsing.
 */
class ParsingCaches(val packRatCache: MutableMap<PackRatKey, PackRatEntry> = HashMap(),
                    val leftRecursionHeads: MutableMap<Int, HeadEntry> = HashMap(),
                    val leftRecursionStack: Deque<LeftRecursion> = ArrayDeque()) {

    data class PackRatKey(val parser: Parser, val startPos: Int)

    data class PackRatEntry(var node: ASTNode?, var lr: LeftRecursion?) {
        fun setASTNode(node: ASTNode?) {
            this.node = node
            this.lr = null
        }
    }

    data class LeftRecursion(var seed: ASTNode?, val rule: Parser, var head: HeadEntry?)

    data class HeadEntry(val rule: Parser, val involvedSet: MutableSet<Parser> = HashSet(), val evalSet: MutableSet<Parser> = HashSet())

}
package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.ParsingCaches.*

/**
 * Base class for parsers
 */
abstract class ParserBase : Parser {

    override var name: String = this.javaClass.simpleName

    final override fun parse(parent: ASTNode): Boolean {

        val node = applyRule(parent)
        return node != null
    }

    private final fun applyRule(parent: ASTNode): ASTNode? {

        val caches = parent.caches

        // Check if we have a previously cached entry
        val pos = parent.end
        val packRatCacheKey = PackRatKey(this, pos)
        val entry = recall(parent, this, pos, caches)

        if (entry == null) {
            // Create a new LR and push it onto the rule invocation stack.
            val lr = LeftRecursion(null, this, null)
            caches.leftRecursionStack.push(lr)

            // Memoize lr
            val newCacheEntry = PackRatEntry(null, lr)
            caches.packRatCache.put(packRatCacheKey, newCacheEntry)

            // Try to parse with this parser
            val ans = eval(parent)

            // Pop lr off the rule invocation stack.
            caches.leftRecursionStack.pop()

            if (lr.head != null) {
                lr.seed = ans
                return leftRecursionAnswer(this, pos, newCacheEntry, parent, caches)
            } else {
                newCacheEntry.setAns(ans)
                return ans
            }
        }
        else {
            val lr = entry.lr
            if (lr != null) {
                setupLeftRecursion(this, lr, caches)
                return lr.seed
            }
            else {
                return entry.node
            }
        }
    }

    private fun eval(parent: ASTNode): ASTNode? {
        // Create node for this parser
        val parserNode: ASTNode = parent.addSubNode(this)

        // Try to parse with this parser
        if (doParse(parserNode)) {
            return parserNode
        }
        else {
            // Remove failed node from parent
            parent.removeSubNode()

            // Update error message
            if (!parent.errorMessage.hasError()) {
                parent.errorMessage.update(parserNode)
            }

            return null
        }
    }

    private fun growLeftRecursion(pos: Int, parent: ASTNode, cacheEntry: PackRatEntry, head: HeadEntry, caches: ParsingCaches): ASTNode? {

        caches.leftRecursionHeads.put(pos, head)

        // Parse node again
        while (true) {

            head.evalSet.clear()
            head.evalSet.addAll(head.involvedSet)

            val oldEnd = parent.end
            val astNode = eval(parent)
            if (astNode != null && parent.end > oldEnd) {
                cacheEntry.setAns(astNode)
            }
            else {
                break
            }
        }

        caches.leftRecursionHeads.remove(pos)

        return cacheEntry.node
    }

    private fun setupLeftRecursion(rule: Parser, leftRecursion: LeftRecursion, caches: ParsingCaches) {
        var lrHead = leftRecursion.head
        if (lrHead == null) {
            lrHead = HeadEntry(rule)
            leftRecursion.head = lrHead
        }

        for (s in caches.leftRecursionStack) {
            if (s.head != lrHead) {
                s.head = lrHead
                lrHead.involvedSet.add(rule)
            }
            else {
                break
            }
        }
    }

    private fun leftRecursionAnswer(rule: Parser, pos: Int, packRatEntry: PackRatEntry, parent: ASTNode, caches: ParsingCaches): ASTNode? {
        val head = packRatEntry.lr!!.head!!
        val seed = packRatEntry.lr!!.seed
        if (head.rule != rule) {
            return seed
        }
        else {
            packRatEntry.setAns(seed)
            if (seed == null) return null
            else return growLeftRecursion(pos, parent, packRatEntry, head, caches)
        }
    }


    private fun recall(parent: ASTNode, rule: Parser, pos: Int, caches: ParsingCaches): PackRatEntry? {
        val packRatEntry = caches.packRatCache.get(PackRatKey(rule, pos))
        val head = caches.leftRecursionHeads.get(pos)

        // If not growing a seed parse, just return what is stored in the memo table
        if (head == null) {
            return packRatEntry
        }

        // Do not evaluate any rule that is not involved in this left recursion.
        if (packRatEntry == null && !(head.involvedSet.contains(rule) || rule == head.rule)) {
            return PackRatEntry(null, null)
        }

        // Allow involved rules to be evaluated, but only once, during a seed-growing iteration.
        if (head.evalSet.contains(rule)) {
            head.evalSet.remove(rule)

            val astNode: ASTNode? = eval(parent)
            packRatEntry!!.setAns(astNode)
        }

        return packRatEntry
    }

    abstract fun doParse(parserNode: ASTNode): Boolean



}
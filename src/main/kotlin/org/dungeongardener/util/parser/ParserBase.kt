package org.dungeongardener.util.parser

import org.dungeongardener.util.parser.ParsingCaches.*

/**
 * Base class for parsers
 */
abstract class ParserBase : Parser {

    override var name: String = this.javaClass.simpleName

    final override fun parse(parent: ASTNode): Boolean {
        val node = applyRule(parent)

        // Add back previously parsed node to parent, if it hasn't already been added
        parent.addIfNotAlreadyAdded(node)

        return node != null
    }

/*
    private fun applyRule(parent: ASTNode): ASTNode? {
        val packRatCache = parent.caches.packRatCache
        val key = PackRatKey(this, parent.end)

        val cachedNode= packRatCache.get(key)
        if (cachedNode == null) {
            packRatCache.put(key, null)

            val node = eval(parent)

            packRatCache.put(key, node)

            return node
        } else {
            if (cachedNode != null) {
                // Add back previously parsed node to parent
                parent.addSubNode(cachedNode)
            }
            return cachedNode
        }
    }

*/

    private fun eval(parent: ASTNode): ASTNode? {
        if (parent.debugOutput) println("  ".repeat(parent.depth) + "### Parsing '$name' at ${parent.end}")

        // Create node for this parser
        val node: ASTNode = parent.addSubNode(this)

        // Try to parse with this parser
        if (doParse(node)) {

            if (parent.debugOutput) {
                print("  ".repeat(parent.depth + 1) + "### '$name' ")
                println("success: '${node.matchedText}'  \n\n" + node + "\n\n")
            }

            return node
        }
        else {
            // Remove failed node from parent
            parent.removeSubNode()

            if (parent.debugOutput) {
                print("  ".repeat(parent.depth + 1) + "### '$name' ")
                println("failure")
            }

            // Update error message
            if (!parent.errorMessage.hasError()) {
                parent.errorMessage.update(node)
            }

            return null
        }

    }


     private final fun applyRule(parent: ASTNode): ASTNode? {

         val caches = parent.caches

         // Check if we have a previously cached entry
         val pos = parent.end
         val packRatCacheKey = PackRatKey(this, pos)
         val m = recall(parent, this, pos, caches)

         if (m == null) {
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

             if (lr.head == null) {
                 // Simple result
                 newCacheEntry.setASTNode(ans)
                 return ans
             } else {
                 lr.seed = ans
                 return leftRecursionAnswer(this, pos, newCacheEntry, parent, caches)
             }
         }
         else {
             val lr = m.lr
             if (lr != null) {
                 setupLeftRecursion(this, lr, caches)
                 return lr.seed
             }
             else {
                 return m.node
             }
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
                 cacheEntry.setASTNode(astNode)
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
             packRatEntry.setASTNode(seed)
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
             packRatEntry!!.setASTNode(astNode)
         }

         return packRatEntry
     }



    abstract fun doParse(parserNode: ASTNode): Boolean


}
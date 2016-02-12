package org.dungeongardener.model.background

import org.dungeongardener.model.background.activities.*
import org.dungeongardener.util.genlang.GenLang
import org.dungeongardener.util.genlang.nodes.Expression

/**
 *
 */
class BackgroundLanguage() : GenLang("activity") {

    val activity = lazy

    init {

        val note = (keyword("note") - "(" - quotedString - ")" + ws).generates {
            NoteActivity(it.pop())
        }

        val learn = (keyword("learn") - "(" - quotedString - "," - expression - ")" + ws).generates {
            LearnActivity(it.pop(), it.pop())
        }

        val choice = (keyword("choice") - "(" - quotedString - ")" - "{" - zeroOrMore(activity).generatesContentList() - "}" + ws).generates {
            ChoiceActivity(it.pop(1), it.pop())
        }

        val ordered = (keyword("ordered") - "(" - quotedString - ")" - "{" - zeroOrMore(activity).generatesContentList() - "}" + ws).generates {
            OrderedActivity(it.pop(1), it.pop())
        }

        val repeat = (keyword("repeat") - "(" - expression - ")" - "{" - zeroOrMore(activity).generatesContentList() - "}" + ws).generates {
            RepeatActivity(it.pop<Expression>(1), OrderedActivity("repeating", it.pop()))
        }

        activity.parser = any(ordered, choice, learn, note, repeat)

        statement.parser = activity


    }

}
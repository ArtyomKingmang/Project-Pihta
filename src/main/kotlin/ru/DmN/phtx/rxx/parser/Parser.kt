package ru.DmN.phtx.rxx.parser

import ru.DmN.pht.std.processor.utils.nodeAdd
import ru.DmN.pht.std.processor.utils.nodeAppFn
import ru.DmN.pht.std.processor.utils.nodePrintln
import ru.DmN.pht.std.processor.utils.nodeValue
import ru.DmN.phtx.rxx.lexer.Lexer
import ru.DmN.phtx.rxx.lexer.TokenType.*
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.STRING
import ru.DmN.siberia.parser.ctx.ParsingContext
import java.util.Stack

class Parser(val lexer: Lexer) {
    val tokens = Stack<Token>()
    var lastToken: Token? = null

    fun parseNodes(): MutableList<Node> {
        val list = ArrayList<Node>()
        while (true) {
            tokens += nextToken() ?: break
            list += parseNode(ParsingContext())
        }
        return list
    }

    fun parseNode(ctx: ParsingContext): Node {
        val words = StringBuilder()
        var nodes: MutableList<Node>? = null
        var tk = nextToken()!!
        while (true) {
            when (tk.type) {
                STRING -> return nodeValue(tk.line, tk.text!!)
                WORD -> words.append(tk.text!!)
                DOT -> break
                COLON -> {
                    nodes = parseArguments(ctx)
                    break
                }
                else -> throw RuntimeException()
            }
            tk = nextToken()!!
        }
        return COMMANDS.find { it.first.matches(words) }!!.second(tk.line, nodes ?: mutableListOf(), this, ctx)
    }

    fun parseArguments(ctx: ParsingContext): MutableList<Node> {
        val list = ArrayList<Node>()
        while (true) {
            val tk = nextToken()!!
            if (tk.type == DOT)
                break
            tokens += tk
            list += parseNode(ctx)
            if (lastToken!!.type == DOT)
                continue
            val tk1 = nextToken()!!
            when (tk1.type) {
                COMMA -> {}
                DOT -> break
                else -> throw RuntimeException()
            }
        }
        return list
    }

    fun nextToken(): Token? {
        lastToken =
            if (tokens.isEmpty())
                lexer.next()
            else tokens.pop()
        return lastToken
    }

    companion object {
        val COMMANDS =
            ArrayList<Pair<Regex, (line: Int, nodes: MutableList<Node>, parser: Parser, ctx: ParsingContext) -> Node>>()

        infix fun String.command(command: (line: Int, nodes: MutableList<Node>, parser: Parser, ctx: ParsingContext) -> Node) {
            COMMANDS.add(Pair(Regex(this.replace(" ", "")), command))
        }
    }

    init {
        "Начало бытия" command { line, nodes, _, _ -> nodeAppFn(line, nodes) }
        "Молвить" command { line, nodes, _, _ -> nodePrintln(line, nodes) }
        "Сумму" command { line, nodes, _, _ -> nodeAdd(line, nodes) }
    }
}
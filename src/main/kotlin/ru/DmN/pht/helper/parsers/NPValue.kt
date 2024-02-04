package ru.DmN.pht.helper.parsers

import ru.DmN.pht.helper.ast.NodeValue
import ru.DmN.pht.node.NodeTypes
import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.OPERATION
import ru.DmN.siberia.lexer.Token.DefaultType.STRING
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser

object NPValue : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node =
        when (token.type) {
            OPERATION, STRING -> NodeValue(INodeInfo.of(NodeTypes.VALUE, ctx, token), token.text!!)
            else -> throw RuntimeException()
        }
}
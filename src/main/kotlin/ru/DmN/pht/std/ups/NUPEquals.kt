package ru.DmN.pht.std.ups

import ru.DmN.pht.base.Parser
import ru.DmN.pht.base.Processor
import ru.DmN.pht.base.Unparser
import ru.DmN.pht.base.ast.Node
import ru.DmN.pht.base.lexer.Token
import ru.DmN.pht.base.parser.ParsingContext
import ru.DmN.pht.base.parsers.NPDefault
import ru.DmN.pht.base.processor.ProcessingContext
import ru.DmN.pht.base.unparser.UnparsingContext
import ru.DmN.pht.base.utils.VirtualType
import ru.DmN.pht.std.ast.NodeEquals
import ru.DmN.pht.std.processors.INodeUniversalProcessor
import ru.DmN.pht.std.unparsers.NUDefaultX

object NUPEquals : INodeUniversalProcessor<NodeEquals, NodeEquals> {
    override fun parse(parser: Parser, ctx: ParsingContext, operationToken: Token): Node =
        NPDefault.parse(parser, ctx) {
            NodeEquals(
                operationToken,
                it,
                when (operationToken.text!!) {
                    "="     -> NodeEquals.Operation.EQ
                    "!="    -> NodeEquals.Operation.NE
                    "<"     -> NodeEquals.Operation.LT
                    "<="    -> NodeEquals.Operation.LE
                    ">"     -> NodeEquals.Operation.GT
                    ">="    -> NodeEquals.Operation.GE
                    else -> throw RuntimeException()
                }
            )
        }

    override fun unparse(node: NodeEquals, unparser: Unparser, ctx: UnparsingContext, indent: Int) =
        NUDefaultX.unparse(node, unparser, ctx, indent)

    override fun calc(node: NodeEquals, processor: Processor, ctx: ProcessingContext): VirtualType =
        VirtualType.BOOLEAN
}
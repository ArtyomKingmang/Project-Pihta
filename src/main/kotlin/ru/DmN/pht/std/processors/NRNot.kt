package ru.DmN.pht.std.processors

import ru.DmN.pht.base.Processor
import ru.DmN.pht.base.ast.Node
import ru.DmN.pht.base.ast.NodeNodesList
import ru.DmN.pht.base.lexer.Token
import ru.DmN.pht.base.processor.ProcessingContext
import ru.DmN.pht.base.processor.ValType
import ru.DmN.pht.base.processors.INodeProcessor
import ru.DmN.pht.base.utils.VirtualType
import ru.DmN.pht.std.processor.utils.nodeMCall
import ru.DmN.pht.std.ups.NUPMCallA
import ru.DmN.pht.std.utils.line

object NRNot : INodeProcessor<NodeNodesList> {
    override fun calc(node: NodeNodesList, processor: Processor, ctx: ProcessingContext): VirtualType? {
        val type = processor.calc(node.nodes[0], ctx)
        return if (type!!.isPrimitive)
            type
        else NUPMCallA.calc(nodeMCall(node.token.line, node.nodes[0], "not", emptyList()), processor, ctx)
    }

    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): Node? {
        val value = processor.process(node.nodes[0], ctx, ValType.VALUE)!!
        return if (processor.calc(value, ctx)!!.isPrimitive)
            if (mode == ValType.VALUE)
                NodeNodesList(node.token.processed(), mutableListOf(value))
            else null
        else NUPMCallA.process(
            nodeMCall(node.line, value, "not", emptyList()),
            processor,
            ctx,
            ValType.VALUE
        )
    }
}
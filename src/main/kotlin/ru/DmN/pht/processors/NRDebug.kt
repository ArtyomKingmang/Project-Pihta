package ru.DmN.pht.processors

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processors.INodeProcessor

object NRDebug : INodeProcessor<Node> {
    override fun process(node: Node, processor: Processor, ctx: ProcessingContext, valMode: Boolean): Node? {
//        throw MessageException(null, "Debug ${if (valMode) "(val)" else "(no val())"}")
        println(ctx.platform)
        return null
    }
}
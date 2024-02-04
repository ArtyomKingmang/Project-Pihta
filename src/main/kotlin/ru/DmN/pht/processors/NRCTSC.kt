package ru.DmN.pht.processors

import ru.DmN.pht.ast.NodeValue
import ru.DmN.pht.node.nodeValue
import ru.DmN.pht.processor.utils.global
import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.utils.VirtualType

/***
 * Compile-Time String Constant
 */
class NRCTSC(val const: (processor: Processor, ctx: ProcessingContext) -> String) : INodeProcessor<Node> {
    override fun calc(node: Node, processor: Processor, ctx: ProcessingContext): VirtualType =
        ctx.global.getType("String", processor.tp)

    override fun process(node: Node, processor: Processor, ctx: ProcessingContext, mode: ValType): NodeValue =
        nodeValue(node.info, const(processor, ctx))
}
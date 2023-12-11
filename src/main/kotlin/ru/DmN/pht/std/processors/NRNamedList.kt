package ru.DmN.pht.std.processors

import ru.DmN.pht.std.ast.NodeNamedList
import ru.DmN.pht.std.utils.computeString
import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.processors.NRDefault
import ru.DmN.siberia.utils.VirtualType

object NRNamedList : INodeProcessor<NodeNodesList> {
    override fun calc(node: NodeNodesList, processor: Processor, ctx: ProcessingContext): VirtualType? =
        NRDefault.calc(node, processor, ctx)

    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): NodeNamedList =
        NodeNamedList(node.token.processed(), node.nodes.drop(1).toMutableList(), processor.computeString(node.nodes[0], ctx)).apply { NRDefault.process(this, processor, ctx, mode) }
}
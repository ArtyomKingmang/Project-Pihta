package ru.DmN.pht.std.processors

import ru.DmN.pht.std.ast.NodeASet
import ru.DmN.pht.std.node.NodeTypes
import ru.DmN.pht.std.utils.processNodes
import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.utils.VirtualType

object NRASet : INodeProcessor<NodeNodesList> {
    override fun calc(node: NodeNodesList, processor: Processor, ctx: ProcessingContext): VirtualType? =
        processor.calc(node.nodes[2], ctx)

    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): NodeASet {
        val nodes = processor.processNodes(node, ctx, ValType.VALUE)
        return NodeASet(node.info.withType(NodeTypes.ASET_), nodes[0], nodes[1], nodes[2])
    }
}
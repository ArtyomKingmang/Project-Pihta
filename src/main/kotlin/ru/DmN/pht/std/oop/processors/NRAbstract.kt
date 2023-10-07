package ru.DmN.pht.std.oop.processors

import ru.DmN.pht.base.Processor
import ru.DmN.pht.base.parser.ast.NodeNodesList
import ru.DmN.pht.base.processor.processors.INodeProcessor
import ru.DmN.pht.base.processor.processors.NRDefault
import ru.DmN.pht.base.processor.utils.ProcessingContext
import ru.DmN.pht.base.processor.utils.ValType
import ru.DmN.pht.std.oop.ast.IAbstractlyNode

object NRAbstract : INodeProcessor<NodeNodesList> {
    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): NodeNodesList {
        val new = NRDefault.process(node, processor, ctx, mode)
        new.nodes.forEach { if (it is IAbstractlyNode) it.abstract = true }
        return new
    }
}
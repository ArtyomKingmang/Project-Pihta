package ru.DmN.pht.processors

import ru.DmN.pht.ast.NodeFSet
import ru.DmN.pht.node.NodeTypes.FSET_
import ru.DmN.pht.processor.utils.global
import ru.DmN.pht.utils.computeString
import ru.DmN.pht.utils.isConstClass
import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.ValType.VALUE
import ru.DmN.siberia.processors.INodeProcessor

object NRFSetA : INodeProcessor<NodeNodesList> {
    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): NodeFSet {
        val instance =
            if (node.nodes[0].isConstClass)
                ctx.global.getType(processor.computeString(node.nodes[0], ctx), processor.tp)
            else processor.calc(node.nodes[0], ctx)!!
        val name = processor.computeString(node.nodes[1], ctx)
        return NodeFSet(
            node.info.withType(FSET_),
            mutableListOf(processor.process(node.nodes[0], ctx, VALUE)!!, processor.process(node.nodes[2], ctx, VALUE)!!),
            instance.fields.find { it.name == name }!!
        )
    }
}
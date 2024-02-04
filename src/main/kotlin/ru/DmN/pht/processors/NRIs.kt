package ru.DmN.pht.processors

import ru.DmN.pht.ast.NodeIsAs
import ru.DmN.pht.node.NodeTypes.IS_
import ru.DmN.pht.node.nodeValue
import ru.DmN.pht.processor.utils.global
import ru.DmN.pht.utils.computeString
import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.Platforms.JVM
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.ValType.VALUE
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.utils.VirtualType
import ru.DmN.siberia.utils.isPrimitive

object NRIs : INodeProcessor<NodeNodesList> {
    override fun calc(node: NodeNodesList, processor: Processor, ctx: ProcessingContext): VirtualType =
        VirtualType.BOOLEAN

    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): Node {
        return when (ctx.platform) {
            JVM -> {
                val type = processor.computeString(node.nodes[0], ctx)
                val value = processor.calc(node.nodes[1], ctx)
                nodeValue(
                    node.info,
                    if (type.isPrimitive()) {
                        if (value?.isPrimitive == true)
                            type == value.name
                        else false
                    } else if (value?.isPrimitive == true)
                        false
                    else return NodeIsAs(node.info.withType(IS_), mutableListOf(processor.process(node.nodes[1], ctx, VALUE)!!), ctx.global.getType(type, processor.tp))
                )
            }

            else -> node
        }
    }
}
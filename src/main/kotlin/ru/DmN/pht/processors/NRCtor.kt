package ru.DmN.pht.processors

import ru.DmN.pht.ast.NodeDefn
import ru.DmN.pht.processor.ctx.BodyContext
import ru.DmN.pht.processor.ctx.clazz
import ru.DmN.pht.processor.ctx.with
import ru.DmN.pht.utils.dropMutable
import ru.DmN.pht.utils.node.NodeParsedTypes
import ru.DmN.pht.utils.type
import ru.DmN.pht.utils.vtype.PhtVirtualMethod
import ru.DmN.pht.utils.vtype.PhtVirtualType
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage.METHODS_BODY
import ru.DmN.siberia.processor.utils.processNodesList
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.utils.exception.pushOrRunTask
import ru.DmN.siberia.utils.vtype.MethodModifiers
import ru.DmN.siberia.utils.vtype.VirtualType

object NRCtor : INodeProcessor<NodeNodesList> {
    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, valMode: Boolean): NodeDefn {
        val type = ctx.clazz as PhtVirtualType.Impl
        //
        val args = NRDefn.parseArguments(node.nodes[0], type.name, type.genericsDefine, processor, ctx)
        //
        val method = PhtVirtualMethod.Impl(
            type,
            "<init>",
            VirtualType.VOID,
            null,
            args.first,
            args.second,
            args.third,
            MethodModifiers(ctor = true),
            extension = null,
            generator = null,
            inline = null,
            type.genericsDefine
        )
        type.methods += method
        //
        val new = NodeDefn(node.info.withType((node.type as NodeParsedTypes).processed), node.nodes.dropMutable(1), method)
        processor.pushOrRunTask(METHODS_BODY, node) {
            processNodesList(new, processor, ctx.with(method).with(BodyContext.of(method)), false)
        }
        return new
    }
}
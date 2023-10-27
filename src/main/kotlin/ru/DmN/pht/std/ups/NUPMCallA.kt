package ru.DmN.pht.std.ups

import ru.DmN.pht.base.Parser
import ru.DmN.pht.base.Processor
import ru.DmN.pht.base.Unparser
import ru.DmN.pht.base.ast.Node
import ru.DmN.pht.base.ast.NodeNodesList
import ru.DmN.pht.base.lexer.Token
import ru.DmN.pht.base.parser.ParsingContext
import ru.DmN.pht.base.parsers.NPDefault
import ru.DmN.pht.base.processor.ProcessingContext
import ru.DmN.pht.base.processor.ValType
import ru.DmN.pht.base.unparser.UnparsingContext
import ru.DmN.pht.base.unparsers.NUDefault
import ru.DmN.pht.base.utils.VirtualMethod
import ru.DmN.pht.base.utils.VirtualType
import ru.DmN.pht.std.ast.NodeMCall
import ru.DmN.pht.std.ast.NodeValue
import ru.DmN.pht.std.processor.utils.*
import ru.DmN.pht.std.processors.INodeUniversalProcessor
import ru.DmN.pht.std.processors.NRArrayOf
import ru.DmN.pht.std.processors.NRAs
import ru.DmN.pht.std.processors.NRNewArray
import ru.DmN.pht.std.utils.computeString
import ru.DmN.pht.std.utils.line

object NUPMCallA : INodeUniversalProcessor<NodeMCall, NodeNodesList> {
    override fun parse(parser: Parser, ctx: ParsingContext, operationToken: Token): Node? =
        NPDefault.parse(parser, ctx, operationToken)

    override fun unparse(node: NodeMCall, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        unparser.out.apply {
            append('(').append(node.token.text).append(' ')
            unparser.unparse(node.instance, ctx, indent + 1)
            append(' ').append(node.method.name)
            NUDefault.unparseNodes(node, unparser, ctx, indent)
            append(')')
        }
    }

    override fun calc(node: NodeNodesList, processor: Processor, ctx: ProcessingContext): VirtualType =
        if (node is NodeMCall) node.method.rettype else findMethod(node, processor, ctx).third.rettype

    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): NodeMCall {
        val triple = findMethod(node, processor, ctx)
        val instance = if (triple.first == NodeMCall.Type.SUPER)
            nodeGetOrNameOf(node.line, "this")
        else processor.process(node.nodes[0], ctx, mode)!!
        return if (triple.third.extend == null)
            NodeMCall(
                node.token,
                processArguments(node.line, processor, ctx, triple.third, triple.second),
                instance,
                triple.third,
                if (triple.first == NodeMCall.Type.UNKNOWN)
                    if (triple.third.modifiers.static)
                        NodeMCall.Type.STATIC
                    else NodeMCall.Type.VIRTUAL
                else triple.first
            )
        else NodeMCall(
            node.token,
            processArguments(
                node.line,
                processor,
                ctx,
                triple.third,
                listOf(instance) + triple.second
            ),
            NodeValue.of(node.token.line, NodeValue.Type.CLASS, triple.third.extend!!.name),
            triple.third,
            NodeMCall.Type.EXTEND
        )
    }

    fun processArguments(line: Int, processor: Processor, ctx: ProcessingContext, method: VirtualMethod, args: List<Node>): MutableList<Node> =
        (if (method.modifiers.varargs) {
            val overflow = args.size.let { if (it > 0) it + 1 else 0 } - method.argsc.size
            if (overflow > 0)
                args.dropLast(overflow).toMutableList().apply {
                    add(
                        NRArrayOf.process(
                            nodeArray(line, args.drop(args.size - overflow).toMutableList()),
                            processor,
                            ctx,
                            ValType.VALUE
                        )!!
                    )
                }
            else (args + NRNewArray.process(
                nodeNewArray(line, method.argsc.last().name.substring(1), 0),
                processor,
                ctx,
                ValType.VALUE
            )!!).toMutableList()
        } else args)
            .mapIndexed { i, it -> NRAs.process(nodeAs(line, it, method.argsc[i].name), processor, ctx, ValType.VALUE)!! }
            .toMutableList()

    private fun findMethod(node: NodeNodesList, processor: Processor, ctx: ProcessingContext): Triple<NodeMCall.Type, List<Node>, VirtualMethod> {
        lateinit var clazz: VirtualType
        val type = node.nodes[0].let {
            if (it.isConstClass()) {
                clazz = ctx.global.getType(it.getConstValueAsString(), processor.tp)
                NodeMCall.Type.STATIC
            } else {
                if (it.isLiteral()) {
                    when (processor.computeString(it, ctx)) {
                        "this" -> {
                            clazz = processor.calc(it, ctx)!!
                            return@let NodeMCall.Type.THIS
                        }
                        "super" -> {
                            clazz = ctx.clazz.superclass!!
                            return@let NodeMCall.Type.SUPER
                        }
                    }
                }

                clazz = processor.calc(it, ctx)!!
                NodeMCall.Type.UNKNOWN
            }
        }
        val result = findMethod(
            clazz,
            processor.computeString(node.nodes[1], ctx),
            node.nodes.asSequence().drop(2).map { processor.process(it, ctx, ValType.VALUE)!! },
            processor,
            ctx
        )
        return Triple(type, result.first, result.second)
    }

    fun findMethod(clazz: VirtualType, name: String, args: Sequence<Node>, processor: Processor, ctx: ProcessingContext): Pair<List<Node>, VirtualMethod> {
        val methods = ctx.global.getMethodVariants(clazz, name, args.map { ICastable.of(processor.tp.typeOf(processor.calc(it, ctx)!!.name)) }.toList())
        if (methods.isEmpty())
            throw RuntimeException("Method '$name' not founded!")
        return Pair(args.toList(), methods.first())
    }
}
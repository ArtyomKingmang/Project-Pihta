package ru.DmN.pht.std.processors

import ru.DmN.pht.base.Processor
import ru.DmN.pht.base.ast.Node
import ru.DmN.pht.base.processor.utils.ProcessingContext
import ru.DmN.pht.base.processors.INodeProcessor

interface IStdNodeProcessor<T : Node> : INodeProcessor<T> {
    fun compute(node: T, processor: Processor, ctx: ProcessingContext): Node =
        node

    val isComputeString: Boolean
        get() = false

    fun computeString(node: T, processor: Processor, ctx: ProcessingContext): String =
        throw UnsupportedOperationException()

    val isComputeInt: Boolean
        get() = false

    fun computeInt(node: T, processor: Processor, ctx: ProcessingContext): Int =
        throw UnsupportedOperationException()

    val isComputeList: Boolean
        get() = false

    fun computeList(node: T, processor: Processor, ctx: ProcessingContext): List<Node> =
        throw UnsupportedOperationException()
}
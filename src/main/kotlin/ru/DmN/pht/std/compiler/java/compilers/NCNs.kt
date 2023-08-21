package ru.DmN.pht.std.compiler.java.compilers

import ru.DmN.pht.base.compiler.java.Compiler
import ru.DmN.pht.base.compiler.java.ctx.CompilationContext
import ru.DmN.pht.base.utils.Variable
import ru.DmN.pht.std.ast.NodeNamespace
import ru.DmN.pht.std.compiler.java.utils.global
import ru.DmN.pht.std.compiler.java.utils.with

object NCNs : IStdNodeCompiler<NodeNamespace> {
    override fun compile(node: NodeNamespace, compiler: Compiler, ctx: CompilationContext, ret: Boolean): Variable? {
        val gctx = ctx.global
        val context = ctx.with(gctx.with(gctx.name(node.name)))
        node.nodes.forEach { compiler.compile(it, context, ret) }
        return null
    }
}
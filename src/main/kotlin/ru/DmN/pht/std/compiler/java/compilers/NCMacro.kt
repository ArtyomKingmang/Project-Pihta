package ru.DmN.pht.std.compiler.java.compilers

import ru.DmN.pht.base.compiler.java.Compiler
import ru.DmN.pht.base.compiler.java.compilers.NCDefault
import ru.DmN.pht.base.compiler.java.compilers.NodeCompiler
import ru.DmN.pht.base.compiler.java.ctx.CompilationContext
import ru.DmN.pht.base.compiler.java.ctx.MacroContext
import ru.DmN.pht.base.parser.ast.Node
import ru.DmN.pht.base.utils.Variable
import ru.DmN.pht.base.utils.VirtualType
import ru.DmN.pht.std.ast.NodeDefMacro
import ru.DmN.pht.std.ast.NodeMacro

object NCMacro : NodeCompiler<NodeMacro>() {
    override fun calcType(node: NodeMacro, compiler: Compiler, ctx: CompilationContext): VirtualType? {
        val result = process(node, ctx)
        return NCDefault.calcType(result.first, compiler, result.second)
    }

    override fun compile(node: NodeMacro, compiler: Compiler, ctx: CompilationContext, ret: Boolean): Variable? {
        val result = process(node, ctx)
        return NCDefault.compile(result.first, compiler, result.second, ret)
    }

    override fun applyAnnotation(node: NodeMacro, compiler: Compiler, ctx: CompilationContext, annotation: Node) {
        val result = process(node, ctx)
        return NCDefault.applyAnnotation(result.first, compiler, result.second, annotation)
    }

    private fun process(node: NodeMacro, ctx: CompilationContext): Pair<NodeDefMacro, CompilationContext> {
        val macro = ctx.global.macros.find { it.name == node.name }!!
        val mctx = MacroContext()
        macro.args.forEachIndexed { i, it -> mctx.args[it] = node.nodes[i] }
        return Pair(macro, ctx.with(ctx.type.with(CompilationContext.Type.MACRO)).with(mctx))
    }
}
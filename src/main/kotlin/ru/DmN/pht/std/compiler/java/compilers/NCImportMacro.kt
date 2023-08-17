package ru.DmN.pht.std.compiler.java.compilers

import ru.DmN.pht.base.compiler.java.CompileStage
import ru.DmN.pht.base.compiler.java.Compiler
import ru.DmN.pht.base.compiler.java.compilers.NodeCompiler
import ru.DmN.pht.base.compiler.java.ctx.CompilationContext
import ru.DmN.pht.base.parser.ast.Node
import ru.DmN.pht.base.parser.ast.NodeNodesList
import ru.DmN.pht.base.utils.Variable

object NCImportMacro : NodeCompiler<NodeNodesList>() {
    override fun compile(node: NodeNodesList, compiler: Compiler, ctx: CompilationContext, ret: Boolean): Variable? {
        if (ctx.type == CompilationContext.Type.GLOBAL) {
            compiler.tasks[CompileStage.MACROS_DEFINE_IMPORT].add {
                compiler.compute<List<Node>>(node.nodes.first(), ctx, false).map { compiler.computeStringConst(it, ctx) }.forEach { it ->
                    val macro = it.substring(it.lastIndexOf('.') + 1)
                    val macros = compiler.macros[it.substring(0, it.lastIndexOf('.'))]!!
                    if (macro == "*")
                        ctx.global.macros += macros
                    else ctx.global.macros += macros.find { it.name == macro }!!
                }
            }
        }
        return null
    }
}
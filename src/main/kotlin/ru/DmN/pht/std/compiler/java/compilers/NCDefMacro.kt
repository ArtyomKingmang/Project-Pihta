package ru.DmN.pht.std.compiler.java.compilers

import ru.DmN.pht.base.compiler.java.CompileStage
import ru.DmN.pht.base.compiler.java.Compiler
import ru.DmN.pht.base.compiler.java.compilers.NodeCompiler
import ru.DmN.pht.base.compiler.java.ctx.CompilationContext
import ru.DmN.pht.base.parser.ast.Node
import ru.DmN.pht.base.parser.ast.NodeNodesList
import ru.DmN.pht.base.utils.Variable
import ru.DmN.pht.std.ast.NodeDefMacro

object NCDefMacro : NodeCompiler<NodeNodesList>() {
    override fun compile(node: NodeNodesList, compiler: Compiler, ctx: CompilationContext, ret: Boolean): Variable? {
        if (ctx.type == CompilationContext.Type.GLOBAL) {
            compiler.tasks[CompileStage.MACROS_DEFINE_IMPORT].add {
                val nodes = node.nodes.map { { name: Boolean -> compiler.compute<Any?>(it, ctx, name) } }
                val macro = NodeDefMacro(
                    node.tkOperation,
                    nodes[0](true) as String,
                    (nodes[1](false) as List<Node>).map { compiler.computeStringConst(it, ctx) },
                    nodes.drop(2).map { it(true) as Node }.toMutableList()
                )
                ctx.global.macros += macro
                compiler.macros.getOrPut(ctx.global.namespace) { ArrayList() } += macro
            }
        }
        return null
    }
}
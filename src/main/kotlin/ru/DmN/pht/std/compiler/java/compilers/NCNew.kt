package ru.DmN.pht.std.compiler.java.compilers

import org.objectweb.asm.Opcodes
import ru.DmN.pht.base.compiler.java.Compiler
import ru.DmN.pht.base.compiler.java.compilers.NodeCompiler
import ru.DmN.pht.base.compiler.java.ctx.CompilationContext
import ru.DmN.pht.base.utils.Variable
import ru.DmN.pht.base.utils.VirtualType
import ru.DmN.pht.base.parser.ast.NodeNodesList

object NCNew : NodeCompiler<NodeNodesList>() {
    override fun calc(node: NodeNodesList, compiler: Compiler, ctx: CompilationContext): VirtualType? =
        if (ctx.type.method)
            ctx.global.getType(compiler, compiler.computeStringConst(node.nodes.first(), ctx))
        else null

    override fun compile(node: NodeNodesList, compiler: Compiler, ctx: CompilationContext, ret: Boolean): Variable? =
        if (ctx.type.method) {
            ctx.method!!.node.run {
                val type = ctx.global.getType(compiler, compiler.computeStringConst(node.nodes.first(), ctx))
                visitTypeInsn(Opcodes.NEW, type.className)
                visitInsn(Opcodes.DUP)
                NCMethodCall.compileWithOutRet(
                    compiler,
                    ctx,
                    type,
                    "<init>",
                    node.nodes.drop(1),
                    {},
                    enumCtor = false,
                    special = true
                )
                if (ret)
                    Variable("tmp$${node.hashCode()}", type.name, -1, true)
                else {
                    visitInsn(Opcodes.POP)
                    null
                }
            }
        } else null
}
package ru.DmN.pht.std.compilers

import org.objectweb.asm.Opcodes
import ru.DmN.pht.base.compiler.java.Compiler
import ru.DmN.pht.base.compiler.java.compilers.INodeCompiler
import ru.DmN.pht.base.compiler.java.utils.CompilationContext
import ru.DmN.pht.base.utils.Variable
import ru.DmN.pht.std.ast.NodeNew
import ru.DmN.pht.std.compiler.java.utils.load
import ru.DmN.pht.std.compiler.java.utils.method

object NCNew : INodeCompiler<NodeNew> {
    override fun compileVal(node: NodeNew, compiler: Compiler, ctx: CompilationContext): Variable {
        node.ctor.run {
            val type = declaringClass!!
            val desc = desc
            ctx.method.node.run {
                visitTypeInsn(Opcodes.NEW, type.className)
                visitInsn(Opcodes.DUP)
                node.nodes.forEach { load(compiler.compileVal(it, ctx), this) }
                visitMethodInsn(
                    Opcodes.INVOKESPECIAL,
                    type.className,
                    "<init>",
                    desc,
                    false
                )
            }
            return Variable.tmp(node, type)
        }
    }
}
package ru.DmN.pht.std.compilers

import org.objectweb.asm.Opcodes
import ru.DmN.pht.base.compiler.java.Compiler
import ru.DmN.pht.base.compiler.java.compilers.INodeCompiler
import ru.DmN.pht.base.compiler.java.utils.CompilationContext
import ru.DmN.pht.base.utils.Variable
import ru.DmN.pht.std.compiler.java.utils.body
import ru.DmN.pht.std.compiler.java.utils.clazz
import ru.DmN.pht.std.compiler.java.utils.method
import ru.DmN.pht.std.ast.NodeGetA

object NCGetA : INodeCompiler<NodeGetA> {
    override fun compileVal(node: NodeGetA, compiler: Compiler, ctx: CompilationContext): Variable =
        when (node.type) {
            NodeGetA.Type.UNKNOWN -> throw RuntimeException()
            NodeGetA.Type.VARIABLE -> ctx.body[node.name]!!
            NodeGetA.Type.THIS_FIELD, NodeGetA.Type.THIS_STATIC_FIELD -> ctx.method.node.run {
                val clazz = ctx.clazz.clazz
                val field = clazz.fields.find { it.name == node.name }!!
                visitFieldInsn(
                    if (node.type == NodeGetA.Type.THIS_FIELD) Opcodes.GETFIELD else Opcodes.GETSTATIC,
                    clazz.className,
                    node.name,
                    field.desc
                )
                Variable(node.name, field.type, -1, true)
            }
        }
}
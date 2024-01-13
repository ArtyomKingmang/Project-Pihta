package ru.DmN.pht.std.compiler.java.compilers

import org.objectweb.asm.Opcodes
import ru.DmN.siberia.Compiler
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.utils.Variable
import ru.DmN.pht.std.compiler.java.utils.body
import ru.DmN.pht.std.compiler.java.utils.clazz
import ru.DmN.pht.std.compiler.java.utils.method
import ru.DmN.pht.std.ast.NodeGet
import ru.DmN.pht.std.ast.NodeGet.Type.*
import ru.DmN.pht.std.processor.utils.classes

object NCGetA : INodeCompiler<NodeGet> {
    override fun compileVal(node: NodeGet, compiler: Compiler, ctx: CompilationContext): Variable =
        when (node.type) {
            VARIABLE -> ctx.body[node.name]!!
            THIS_FIELD -> ctx.method.node.run {
                visitVarInsn(Opcodes.ALOAD, 0)
                val field = ctx.clazz.clazz.fields.find { it.name == node.name }!!
                visitFieldInsn(
                    Opcodes.GETFIELD,
                    field.declaringClass!!.className,
                    node.name,
                    field.desc
                )
                Variable(node.name, field.type, -1, true)
            }
            THIS_STATIC_FIELD -> ctx.method.node.run {
                val field = ctx.clazz.clazz.fields.find { it.name == node.name } ?: ctx.classes.asSequence().map { it -> it.fields.find { it.name == node.name } }.first()!!
                visitFieldInsn(
                    Opcodes.GETSTATIC,
                    field.declaringClass!!.className,
                    node.name,
                    field.desc
                )
                Variable(node.name, field.type, -1, true)
            }
        }
}
package ru.DmN.pht.std.utils

import org.objectweb.asm.Opcodes
import org.objectweb.asm.tree.MethodNode
import ru.DmN.pht.base.utils.Variable
import ru.DmN.pht.base.utils.VirtualField
import ru.DmN.pht.base.utils.VirtualType
import ru.DmN.pht.std.compiler.java.utils.load

abstract class NVC {
    abstract val name: String
    abstract val type: VirtualType

    abstract fun load(node: MethodNode, i: Int = -1)

    companion object {
        fun of(variable: Variable) =
            VarImpl(variable)
        fun of(field: VirtualField) =
            FieldImpl(field)
    }


    class VarImpl(private val variable: Variable) : NVC() {
        override val name: String
            get() = variable.name
        override val type: VirtualType
            get() = variable.type()

        override fun load(node: MethodNode, i: Int) {
            load(if (i == -1) variable else variable.copy(id = i), node)
        }
    }

    class FieldImpl(private val field: VirtualField) : NVC() {
        override val name: String
            get() = this.field.name
        override val type: VirtualType
            get() = this.field.type

        override fun load(node: MethodNode, i: Int) {
            node.visitFieldInsn(if (field.static) Opcodes.GETSTATIC else Opcodes.GETFIELD, field.declaringClass!!.className, field.name, field.desc)
        }
    }
}
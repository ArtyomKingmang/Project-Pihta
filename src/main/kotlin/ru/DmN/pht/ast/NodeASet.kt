package ru.DmN.pht.std.ast

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.node.INodeInfo

class NodeASet(info: INodeInfo, val arr: Node, val index: Node, val value: Node) : Node(info) {
//    override fun print(builder: StringBuilder, indent: Int): StringBuilder =
//        value.print(index.print(arr.print(builder.indent(indent).append('[').append(text).append('\n'), indent + 1).append('\n'), indent + 1).append('\n'), indent + 1).append('\n').indent(indent).append(']')
}
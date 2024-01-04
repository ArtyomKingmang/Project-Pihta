package ru.DmN.pht.std.ast

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.node.INodeInfo
import java.util.concurrent.atomic.AtomicReference

class NodeLazySymbol(info: INodeInfo, nodes: MutableList<Node>, val symbol: AtomicReference<String?>) : NodeNodesList(info, nodes) {
    override fun copy(): NodeLazySymbol =
        NodeLazySymbol(info, copyNodes(), symbol)
}
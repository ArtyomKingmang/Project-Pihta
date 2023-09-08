package ru.DmN.pht.base

import ru.DmN.pht.base.compiler.java.compilers.NCDefault
import ru.DmN.pht.base.compiler.java.compilers.NCUse
import ru.DmN.pht.base.compiler.java.compilers.NCUseCtx
import ru.DmN.pht.base.parser.parsers.NPNodesList
import ru.DmN.pht.base.parser.parsers.NPUse
import ru.DmN.pht.base.parser.parsers.NPUseCtx
import ru.DmN.pht.base.processor.processors.NRDefault
import ru.DmN.pht.base.processor.processors.NRUse
import ru.DmN.pht.base.unparser.unparsers.NUNodesList
import ru.DmN.pht.base.unparser.unparsers.NUUse
import ru.DmN.pht.base.unparser.unparsers.NUUseCtx
import ru.DmN.pht.base.utils.Module

object Base : Module("base") {
    init {
        add("use-ctx",  NPUseCtx,   NUUseCtx,   NRUse,      NCUseCtx)
        add("use",      NPUse,      NUUse,      NRUse,      NCUse)
        add("progn",    NPNodesList,NUNodesList,NRDefault,  NCDefault)
    }
}
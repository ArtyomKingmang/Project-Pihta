package ru.DmN.pht.std.module

import ru.DmN.pht.base.utils.Module
import ru.DmN.pht.std.module.parsers.NPArgument
import ru.DmN.pht.std.module.parsers.NPValue
import ru.DmN.pht.std.module.parsers.NPValueList

object StdModuleHelper : Module("std/module/helper") {
    init {
        add("name|version|files|deps|author".toRegex(), NPArgument)
        add("valn!",    NPValueList)
        add("value!",   NPValue)
    }
}
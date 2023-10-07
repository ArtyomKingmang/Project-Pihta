package ru.DmN.pht.base.utils

interface IContextCollection<T : IContextCollection<T>> {
    val contexts: MutableMap<String, Any?>

    fun with(name: String, ctx: Any?): T
}
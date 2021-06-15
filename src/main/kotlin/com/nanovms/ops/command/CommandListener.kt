package com.nanovms.ops.command

open class CommandListener {
    open fun started(cmd: Command) {}
    open fun terminated(cmd: Command) {}
}
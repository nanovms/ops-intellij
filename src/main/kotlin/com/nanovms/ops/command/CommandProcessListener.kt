package com.nanovms.ops.command

import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.util.Key
import com.nanovms.ops.Service

class CommandProcessListener(private val command: Command): ProcessAdapter() {

    override fun onTextAvailable(event: ProcessEvent, outputType: Key<*>) {}

    override fun startNotified(event: ProcessEvent) {
        command.processHandler?.let {
            val ops = service<Service>()
            ops.println(command.project, "\n\n[exec] ${it.commandLine}")
        }
        command.listener?.started(CommandEvent(event))
    }

    override fun processTerminated(event: ProcessEvent) {
        event.text?.let {
            val ops = service<Service>()
            ops.println(command.project, event.text)
        }
        command.listener?.terminated(CommandEvent(event))
    }
}
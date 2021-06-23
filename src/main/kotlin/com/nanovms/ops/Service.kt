package com.nanovms.ops

import com.intellij.execution.process.OSProcessHandler
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.nanovms.ops.command.Command

interface Service: Disposable {
    fun listImages(): Array<String>
    fun listInstances(): Array<String>
    val isInstalled: Boolean

    fun holdCommand(command: Command);
    fun releaseCommand(command: Command)
    fun runningExecutables(): Collection<Command>

    val settings: Settings

    fun println(project: Project, text: String)
    fun print(project: Project, text: String)

    fun execute(vararg args: String): OSProcessHandler
}
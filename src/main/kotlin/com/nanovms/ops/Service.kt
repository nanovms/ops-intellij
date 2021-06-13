package com.nanovms.ops

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.nanovms.ops.command.Command

interface Service: Disposable {
    val applications: Array<Application>
    val hasImages: Boolean
    fun listImages(): Array<String>
    val hasInstances: Boolean
    fun listInstances(): Array<String>
    val isInstalled: Boolean

    fun holdCommand(command: Command);
    fun releaseCommand(command: Command)
    fun runningExecutables(): Collection<Command>

    fun println(project: Project, vararg texts: String)
}
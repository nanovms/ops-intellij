package com.nanovms.ops

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

interface OpsService: Disposable {
    fun runExecutable(filepath: String, configpath: String? = null): OpsResult<String>
    fun runSource(file: VirtualFile): OpsResult<String>
    fun stop(app: Application)
    fun build(filepath: String): OpsResult<String>
    fun startInstance(imageName: String): OpsResult<String>
    fun stopInstance(name: String): OpsResult<String>

    val applications: Array<Application>
    val hasImages: Boolean
    fun listImages(): Array<String>
    val hasInstances: Boolean
    fun listInstances(): Array<String>
    val isInstalled: Boolean

    fun holdCommand(command: Command);
    fun releaseCommand(command: Command)

    fun println(project: Project, vararg texts: String)
}
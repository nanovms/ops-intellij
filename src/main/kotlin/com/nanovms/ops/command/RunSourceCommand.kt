package com.nanovms.ops.command

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.io.File

class RunSourceCommand(project: Project, private val file: VirtualFile) : Command(project) {
    override fun createArguments(): Collection<String> {
        when (file.extension) {
            "js" -> {
                return listOf("pkg", "load", "node_v14.2.0", "-a", file.path)
            }
            "cpp" -> {
                val binpath = file.parent.path + File.pathSeparatorChar + file.name
                return listOf("g++", file.path, "-o", binpath)
            }
        }
        throw UnsupportedSourceType(file)
    }

    init {
        type = CommandType.RunExecutable
    }
}
package com.nanovms.ops.command

import com.intellij.openapi.project.Project
import java.nio.file.Paths

class BuildCommand(project: Project, private val filepath: String) : Command(project) {
    override fun createArguments(): Collection<String> {
        return mutableListOf("build", filepath)
    }

    init {
        val imagePath = Paths.get(filepath)
        name = "${imagePath.fileName}.img"
    }
}
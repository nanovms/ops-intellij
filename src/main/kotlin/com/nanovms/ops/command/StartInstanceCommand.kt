package com.nanovms.ops.command

import com.intellij.openapi.project.Project

class StartInstanceCommand(project: Project, private val imageName: String): Command(project) {
    override fun createArguments(): Collection<String> {
        return listOf("instance", "create", imageName)
    }

    init {
        name = imageName
    }
}
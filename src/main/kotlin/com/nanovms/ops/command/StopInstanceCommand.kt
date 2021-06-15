package com.nanovms.ops.command

import com.intellij.openapi.project.Project

class StopInstanceCommand(project: Project, private val instanceName: String) : Command(project) {
    override fun createArguments(): Collection<String> {
        return listOf("instance", "delete", instanceName)
    }

}
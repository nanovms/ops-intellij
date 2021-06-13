package com.nanovms.ops.action

import com.intellij.openapi.project.Project
import com.nanovms.ops.Command

class BuildCommand(project: Project, private val filepath: String): Command(project) {
    override fun createArguments(): Collection<String> {
        return mutableListOf("build", filepath)
    }
}
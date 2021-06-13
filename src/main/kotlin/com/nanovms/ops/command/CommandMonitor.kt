package com.nanovms.ops.command

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.nanovms.ops.Service
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandMonitor(private val project: Project, private val command: Command): Thread() {
    override fun run() {
        val ops = service<Service>()
        val reader = BufferedReader(InputStreamReader(command.inputStream))
        var line: String?
        while(command.isAlive) {
            line = reader.readLine()
            line?.let {
                ops.println(project, it)
            }
        }

        line = reader.readLine()
        while(line != null) {
            ops.println(project, line)
            line = reader.readLine()
        }

        ops.releaseCommand(command)
    }
}
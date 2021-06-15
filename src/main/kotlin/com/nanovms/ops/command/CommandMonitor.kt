package com.nanovms.ops.command

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.nanovms.ops.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class CommandMonitor(private val project: Project, private val command: Command) : Thread() {
    override fun run() {
        val ops = service<Service>()
        val reader = BufferedReader(InputStreamReader(command.inputStream))
        var line = reader.readLine()
        while (command.isAlive || (line != null)) {
            line = reader.readLine()
            line?.let {
                ops.println(project, it)
            }
        }

        ops.releaseCommand(command)
    }
}
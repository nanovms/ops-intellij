package com.nanovms.ops

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import java.io.BufferedReader
import java.io.InputStreamReader

class CommandMonitor(private val project: Project, private val command: Command): Thread() {
    override fun run() {
        command.processHandler?.let {
            val ops = service<OpsService>()
            val process = it.process
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            var line: String?
            while(process.isAlive) {
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
}
package com.nanovms.ops.command

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.nanovms.ops.Service
import kotlinx.coroutines.delay
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class CommandMonitor(private val project: Project, private val command: Command) : Thread() {
    override fun run() {
        val ops = service<Service>()
        val reader = BufferedReader(InputStreamReader(command.inputStream))
        val errReader = BufferedReader(InputStreamReader(command.errorStream))
        var line: String?
        while (command.isAlive) {
            try {
                if(reader.ready()) {
                    line = reader.readLine()
                    line?.let {
                        ops.println(project, it)
                    }
                }

                if(errReader.ready()) {
                    line = errReader.readLine()
                    line?.let {
                        ops.println(project, it)
                    }
                }
            } catch (ex: IOException) {
                // At this point stream already closed by the process,
                // but live flag is not updated yet, so just ignore ..
                // and the loop will break by itself
            }
        }
        ops.releaseCommand(command)
    }
}
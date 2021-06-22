package com.nanovms.ops

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.nanovms.ops.command.Command
import com.nanovms.ops.command.CommandType
import com.nanovms.ops.ui.ToolWindowFactory
import okhttp3.internal.toImmutableList
import java.io.*

class ServiceDefault() : Service {
    override fun hasImages(): Boolean {
        val proc = execute("image", "list")
        if (proc.waitFor() != 0) {
            throw Exception(readStream(proc.errorStream))
        }
        val output = readStream(proc.inputStream)
        val lines = output.split('\n')
        return lines.size > 3
    }

    override fun listImages(): Array<String> {
        val proc = execute("image", "list")
        if (proc.waitFor() != 0) {
            throw Exception(readStream(proc.errorStream))
        }
        return extractTableOutput(readStream(proc.inputStream), 1)
    }

    override fun hasInstances(): Boolean {
        val proc = execute("instance", "list")
        if (proc.waitFor() != 0) {
            throw Exception(readStream(proc.errorStream))
        }
        val output = readStream(proc.inputStream)
        val lines = output.split('\n')
        return lines.size > 3
    }

    override fun listInstances(): Array<String> {
        val proc = execute("instance", "list")
        if (proc.waitFor() != 0) {
            throw Exception(readStream(proc.errorStream))
        }
        return extractTableOutput(readStream(proc.inputStream), 2)
    }

    private var _isInstalled: Boolean = false
    override val isInstalled: Boolean
        get() = _isInstalled

    override fun dispose() {
        for (cmd in _commands) {
            if (cmd.isAlive) {
                cmd.stop()
            }
        }
    }

    private val _commands = mutableListOf<Command>()
    override fun holdCommand(command: Command) {
        if (command.isAlive) {
            _commands.add(command)
        }
    }

    override fun releaseCommand(command: Command) {
        _commands.remove(command)
    }

    override fun runningExecutables(): Collection<Command> {
        val list = mutableListOf<Command>()
        for (cmd in _commands) {
            if ((cmd.type == CommandType.RunExecutable) && cmd.isAlive) {
                list.add(cmd)
            }
        }
        return list.toImmutableList()
    }

    override val settings = service<Settings>()

    override fun println(project: Project, vararg texts: String) {
        ToolWindowFactory.println(project, texts.joinToString(" "))
    }

    init {
        var file = File(homeDir() + File.separatorChar + "bin" + File.separatorChar + "ops")
        if (file.exists()) {
            _isInstalled = true
        } else {
            val pathVars = System.getenv("PATH").split(File.pathSeparator)
            for (path in pathVars) {
                file = File(path + File.separatorChar + "ops")
                if (file.exists()) {
                    _isInstalled = true
                    break
                }
            }
        }
    }

    private fun execute(vararg args: String): Process {
        var cmd = homeDir() + File.separatorChar + "bin" + File.separatorChar + "ops"
        val file = File(cmd)
        if (!file.exists()) {
            cmd = "ops"
        }
        for (arg in args) {
            cmd += " $arg"
        }

        val pathList = settings.getMergedPaths()
        println("[OPS] Execute \"${cmd}\" with PATH=${pathList}")
        return Runtime.getRuntime().exec(cmd, arrayOf("PATH=${pathList}"))
    }

    private fun readStream(input: InputStream): String {
        val reader = BufferedReader(InputStreamReader(input))
        var output: String
        try {
            output = reader.readText()
        } finally {
            reader.close()
        }
        return output.trim()
    }

    private fun homeDir(): String {
        val userHomeDir = System.getProperty("user.home")
        return userHomeDir + File.separatorChar + ".ops"
    }

    private fun extractTableOutput(output: String, colIndex: Int): Array<String> {
        val rows = mutableListOf<String>()
        val lines = output.split('\n')
        if (lines.size === 3) {
            return arrayOf() // no image listed
        }

        var columns: List<String>
        var trimmedLine: String
        var value: String
        for (line in lines) {
            trimmedLine = line.trim()
            if (line.startsWith('+') || line.startsWith('-')) {
                continue
            }

            if (trimmedLine.isBlank()) {
                continue
            }

            columns = trimmedLine.split('|')
            value = columns[colIndex].trim()
            if (value.toLowerCase().contains("name")) {
                continue
            }
            rows.add(value)
        }
        return rows.toTypedArray();
    }
}
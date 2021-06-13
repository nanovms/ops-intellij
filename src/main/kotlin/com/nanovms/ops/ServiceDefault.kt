package com.nanovms.ops

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.nanovms.ops.command.Command
import com.nanovms.ops.command.CommandType
import com.nanovms.ops.ui.ToolWindowFactory
import okhttp3.internal.toImmutableList
import java.io.File

class ServiceDefault() : Service {
    private val _applications = mutableListOf<Application>()
    override val applications: Array<Application>
        get() = _applications.toTypedArray()

    override val hasImages: Boolean
        get() {
            val proc = execute("image", "list")
            if (!proc.waitFor()) {
                throw Exception(proc.errorOutput)
            }
            val output = proc.output.trim()
            val lines = output.split('\n')
            return lines.size > 3
        }

    override fun listImages(): Array<String> {
        val proc = execute("image", "list")
        if (!proc.waitFor()) {
            throw Exception(proc.errorOutput)
        }
        return extractTableOutput(proc.output.trim(), 1)
    }

    override val hasInstances: Boolean
        get() {
            val proc = execute("instance", "list")
            if (!proc.waitFor()) {
                throw Exception(proc.errorOutput)
            }
            val output = proc.output.trim()
            val lines = output.split('\n')
            return lines.size > 3
        }

    override fun listInstances(): Array<String> {
        val proc = execute("instance", "list")
        if (!proc.waitFor()) {
            throw Exception(proc.errorOutput)
        }
        return extractTableOutput(proc.output.trim(), 2)
    }

    private var _isInstalled: Boolean = false

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

    override val isInstalled: Boolean
        get() = _isInstalled

    override fun dispose() {
        for (app in _applications) {
            app.terminate()
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

    override fun println(project: Project, vararg texts: String) {
        ToolWindowFactory.println(project, texts.joinToString(" "))
    }

    private fun execute(vararg args: String): OpsProcess {
        var cmd = homeDir() + File.separatorChar + "bin" + File.separatorChar + "ops"
        val file = File(cmd)
        if (!file.exists()) {
            cmd = "ops"
        }
        for (arg in args) {
            cmd += " $arg"
        }
        return OpsProcess(Runtime.getRuntime().exec(cmd))
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
package com.nanovms.ops.command

import com.intellij.openapi.project.Project
import okhttp3.internal.toImmutableList
import java.nio.file.Paths

class RunCommand(project: Project,
                 private val filepath: String,
                 private val configpath: String? = null): Command(project) {

    override fun createArguments(): Collection<String> {
        var args = mutableListOf<String>()
        if (filepath.endsWith(".js")) {
            args.addAll(listOf("pkg", "load", "node_v14.2.0", "-a"))
        } else {
            args.add("run")
        }
        args.add(filepath)

        configpath?.let {
            args.addAll(listOf("-c", it))
        }
        return args.toImmutableList()
   }

    init {
        val path = Paths.get(filepath)
        super.name = path.fileName.toString()
    }
}
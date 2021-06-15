package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.nanovms.ops.Log
import com.nanovms.ops.command.Command
import com.nanovms.ops.command.CommandListener
import com.nanovms.ops.command.RunCommand

class RunWithConfigAction : BaseAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val descriptor = FileChooserDescriptor(true, false, true, false, false, false)
            descriptor.title = "Select ELF or Javascript File"
            var selectedFiles = FileChooser.chooseFiles(descriptor, it, null)
            if (selectedFiles.isNotEmpty()) {
                val filepath = selectedFiles[0].path

                descriptor.title = "Select Configuration File"
                selectedFiles = FileChooser.chooseFiles(descriptor, it, null)

                RunCommand(it, filepath, selectedFiles[0].path).withListener(
                    object : CommandListener() {
                        override fun terminated(cmd: Command) {
                            Log.notifyInfo("[${cmd.pid}] ${cmd.name} terminated")
                        }
                    }
                ).execute()
            }
        }
    }
}
package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.nanovms.ops.command.CommandListener
import com.nanovms.ops.Log
import com.nanovms.ops.command.BuildCommand
import com.nanovms.ops.command.Command

class BuildAction : BaseAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val descriptor = FileChooserDescriptor(true, false, true, false, false, false)
            val selectedFiles = FileChooser.chooseFiles(descriptor, null, null)
            if (selectedFiles.isNotEmpty()) {
                val filePath = selectedFiles[0].path
                BuildCommand(it, filePath).withListener(object : CommandListener() {
                    override fun terminated(cmd: Command) {
                        if (cmd.hasError) {
                            Log.notifyError("Failed to build image for '${filePath}'")
                        } else {
                            Log.notifyInfo("Built image for '${filePath}'")
                        }
                    }
                }).execute()
            }
        }
    }
}
package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.nanovms.ops.command.CommandEvent
import com.nanovms.ops.command.CommandListener
import com.nanovms.ops.Log
import com.nanovms.ops.command.BuildCommand

class BuildAction : BaseAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val descriptor = FileChooserDescriptor(true, false, true, false, false, false)
            val selectedFiles = FileChooser.chooseFiles(descriptor, null, null)
            if (selectedFiles.isNotEmpty()) {
                val filePath = selectedFiles[0].path
                val cmd = BuildCommand(it, filePath).withListener(object: CommandListener() {
                    override fun terminated(event: CommandEvent) {
                        if(event.hasError) {
                            Log.notifyError("Failed to build image for '${filePath}'")
                        } else {
                            Log.notifyInfo("Built image for '${filePath}'")
                        }
                    }
                })
                cmd.execute()
            }
        }
    }
}
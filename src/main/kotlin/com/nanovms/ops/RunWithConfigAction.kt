package com.nanovms.ops

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor

class RunWithConfigAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val descriptor = FileChooserDescriptor(true, false, true, false, false, false)
        descriptor.title = "Select ELF or Javascript File"
        var selectedFiles = FileChooser.chooseFiles(descriptor, null, null)
        if (selectedFiles.isNotEmpty()) {
            val filepath = selectedFiles[0].path

            descriptor.title = "Select Configuration File"
            selectedFiles = FileChooser.chooseFiles(descriptor, null, null)

            val ops = service<OpsService>()
            val result = ops.runExecutable(filepath, selectedFiles[0].path)
            if (result.hasError) {
                Log.error(result.error)
                Log.notifyError("Failed to execute $filepath")
                return
            }
            Log.infoAndNotify("Executed $filepath with PID #${result.process?.pid}")
        }
    }
}
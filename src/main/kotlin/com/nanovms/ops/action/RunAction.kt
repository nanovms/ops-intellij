package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.nanovms.ops.Log
import com.nanovms.ops.Service

class RunAction : BaseAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val descriptor = FileChooserDescriptor(true, false, true, false, false, false)
        val selectedFiles = FileChooser.chooseFiles(descriptor, null, null)
        if (selectedFiles.isNotEmpty()) {
            val filepath = selectedFiles[0].path
            val ops = service<Service>()
            val result = ops.runExecutable(filepath)
            if (result.hasError) {
                Log.error(result.error)
                Log.notifyError("Failed to execute $filepath")
                return
            }
            Log.infoAndNotify("Executed $filepath with PID #${result.process?.pid}")
        }
    }
}
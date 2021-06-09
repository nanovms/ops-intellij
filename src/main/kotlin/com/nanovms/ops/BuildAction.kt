package com.nanovms.ops

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.fileChooser.FileChooser
import com.intellij.openapi.fileChooser.FileChooserDescriptor

class BuildAction : BaseAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val descriptor = FileChooserDescriptor(true, false, true, false, false, false)
        val selectedFiles = FileChooser.chooseFiles(descriptor, null, null)
        if (selectedFiles.isNotEmpty()) {
            val filePath = selectedFiles[0].path
            val ops = service<OpsService>()
            val result = ops.build(filePath)
            if(result.hasError) {
                Log.notifyError("Failed to build image for '${filePath}'")
                result.process?.let { Log.error(it.errorOutput) }
                return
            }
            Log.notifyInfo("Built image for '${filePath}'")
        }
    }
}
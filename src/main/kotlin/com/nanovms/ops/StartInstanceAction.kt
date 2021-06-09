package com.nanovms.ops

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class StartInstanceAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: OpsService): Boolean {
        return ops.hasImages
    }

    override fun actionPerformed(e: AnActionEvent) {
        val ops = service<OpsService>()
        val dialog = DropdownDialog(ops.listImages())
        val isOK = dialog.showAndGet()
        if (isOK) {
            val selectedImage = dialog.model.selectedItem as String
            val result = ops.startInstance(selectedImage)
            if (result.hasError) {
                Log.error(result.error)
                Log.notifyError("Failed to start $selectedImage")
                return
            }
            Log.notifyInfo("Started new instance of $selectedImage")
        }
    }
}
package com.nanovms.ops

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class StopInstanceAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: OpsService): Boolean {
        return ops.hasInstances
    }

    override fun actionPerformed(e: AnActionEvent) {
        val ops = service<OpsService>()
        val dialog = DropdownDialog(ops.listInstances(), null)
        val isOK = dialog.showAndGet()
        if (isOK) {
            val instanceName = dialog.model.selectedItem as String
            val result = ops.stopInstance(instanceName)
            if (result.hasError) {
                Log.error(result.error)
                Log.notifyError("Failed to stop instance '$instanceName'")
                return
            }
            dialog.model.removeElement(instanceName)
            Log.notifyInfo("Stopped instance '$instanceName'")
        }
    }
}
package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.DropdownDialog
import com.nanovms.ops.Log
import com.nanovms.ops.Service

class StopInstanceAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: Service): Boolean {
        return ops.hasInstances
    }

    override fun actionPerformed(e: AnActionEvent) {
        val ops = service<Service>()
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
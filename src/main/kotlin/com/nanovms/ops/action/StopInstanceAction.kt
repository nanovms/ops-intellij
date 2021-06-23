package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.Log
import com.nanovms.ops.Ops
import com.nanovms.ops.ui.DropdownDialog
import com.nanovms.ops.Service
import com.nanovms.ops.command.StopInstanceCommand

class StopInstanceAction : BaseAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val instances = Ops.instance.listInstances()
            if(instances.isEmpty()) {
                Log.notifyError(it, "You don't have one or more running instances. To run one, please use [Tool | NanoVMs | Start Instance] menu.")
                return
            }

            val dialog = DropdownDialog(instances, null)
            val isOK = dialog.showAndGet()
            if (isOK) {
                val instanceName = dialog.model.selectedItem as String
                StopInstanceCommand(it, instanceName).execute()
            }
        }
    }
}
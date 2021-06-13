package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.ui.DropdownDialog
import com.nanovms.ops.Log
import com.nanovms.ops.Service
import com.nanovms.ops.command.Command
import com.nanovms.ops.command.CommandListener
import com.nanovms.ops.command.StartInstanceCommand
import com.nanovms.ops.command.StopInstanceCommand

class StopInstanceAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: Service): Boolean {
        return ops.hasInstances
    }

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val ops = service<Service>()
            val dialog = DropdownDialog(ops.listInstances(), null)
            val isOK = dialog.showAndGet()
            if (isOK) {
                val instanceName = dialog.model.selectedItem as String
                val command = StopInstanceCommand(it, instanceName).withListener(
                    object: CommandListener() {
                        override fun terminated(cmd: Command) {
                            Log.notifyInfo("Stopped instance '${cmd.name}'")
                        }
                    }
                )
                command.execute()
            }
        }
    }
}
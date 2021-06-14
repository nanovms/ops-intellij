package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.ui.DropdownDialog
import com.nanovms.ops.Log
import com.nanovms.ops.Service
import com.nanovms.ops.command.Command
import com.nanovms.ops.command.CommandListener
import com.nanovms.ops.command.StartInstanceCommand

class StartInstanceAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: Service): Boolean {
        return ops.hasImages()
    }

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val ops = service<Service>()
            val dialog = DropdownDialog(ops.listImages())
            val isOK = dialog.showAndGet()
            if (isOK) {
                val selectedImage = dialog.model.selectedItem as String
                val command = StartInstanceCommand(it, selectedImage).withListener(
                    object: CommandListener() {
                        override fun started(cmd: Command) {
                            Log.notifyInfo("Started new instance of $selectedImage")
                        }
                    }
                )
                command.execute()
            }
        }
    }
}
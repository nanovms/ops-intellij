package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.ui.DropdownDialog
import com.nanovms.ops.Log
import com.nanovms.ops.Ops
import com.nanovms.ops.Service
import com.nanovms.ops.command.Command
import com.nanovms.ops.command.CommandListener
import com.nanovms.ops.command.StartInstanceCommand

class StartInstanceAction : BaseAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val images = Ops.instance.listImages()
            if(images.isEmpty()) {
                Log.notifyError(it, "You don't have one or more image to run, please create one using [Tool | NanoVMs | Build] menu.")
                return
            }

            val dialog = DropdownDialog(images)
            val isOK = dialog.showAndGet()
            if (isOK) {
                val selectedImage = dialog.model.selectedItem as String
                StartInstanceCommand(it, selectedImage).withListener(
                    object : CommandListener() {
                        override fun started(cmd: Command) {
                            Log.notifyInfo(it, "[${cmd.pid}] ${cmd.name} started")
                        }

                        override fun terminated(cmd: Command) {
                            Log.notifyInfo(it, "[${cmd.pid}] ${cmd.name} terminated")
                        }
                    }
                ).execute()
            }
        }
    }
}
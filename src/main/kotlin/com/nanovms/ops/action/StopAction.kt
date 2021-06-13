package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.Service
import com.nanovms.ops.command.Command
import com.nanovms.ops.ui.DropdownDialog
import com.nanovms.ops.ui.DropdownDialogRenderer

class StopAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: Service): Boolean {
        return ops.runningCommands().isNotEmpty()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val ops = service<Service>()
        val dialog = DropdownDialog(ops.runningCommands().toTypedArray(), DropdownDialogRenderer(::dialogRenderFunc))
        val isOK = dialog.showAndGet()
        if(isOK) {
            val cmd = dialog.model.selectedItem as Command
            cmd.stop()
        }
    }

    private fun dialogRenderFunc(value: Any?): String {
        val cmd = value as Command
        return "[${cmd.pid}] ${cmd.name}"
    }
}
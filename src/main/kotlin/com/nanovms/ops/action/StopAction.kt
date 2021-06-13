package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.*

class StopAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: Service): Boolean {
        return ops.applications.isNotEmpty()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val ops = service<Service>()
        val dialog = DropdownDialog(ops.applications, DropdownDialogRenderer(::dialogRenderFunc))
        val isOK = dialog.showAndGet()
        if(isOK) {
            val app = dialog.model.selectedItem as Application
            ops.stop(app)
            dialog.model.removeElement(app)
            Log.notifyInfo("Stopped ${app.path} with PID #${app.process.pid}")
        }
    }

    private fun dialogRenderFunc(value: Any?): String {
        val app = value as Application
        return "${app.path} (PID: ${app.process.pid})"
    }
}
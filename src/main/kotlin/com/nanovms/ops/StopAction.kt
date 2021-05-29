package com.nanovms.ops

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class StopAction : AnAction() {
    override fun update(e: AnActionEvent) {
        val ops = service<OpsService>()
        e.presentation.isEnabled = ops.applications.isNotEmpty()
    }

    override fun actionPerformed(e: AnActionEvent) {
        val ops = service<OpsService>()
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
package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.nanovms.ops.Log
import com.nanovms.ops.Service

abstract class BaseAction : AnAction() {
    companion object {
        var opsInstallNotified: Boolean = false
    }

    protected open fun isEnabled(e: AnActionEvent, ops: Service): Boolean = true

    override fun update(e: AnActionEvent) {
        val ops = service<Service>()
        e.presentation.isEnabled = ops.isInstalled && isEnabled(e, ops) && (e.project != null)
        e.project?.let {
            if (!ops.isInstalled && !opsInstallNotified) {
                Log.notifyError(
                    it,
                    """
                    OPS is not installed, please install it first then restart the IDE. For quick install, execute 'curl https://ops.city/get.sh -sSfL | sh'                    
                """
                )
                opsInstallNotified = true
            }
        }
    }
}
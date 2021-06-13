package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.nanovms.ops.Log
import com.nanovms.ops.Service

class RunOpenedFileAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: Service): Boolean {
        if (e.project == null) {
            return false
        }
        val editor = FileEditorManager.getInstance(e.project!!).selectedTextEditor
        return (editor != null)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val editor = FileEditorManager.getInstance(e.project!!).selectedTextEditor
        if (editor != null) {
            var file = FileDocumentManager.getInstance().getFile(editor.document)
            if (file != null) {
                if (file.extension == null) {
                    Log.errorAndNotify("Unknown file extension: ${file.path}")
                    return
                }

                val ops = service<Service>()
                val result = ops.runSource(file)
                if (result == null) {
                    Log.errorAndNotify("Unsupported source file: ${file.path}")
                    return
                }
                if (result.hasError) {
                    Log.error(result.error)
                    Log.notifyError("Failed to execute source file: ${file.path}")
                    return
                }
                Log.infoAndNotify("Executed ${file.path}")
            }
        }
    }
}
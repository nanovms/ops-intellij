package com.nanovms.ops

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager

class RunOpenedFileAction : AnAction() {
    override fun update(e: AnActionEvent) {
        if (e.project == null) {
            e.presentation.isEnabled = false
            return
        }

        val editor = FileEditorManager.getInstance(e.project!!).selectedTextEditor
        e.presentation.isEnabled = editor != null
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

                val ops = service<OpsService>()
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
package com.nanovms.ops.action

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.nanovms.ops.Log
import com.nanovms.ops.Service
import com.nanovms.ops.command.Command
import com.nanovms.ops.command.CommandListener
import com.nanovms.ops.command.RunSourceCommand
import com.nanovms.ops.command.UnsupportedSourceType

class RunSourceAction : BaseAction() {
    override fun isEnabled(e: AnActionEvent, ops: Service): Boolean {
        if (e.project == null) {
            return false
        }
        val editor = FileEditorManager.getInstance(e.project!!).selectedTextEditor
        return (editor != null)
    }

    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            val editor = FileEditorManager.getInstance(e.project!!).selectedTextEditor
            if (editor != null) {
                var file = FileDocumentManager.getInstance().getFile(editor.document)
                if (file != null) {
                    if (file.extension == null) {
                        Log.errorAndNotify("Unknown file extension: ${file.path}")
                        return
                    }

                    try {
                        RunSourceCommand(it, file).withListener(
                            object : CommandListener() {
                                override fun started(cmd: Command) {
                                    Log.infoAndNotify("[exec] ${file.path}")
                                }
                            }
                        ).execute()
                    } catch (ex: UnsupportedSourceType) {
                        val ops = service<Service>()
                        ex.message?.let { msg ->
                            ops.println(it, msg)
                        }
                    }
                }
            }
        }
    }
}
package com.nanovms.ops.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory

class ToolWindowFactory(): ToolWindowFactory {
    private val opsConsole = ConsolePanel()

    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(opsConsole, OpsDisplayName, false)
        toolWindow.contentManager.addContent(content)
    }

    companion object {
        private const val OpsDisplayName = "OPS"

        fun print(project: Project, text: String) {
            project?.let {
                val content = getContent(project, OpsDisplayName)
                content?.let {
                    val panel = content.component as? ConsolePanel
                    panel?.append(text)
                }
            }
        }

        private fun getContent(project: Project, displayName: String): Content? {
            val toolWindow = getWindow(project)
            toolWindow?.let {
                for (content in toolWindow.contentManager.contents) {
                    if (content.displayName == displayName) {
                        return content
                    }
                }
            }
            return null
        }

        private fun getWindow(project: Project): ToolWindow? {
            return ToolWindowManager.getInstance(project).getToolWindow("NanoVMs")
        }
    }
}
package com.nanovms.ops.ui

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.EventQueue

class ConsolePanel : BorderLayoutPanel() {
    private val textArea = JBTextArea()
    private val scrollPane =
        JBScrollPane(textArea, JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)

    init {
        textArea.isEditable = false
        addToCenter(scrollPane)
    }

    fun append(text: String) {
        EventQueue.invokeLater {
            textArea.append(stripColorCodes(text))
            val vertical = scrollPane.verticalScrollBar
            vertical.value = vertical.maximum * 2
        }
    }

    private fun stripColorCodes(text: String): String {
        var colorCodes = listOf(
            "\u001b[31m",
            "\u001b[32m",
            "\u001b[33m",
            "\u001b[34m",
            "\u001b[35m",
            "\u001b[36m",
            "\u001b[37m",
            "\u001b[0m"
        )
        var str = text
        colorCodes.forEach {
            str = str.replace(it, "", true)
        }
        return str
    }
}
package com.nanovms.ops.ui

import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.EventQueue

class ConsolePanel : BorderLayoutPanel() {
    private val textArea = JBTextArea()
    private val scrollPane = JBScrollPane(textArea, JBScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JBScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)

    init {
        textArea.isEditable = false
        addToCenter(scrollPane)
    }

    fun println(text: String) {
        EventQueue.invokeLater {
            textArea.append("$text\n")
            val vertical = scrollPane.verticalScrollBar
            vertical.value = vertical.maximum * 2
        }
    }
}
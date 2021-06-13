package com.nanovms.ops.ui

import com.intellij.ui.components.JBTextArea
import com.intellij.util.ui.components.BorderLayoutPanel

class ToolWindowConsolePanel : BorderLayoutPanel() {
    private val _textArea = JBTextArea()
    val textArea: JBTextArea
        get() = _textArea

    init {
        _textArea.isEditable = false
        addToCenter(_textArea)
    }

    fun println(text: String) {
        _textArea.append(String(text.toByteArray(), Charsets.UTF_8))
    }
}
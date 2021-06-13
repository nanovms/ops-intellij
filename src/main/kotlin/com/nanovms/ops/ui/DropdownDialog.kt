package com.nanovms.ops.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import java.awt.BorderLayout
import java.awt.Component
import javax.swing.*

class DropdownDialogRenderer(valueFunc: (value: Any?) -> String) : DefaultListCellRenderer() {
    private val valueFunc = valueFunc

    override fun getListCellRendererComponent(
        list: JList<*>?,
        value: Any?,
        index: Int,
        isSelected: Boolean,
        cellHasFocus: Boolean
    ): Component {
        return super.getListCellRendererComponent(
            list,
            valueFunc(value),
            index,
            isSelected,
            cellHasFocus
        )
    }
}

class DropdownDialog<T>(data: Array<T>, renderer: DropdownDialogRenderer? = null) : DialogWrapper(true) {
    val model = DefaultComboBoxModel(data)
    val renderer = renderer

    init {
        init()
        this.title = "Select Running Application"
    }

    override fun createCenterPanel(): JComponent? {
        val dropdown = ComboBox(model)
        if (renderer != null) {
            dropdown.renderer = renderer
        }

        val panel = JPanel(BorderLayout())
        panel.add(dropdown, BorderLayout.CENTER)
        return panel
    }
}
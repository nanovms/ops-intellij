package com.nanovms.ops.ui

import com.intellij.ui.components.JBPanel
import com.intellij.ui.components.JBTextField
import com.nanovms.ops.Ops
import java.awt.*
import javax.swing.JLabel
import javax.swing.JPanel

class ConfigurableSettingsPanel : JBPanel<ConfigurableSettingsPanel>() {
    var text: String
        get() {
            return pathTextField.text
        }
        set(value) {
            pathTextField.text = value
        }

    private var pathTextField = JBTextField()

    init {
        layout = BorderLayout()

        val rootPane = JPanel(GridLayout(2, 1))

        val pathHeaderPane = JPanel(FlowLayout(FlowLayout.LEADING, 0, 5))
        pathHeaderPane.alignmentX = LEFT_ALIGNMENT
        pathHeaderPane.add(JLabel("Following paths will be appended to runtime PATH. Use colon character (:) as separator between paths."))
        rootPane.add(pathHeaderPane)

        val cs = GridBagConstraints()
        cs.fill = GridBagConstraints.HORIZONTAL
        cs.ipadx = 10
        val pathFormPane = JPanel(GridBagLayout())
        pathFormPane.add(JLabel("PATH: "), cs)

        cs.gridx = 1
        cs.weightx = 1.0
        pathFormPane.add(pathTextField, cs)
        rootPane.add(pathFormPane)

        add(rootPane, BorderLayout.PAGE_START)

        text = Ops.instance.settings.additionalPaths
    }
}
package com.nanovms.ops.ui

import com.intellij.openapi.options.Configurable
import com.nanovms.ops.Ops
import javax.swing.JComponent

class ConfigurableSettings: Configurable {
    override fun createComponent(): JComponent? {
        return panel
    }

    override fun isModified(): Boolean {
        return panel.text != Ops.instance.settings.additionalPaths
    }

    override fun apply() {
        val settings = Ops.instance.settings
        settings.additionalPaths = panel.text
    }

    override fun getDisplayName(): String {
        return "NanoVMs"
    }

    private val panel = ConfigurableSettingsPanel()
}
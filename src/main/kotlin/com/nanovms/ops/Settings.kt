package com.nanovms.ops

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import java.io.File

@State(
    name = "com.nanovms.ops.Settings",
    storages = [Storage("nanovms-ops.xml")]
)
class Settings: PersistentStateComponent<Settings> {
    var additionalPaths = ""

    fun getMergedPaths(): String {
        var merged = systemPaths
        if(merged.endsWith(File.pathSeparatorChar)) {
            return systemPaths + additionalPaths
        }
        return systemPaths + File.pathSeparator + additionalPaths
    }

    override fun getState(): Settings? {
        return this
    }

    override fun loadState(state: Settings) {
        XmlSerializerUtil.copyBean(state, this)
    }

    private val systemPaths = System.getenv("PATH")
}
package com.nanovms.ops

import com.intellij.openapi.components.service

object Ops {
    val instance by lazy(LazyThreadSafetyMode.PUBLICATION) {
        service<Service>()
    }
}
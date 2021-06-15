package com.nanovms.ops.command

import com.intellij.openapi.vfs.VirtualFile

class UnsupportedSourceType(file: VirtualFile) : Exception("Unsupported source type: ${file.extension}") {}
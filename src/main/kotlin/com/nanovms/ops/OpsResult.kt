package com.nanovms.ops

class OpsResult<T>(data: T, proc: OpsProcess? = null) {
    var process: OpsProcess? = proc
    val data = data

    var _error: String? = null
    val error: String
        get() {
            if (_error != null) {
                return _error!!
            }

            if (process != null) {
                return process!!.errorOutput
            }
            return ""
        }

    val hasError: Boolean
        get() {
            if ((_error == null) || (process == null) || (!process!!.hasError)) {
                return false
            }
            return true
        }

    fun withProcess(proc: Process): OpsResult<T> {
        process = OpsProcess(proc)
        return this
    }

    fun withError(message: String): OpsResult<T> {
        _error = message
        return this
    }
}
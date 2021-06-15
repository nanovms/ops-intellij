package com.nanovms.ops

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

object Log {
    fun infoAndNotify(project: Project, message: String) {
        info(project, message)
        notifyInfo(project, message)
    }

    fun errorAndNotify(project: Project, message: String) {
        error(project, message)
        notifyError(project, message)
    }

    fun notifyInfo(project: Project, message: String) {
        notify(project, message, NotificationType.INFORMATION)
    }

    fun notifyError(project: Project, message: String) {
        notify(project, message, NotificationType.ERROR)
    }

    private fun error(project: Project, message: String) {
        logger.error("[NanoVMs] ERR: $message")
        ops.println(project, "ERROR: $message")
    }

    private fun info(project: Project, message: String) {
        logger.info("[NanoVMs]: $message")
        ops.println(project, "INFO: $message")
    }

    private fun notify(project: Project, message: String, type: NotificationType) {
        var notif = notificationGroup.createNotification(message, type)
        notif.notify(project)
    }

    private val ops = service<Service>()
    private val logger = Logger.getInstance("NanoVMs")
    private val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("NanoVMs")
}
package com.nanovms.ops

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager

class Log {
    companion object {
        private val logger = Logger.getInstance("NanoVMs")
        private val notificationGroup = NotificationGroupManager.getInstance().getNotificationGroup("NanoVMs")

        fun info(message: String) {
            val msg = "[NanoVMs]: $message"
            logger.info(msg)
            println(msg)
        }

        fun error(message: String) {
            val msg = "[NanoVMs] ERR: $message"
            logger.error(msg)
            println(msg)
        }

        fun error(err: Throwable?) {
            val msg = "[NanoVMs] ERR: $err"
            logger.error(msg)
            println(msg)
        }

        fun infoAndNotify(message: String) {
            info(message)
            notifyInfo(message)
        }

        fun errorAndNotify(message: String) {
            error(message)
            notifyError(message)
        }

        fun errorAndNotify(message: String, err: Throwable?) {
            error("$message: $err")
            notifyError(message)
        }

        fun notifyInfo(message: String) {
            notify(message, NotificationType.INFORMATION)
        }

        fun notifyError(message: String) {
            notify(message, NotificationType.ERROR)
        }

        private fun notify(message: String, type: NotificationType) {
            var notif = notificationGroup.createNotification(message, type)
            val projects = ProjectManager.getInstance().openProjects
            var project: Project? = null
            if (projects.isNotEmpty()) {
                project = projects.first()
            }
            notif.notify(project)
        }
    }
}
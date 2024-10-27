package com.liubs.visual.classbytes.util;

/**
 * @author Liubsyy
 * @date 2024/5/9
 */

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationGroupManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.ui.MessageType;

public class NoticeInfo {
    private static NotificationGroup notificationGroup = NotificationGroupManager.getInstance()
            .getNotificationGroup("VisualClassBytesNoticeGroup");


    public static void info(String message){
        Notification notification = notificationGroup.createNotification(message, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }
    public static void info(String message,Object ...param){
        if(message == null || message.isEmpty()) {
            return;
        }
        message = String.format(message,param);
        Notification notification = notificationGroup.createNotification(message, MessageType.INFO);
        Notifications.Bus.notify(notification);
    }
    public static void errorWithoutFormat(String message){
        Notification notification = notificationGroup.createNotification(message, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }
    public static void error(String message){
        Notification notification = notificationGroup.createNotification(message, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }

    public static void error(String message,Object ...param){
        if(message == null || message.isEmpty()) {
            return;
        }
        message = String.format(message,param);
        Notification notification = notificationGroup.createNotification(message, MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }

    public static void warning(String message){
        Notification notification = notificationGroup.createNotification(message, MessageType.WARNING);
        Notifications.Bus.notify(notification);
    }
    public static void warning(String message,Object ...param){
        if(message == null || message.isEmpty()) {
            return;
        }
        message = String.format(message,param);
        Notification notification = notificationGroup.createNotification(message, MessageType.WARNING);
        Notifications.Bus.notify(notification);
    }

    public static void auto(String message,boolean enable){
        Notification notification = notificationGroup.createNotification(message, enable ? MessageType.INFO: MessageType.ERROR);
        Notifications.Bus.notify(notification);
    }


}

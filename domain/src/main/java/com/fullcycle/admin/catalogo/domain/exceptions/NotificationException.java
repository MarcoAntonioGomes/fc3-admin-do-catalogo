package com.fullcycle.admin.catalogo.domain.exceptions;

import com.fullcycle.admin.catalogo.domain.handler.Notification;

public class NotificationException extends DomainException {
    public NotificationException(String aMessage, Notification notification) {
         super(aMessage, notification.getErrors());
    }
}

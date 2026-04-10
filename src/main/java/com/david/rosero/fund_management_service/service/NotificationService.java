package com.david.rosero.fund_management_service.service;

import com.david.rosero.fund_management_service.model.NotificationChannel;

public interface NotificationService {
    void notify(String destination, NotificationChannel channel, String message);
}

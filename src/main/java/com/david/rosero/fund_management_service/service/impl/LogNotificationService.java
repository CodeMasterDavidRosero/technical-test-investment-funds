package com.david.rosero.fund_management_service.service.impl;

import com.david.rosero.fund_management_service.model.NotificationChannel;
import com.david.rosero.fund_management_service.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LogNotificationService implements NotificationService {
    @Override
    public void notify(String destination, NotificationChannel channel, String message) {
        log.info("Sending {} notification to {}: {}", channel, destination, message);
    }
}

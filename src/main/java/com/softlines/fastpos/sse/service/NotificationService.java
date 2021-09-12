package com.softlines.fastpos.sse.service;


import com.softlines.fastpos.sse.model.EventDto;

import java.io.IOException;

public interface NotificationService {

    void sendNotification(String  identifier, EventDto event) throws IOException;
    void sendNotificationForAll(EventDto event,String  senderId) throws IOException;
}

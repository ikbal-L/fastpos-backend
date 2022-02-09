package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class NotificationService {
    @Autowired
    private  SimpMessagingTemplate simpMessagingTemplate;
    private final Map<Object,String> publishers = new HashMap<>();

    public void registerPublisher(Object publisher,String channel){
        publishers.put(publisher,channel);
    }
    public void publish(Object publisher, Message message){
        var channel = publishers.get(publisher);
        simpMessagingTemplate.setDefaultDestination("/app");
        simpMessagingTemplate.setUserDestinationPrefix("/app");
        simpMessagingTemplate.convertAndSend(channel,message);
    }


    public void sendUnlockOrderMessage(String sessionId,List<Long> ids) {
        if (ids.isEmpty()) return;
        simpMessagingTemplate.setDefaultDestination("/app");
        simpMessagingTemplate.setUserDestinationPrefix("/app");

        var message = Message.builder()
                .type("Unlock.Order")
                .content(ids)
                .source(sessionId)
                .build();
        simpMessagingTemplate.convertAndSend("/topic/messages/locks",message);

    }
}

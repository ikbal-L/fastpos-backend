package com.softlines.fastpos.service;

import com.softlines.fastpos.domain.Order;
import com.softlines.fastpos.dto.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

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
        simpMessagingTemplate.setDefaultDestination("/app");
        simpMessagingTemplate.setUserDestinationPrefix("/app");

//
        simpMessagingTemplate.convertAndSend("/topic/unlock",ids);

    }
}

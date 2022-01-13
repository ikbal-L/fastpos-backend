package com.softlines.fastpos.socket;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
public class ChatController {

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public String send(@Payload String data) {
        return data;
    }

//    @MessageMapping("/session")
//    @SendTo("/queue/{user_id}")
//    public Message sendSessionId(Message message) {
//        GenericMessage connectHeader = (GenericMessage) message
//                .getHeader(SimpMessageHeaderAccessor.CONNECT_MESSAGE_HEADER);
//        @SuppressWarnings("unchecked")
//        Map<String, List<String>> nativeHeaders = (Map<String, List<String>>) connectHeader.getHeaders()
//                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
//
//        String login = nativeHeaders.get("UserId").get(0);
//        String sessionId = stompAccessor.getSessionId();

//        simpMessagingTemplate.setDefaultDestination("/app");
//        simpMessagingTemplate.setUserDestinationPrefix("/app");
//        simpMessagingTemplate.convertAndSend("/session/" + message, message);

//        return message;
//    }

    @MessageExceptionHandler
    @SendTo("/queue/errors")
    public String handleException(Throwable exception) {              //3
        return exception.getMessage();
    }
}
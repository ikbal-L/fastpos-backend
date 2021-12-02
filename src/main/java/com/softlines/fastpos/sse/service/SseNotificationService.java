package com.softlines.fastpos.sse.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.softlines.fastpos.sse.model.EventDto;
import com.softlines.fastpos.sse.repository.EmitterRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.IOException;
import java.util.UUID;

@Service
public class SseNotificationService implements NotificationService {
    @Autowired
    EmitterRepository emitterRepository;
    @Autowired
    private ObjectMapper jacksonObjectMapper;

    public void sendNotification(String identifier, EventDto event) throws IOException {
        if (event == null) {
            return;
        }
        doSendNotification(identifier, event);
    }

    @Override
    public void sendNotificationForAll(EventDto event, String senderId) throws IOException {
       var emitters = emitterRepository.getAllExcept(clientEmitter -> clientEmitter.getIdentifier().equals(senderId));
        for (var emitter: emitters) {
            sendData(event,emitter);
        }
    }

    private void doSendNotification(String identifier, EventDto event) {
        emitterRepository.get(identifier).ifPresent(emitter -> {
            sendData(event, emitter);
        });

    }

    private void sendData(EventDto event, SseEmitter emitter) {
        try {

            var data =jacksonObjectMapper.writeValueAsString(event.getBody());

            emitter.send(
                    SseEmitter.event()
                            .id(UUID.randomUUID().toString())
                            .name(event.getType())
                            .data(data));
        } catch (IOException e) {
            emitter.complete();
        }
    }
}

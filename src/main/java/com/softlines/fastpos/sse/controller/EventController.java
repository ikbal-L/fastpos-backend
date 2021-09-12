package com.softlines.fastpos.sse.controller;

import com.softlines.fastpos.sse.service.EmitterService;
import com.softlines.fastpos.sse.service.NotificationService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@RestController
@RequestMapping("/events")
public class EventController {

    final
    EmitterService emitterService;
    final
    NotificationService notificationService;

    public EventController(EmitterService emitterService, NotificationService notificationService) {
        this.emitterService = emitterService;
        this.notificationService = notificationService;
    }

    @GetMapping(value = "/subscribe",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public  SseEmitter subscribe(@RequestHeader(name="Authorization") String token) throws IOException {
        var emitter=emitterService.createEmitter(token);
        return emitter;
    }

    @GetMapping(value = "/unsubscribe")
    public  boolean unsubscribe(@RequestHeader(name="Authorization") String token){
        return emitterService.close(token);
    }

}

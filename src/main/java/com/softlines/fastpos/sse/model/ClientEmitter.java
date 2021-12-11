package com.softlines.fastpos.sse.model;


import lombok.Data;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


@Data
public class ClientEmitter {


    String identifier;
    SseEmitter emitter;

    public ClientEmitter(String identifier) {
        this.identifier = identifier;
        this.emitter = new SseEmitter(60*60_000L);
    }

    public ClientEmitter(String identifier, SseEmitter emitter) {
        this.identifier = identifier;
        this.emitter = emitter;
    }
    public void  onCompletion(Runnable callback){
        if (this.emitter== null) return;
        this.emitter.onCompletion(callback);
    }
}
package com.softlines.fastpos.sse.service;


import com.softlines.fastpos.sse.repository.EmitterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.util.Optional;

@Service
@Slf4j
public class EmitterService {

    @Autowired
    EmitterRepository repository;


    public SseEmitter createEmitter(String  identifier) {
        return repository.addEmitter(identifier);
    }
    public  Optional<SseEmitter> getEmitter(String  identifier){
      return   repository.get(identifier);
    }

    public boolean close(String  identifier) {
        return repository.remove(identifier);
    }


}

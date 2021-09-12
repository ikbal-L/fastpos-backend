package com.softlines.fastpos.sse.repository;

import com.softlines.fastpos.sse.model.ClientEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface EmitterRepository {

    SseEmitter addEmitter(String  identifier);

    boolean remove(String  identifier);

    Optional<SseEmitter>  get(String  identifier);
    List<SseEmitter>  getAll();
    List<SseEmitter> getAllExcept(Predicate<ClientEmitter> predicate);


}

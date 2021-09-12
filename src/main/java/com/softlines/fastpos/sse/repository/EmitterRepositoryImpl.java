package com.softlines.fastpos.sse.repository;


import com.softlines.fastpos.sse.model.ClientEmitter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.*;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class EmitterRepositoryImpl implements EmitterRepository {

    private final List<ClientEmitter> emitters = new ArrayList<>();

    @Override
    public SseEmitter  addEmitter(String  identifier) {

        var clientEmitter = emitters.stream().filter(x-> Objects.equals(x.getIdentifier(), identifier)).findFirst();

        if (clientEmitter.isEmpty()) {

            var ce =new ClientEmitter(identifier);

            ce.getEmitter().onCompletion(() -> {
                synchronized (this.emitters){
                    emitters.remove(ce);
                }
            });
            ce.getEmitter().onError(throwable -> {
                synchronized (this.emitters){
                    emitters.remove(ce);
                }
            });
//            ce.getEmitter().onTimeout(() -> {
//                ce.getEmitter().complete();
//            });
            emitters.add(ce);

            return ce.getEmitter();
        }else {
           return clientEmitter.get().getEmitter();
        }

    }

    @Override
    public boolean remove(String identifier) {
        return emitters.removeIf(clientEmitter -> clientEmitter.getIdentifier().equals(identifier));
    }

    @Override
    public Optional<SseEmitter> get(String identifier) {
        return Optional.empty();
    }

    @Override
    public List<SseEmitter> getAll() {
        return emitters.stream().map(ClientEmitter::getEmitter).collect(Collectors.toList());
    }
    public List<SseEmitter> getAllExcept(Predicate<ClientEmitter> predicate) {
        return emitters.stream().filter(predicate.negate()).map(ClientEmitter::getEmitter).collect(Collectors.toList());
    }


}

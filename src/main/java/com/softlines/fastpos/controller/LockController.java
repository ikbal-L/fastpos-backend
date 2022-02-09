package com.softlines.fastpos.controller;

import com.softlines.fastpos.dto.Message;
import com.softlines.fastpos.security.securitydomain.Session;
import com.softlines.fastpos.service.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@RestController
@RequestMapping("/api/locks")

public class LockController {
    private final NotificationService notificationService;
    @PersistenceContext
    EntityManager entityManager;

    public LockController(NotificationService notificationService) {
        this.notificationService = notificationService;
        notificationService.registerPublisher(this, "/topic/messages/locks");
    }

    @PostMapping("/lock/{type}/{id}")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<?> lock(@PathVariable String type, @PathVariable Long id) {

        String source = getSession().getId().toString();

        int result = lockEntityById(type, id, source);
        if (result != 1) return ResponseEntity.unprocessableEntity().build();

        var message = Message.builder()
                .type("Lock." + StringUtils.capitalize(type))
                .content(List.of(id))
                .source(source)
                .build();
        notificationService.publish(this, message);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/unlock/{type}/{id}")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<?> unlock(@PathVariable String type, @PathVariable Long id) {


        Query query = unlockEntityById(type, id);
        var result = query.executeUpdate();

        if (result != 1) return ResponseEntity.unprocessableEntity().build();
        var message = Message.builder()
                .type("Unlock." + StringUtils.capitalize(type))
                .content(List.of(id))
                .source(getSession().getId().toString())
                .build();
        notificationService.publish(this, message);

        return ResponseEntity.ok().build();
    }


    public Session getSession() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (Session) auth.getPrincipal();
    }

    @PostMapping("/unlockall/{type}")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<?> unlockAll(@PathVariable String type/*, @RequestBody List<Long> ids*/) {
        var source = getSession().getId().toString();
        List<Long> ids = getEntitiesLockedBySource(type, source);
        int result = unlockEntitiesLockedBySource(type, source);
        if (result == ids.size()) {
            notificationService.sendUnlockOrderMessage(source, ids);
            return ResponseEntity.ok(ids);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    private int lockEntityById(String type, Long id, String source) {
        var namedQuery = StringUtils.capitalize(type) + ".lock";
        var query = entityManager.createNamedQuery(namedQuery)
                .setParameter("id", id)
                .setParameter("source", source);
        return query.executeUpdate();
    }

    private Query unlockEntityById(String type, Long id) {
        var namedQuery = StringUtils.capitalize(type) + ".unlock";
        return entityManager.createNamedQuery(namedQuery).setParameter("id", id);
    }

    private int unlockEntitiesLockedBySource(String type, String source) {
        var namedUnlockQuery = StringUtils.capitalize(type) + ".unlockAllLockedBySource";
        var unlockQuery = entityManager.createNamedQuery(namedUnlockQuery).setParameter("source", source);
        return unlockQuery.executeUpdate();
    }

    private List<Long> getEntitiesLockedBySource(String type, String source) {
        var namedFetchQuery = StringUtils.capitalize(type) + ".findAllLockedBySourceIds";
        var fetchQuery = entityManager.createNamedQuery(namedFetchQuery, Long.class).setParameter("source", source);
        return fetchQuery.getResultList();
    }
}

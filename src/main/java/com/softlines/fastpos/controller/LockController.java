package com.softlines.fastpos.controller;

import com.softlines.fastpos.dto.Message;
import com.softlines.fastpos.security.securitydomain.Session;
import com.softlines.fastpos.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Locale;

@RestController
@RequestMapping("/api/locks")

public class LockController {
    private final NotificationService notificationService;
    @PersistenceContext
    EntityManager entityManager;

    public LockController(NotificationService notificationService) {
        this.notificationService = notificationService;
        notificationService.registerPublisher(this,"/topic/messages/locks");
    }

    @PostMapping("/lock/{type}/{id}")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<?> lock(@PathVariable String type, @PathVariable Long id){


        var namedQuery = StringUtils.capitalize(type) + ".lock";
        var query = entityManager.createNamedQuery(namedQuery).setParameter("id",id);
        var result = query.executeUpdate();
        if (result !=1) return ResponseEntity.unprocessableEntity().build();
        var message = Message.builder().type("Lock." + StringUtils.capitalize(type)).content(id).source(getSession().getId().toString()).build();
        notificationService.publish(this,message);
        return  ResponseEntity.ok().build();
    }
    @PostMapping("/unlock/{type}/{id}")
    @Transactional(transactionManager = "transactionManager")
    public ResponseEntity<?> unlock(@PathVariable String type, @PathVariable Long id){


        var namedQuery = StringUtils.capitalize(type) + ".unlock";
        var query = entityManager.createNamedQuery(namedQuery).setParameter("id",id);
        var result = query.executeUpdate();

        if (result !=1) return ResponseEntity.unprocessableEntity().build();
        var message = Message.builder().type("Unlock." + StringUtils.capitalize(type)).content(id).source(getSession().getId().toString()).build();
        notificationService.publish(this,message);

        return  ResponseEntity.ok().build();
    }

    public Session getSession(){
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return (Session)auth.getPrincipal();
    }
}

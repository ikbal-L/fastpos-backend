package com.softlines.fastpos.security.securityservice;

import com.softlines.fastpos.security.securitydomain.Session;
import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securityrepository.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;


@Service
public class SessionService {

    @PersistenceContext
    EntityManager entityManager;
    @Autowired
    SessionRepository sessionRepository;

//    public String getFullUsernameFromSessionId(UUID id) {
//        @Language("HQL") String queryString = "SELECT concat(u.firstName,concat(' ',u.lastName) ),s from Session s  join fetch s.user u where s.id = :id";
//        var query = entityManager.createQuery(queryString).setParameter("id",id);
//        var result = query.getSingleResult();
//        return  "";
//    }

    private Optional<User> getUserFromSession(String sessionUUID) {
        var session = sessionRepository.findById(UUID.fromString(sessionUUID));
        return session.map(Session::getUser);
    }

    public String getUserFullNameFromSession(String sessionUUID) {
        var user = getUserFromSession(sessionUUID);
        if (user.isPresent()) return String.format("%s %s", user.get().getFirstName(), user.get().getLastName());
        return "";
    }

}

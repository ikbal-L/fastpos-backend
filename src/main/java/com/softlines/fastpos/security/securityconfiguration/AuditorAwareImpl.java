package com.softlines.fastpos.security.securityconfiguration;

import com.softlines.fastpos.security.securitydomain.User;
import com.softlines.fastpos.security.securitydomain.Session;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public @NotNull Optional<String> getCurrentAuditor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        String auditor =null;
        if (authentication.getPrincipal() instanceof User){
            var user =  ((User)authentication.getPrincipal());
            auditor = user.getId()+"";
        }else
        if (authentication.getPrincipal() instanceof Session ){
            var session = (Session) authentication.getPrincipal();
            auditor = session.getId().toString();
        }else {
            auditor = (String) authentication.getPrincipal();
        }
        return Optional.of(auditor);

    }
}

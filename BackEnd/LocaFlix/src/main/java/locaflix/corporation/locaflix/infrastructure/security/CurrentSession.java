package locaflix.corporation.locaflix.infrastructure.security;

import locaflix.corporation.locaflix.domain.exception.UnauthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentSession {

    public SessionPrincipal require() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof SessionPrincipal principal)) {
            throw new UnauthorizedException("Authentication is required");
        }
        return principal;
    }

}

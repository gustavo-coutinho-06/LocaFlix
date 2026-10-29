package locaflix.corporation.locaflix.application.auth;

import locaflix.corporation.locaflix.domain.exception.UnauthorizedException;
import locaflix.corporation.locaflix.domain.model.Session;
import locaflix.corporation.locaflix.domain.model.User;
import locaflix.corporation.locaflix.domain.repository.SessionRepository;
import locaflix.corporation.locaflix.domain.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AuthService {

    private static final String INVALID_CREDENTIALS = "Invalid credentials";

    private final UserRepository userRepository;
    private final SessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final SessionTokenIssuer sessionTokenIssuer;
    private final SessionTtl sessionTtl;

    public AuthService(
            UserRepository userRepository,
            SessionRepository sessionRepository,
            PasswordEncoder passwordEncoder,
            SessionTokenIssuer sessionTokenIssuer,
            SessionTtl sessionTtl
    ) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.sessionTokenIssuer = sessionTokenIssuer;
        this.sessionTtl = sessionTtl;
    }

    @Transactional
    public TokenResponse login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException(INVALID_CREDENTIALS));
        if (!passwordMatches(password, user.passwordHash())) {
            throw new UnauthorizedException(INVALID_CREDENTIALS);
        }
        Session session = sessionRepository.save(Session.create(user.id(), sessionTtl.value()));
        return new TokenResponse(sessionTokenIssuer.issue(session), session.expiresAt());
    }

    @Transactional
    public void logout(UUID sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    private boolean passwordMatches(String password, String passwordHash) {
        try {
            return passwordEncoder.matches(password, passwordHash);
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

}

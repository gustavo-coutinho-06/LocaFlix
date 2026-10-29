package locaflix.corporation.locaflix.infrastructure.web.auth;

import locaflix.corporation.locaflix.application.auth.AuthService;
import locaflix.corporation.locaflix.application.auth.TokenResponse;
import locaflix.corporation.locaflix.infrastructure.security.CurrentSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CurrentSession currentSession;

    public AuthController(AuthService authService, CurrentSession currentSession) {
        this.authService = authService;
        this.currentSession = currentSession;
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        return authService.login(request.email(), request.password());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        authService.logout(currentSession.require().sessionId());
        return ResponseEntity.noContent().build();
    }

}

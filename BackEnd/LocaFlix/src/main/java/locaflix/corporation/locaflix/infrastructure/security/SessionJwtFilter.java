package locaflix.corporation.locaflix.infrastructure.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import locaflix.corporation.locaflix.domain.model.Session;
import locaflix.corporation.locaflix.domain.repository.SessionRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class SessionJwtFilter extends OncePerRequestFilter {

    private static final String BEARER_PREFIX = "Bearer ";

    private final SessionRepository sessionRepository;
    private final JwtDecoder jwtDecoder;

    public SessionJwtFilter(SessionRepository sessionRepository, JwtDecoder jwtDecoder) {
        this.sessionRepository = sessionRepository;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!requiresAuthentication(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith(BEARER_PREFIX) || header.length() == BEARER_PREFIX.length()) {
            writeUnauthorized(response, "Authentication is required");
            return;
        }

        try {
            Jwt jwt = jwtDecoder.decode(header.substring(BEARER_PREFIX.length()));
            UUID userId = UUID.fromString(jwt.getSubject());
            UUID sessionId = UUID.fromString(jwt.getId());
            Session session = sessionRepository.findById(sessionId).orElse(null);
            if (session == null
                    || !session.userId().equals(userId)
                    || !session.expiresAt().isAfter(Instant.now())) {
                writeUnauthorized(response, "Invalid credentials");
                return;
            }
            SessionPrincipal principal = new SessionPrincipal(userId, sessionId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        } catch (JwtException | IllegalArgumentException exception) {
            writeUnauthorized(response, "Invalid credentials");
        }
    }

    private boolean requiresAuthentication(HttpServletRequest request) {
        String path = request.getServletPath();
        return "/auth/logout".equals(path) || "/rentals".equals(path) || path.startsWith("/rentals/");
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"message\":\"" + message + "\"}");
    }

}

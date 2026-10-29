package locaflix.corporation.locaflix.infrastructure.security;

import locaflix.corporation.locaflix.application.auth.SessionTokenIssuer;
import locaflix.corporation.locaflix.domain.model.Session;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class JwtSessionTokenIssuer implements SessionTokenIssuer {

    private final JwtEncoder jwtEncoder;

    public JwtSessionTokenIssuer(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    @Override
    public String issue(Session session) {
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(session.userId().toString())
                .id(session.id().toString())
                .issuedAt(Instant.now())
                .expiresAt(session.expiresAt())
                .build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

}

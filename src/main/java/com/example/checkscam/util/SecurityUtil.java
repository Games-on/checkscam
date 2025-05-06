package com.example.checkscam.util;

import com.example.checkscam.dto.ResLoginDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class SecurityUtil {

    private final JwtEncoder jwtEncoder;

    public SecurityUtil(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${checkscam.jwt.base64-secret}")
    private String jwtKey;

    @Value("${checkscam.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${checkscam.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public String createAccessToken(Authentication authentication) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);


        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("checkscam", authentication)
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return jwt.getSubject();
        }
        if (authentication.getPrincipal() instanceof org.springframework.security.core.userdetails.UserDetails user) {
            return user.getUsername();
        }
        return null;
    }

    public String createRefreshToken(String email, ResLoginDTO dto) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);

        // @formatter:off
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", dto.getUser())
                .build();

        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springUser = (UserDetails) authentication.getPrincipal();
                String userName = springUser.getUsername();
                return Optional.of(userName);
            } else if (authentication.getPrincipal() instanceof String) {
                String userName = (String) authentication.getPrincipal();
                return Optional.of(userName);
            }
        }
        return Optional.empty();
    }


}

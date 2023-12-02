package com.example.tomatoAccounting.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${secret.key}")
    private String KEY;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {  // Weryfikacja tokenu
        String authorization = request.getHeader("Authorization");                                                                                          // Pobranie tokenu z nagłówka
        if (authorization == null) {                                                                                                                           // Jeśli nie ma tokenu to nie weryfikuj
            filterChain.doFilter(request, response);                                                                                                           // Przekaż dalej
            return;
        }
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUsernamePasswordAuthenticationToken(authorization);                        // Weryfikacja tokenu
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);                                                              // Ustawienie kontekstu
        filterChain.doFilter(request, response);                                                                                                                // Przekaż dalej
    }


    public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationToken(String token) {                                                           // Weryfikacja tokenu
        Algorithm algorithm = Algorithm.HMAC256(KEY);                                                                                                           // Ustawienie algorytmu
        JWTVerifier verifier = JWT.require(algorithm)                                                                                                           // Ustawienie weryfikatora
                .build(); //Reusable verifier instance                                                                                                          // Ustawienie weryfikatora
        DecodedJWT jwt = verifier.verify(token.substring(7));                                                                                         // Weryfikacja tokenu
        String[] roles = jwt.getClaim("roles").asArray(String.class);                                                                                        // Pobranie ról z tokenu
        List<SimpleGrantedAuthority> collect = Stream.of(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList());                                  // Utworzenie listy ról

        return new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, collect);                                                              // Zwrócenie tokenu
    }
}

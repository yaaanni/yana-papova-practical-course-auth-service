package com.example.AuthService.filter;

import com.example.AuthService.exception.InvalidTokenException;
import com.example.AuthService.service.CredentialUserDetailsService;
import com.example.AuthService.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CredentialUserDetailsService credentialUserDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            try {
                validateToken(token);
                authenticate(token, request);
            } catch (InvalidTokenException ex) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write(ex.getMessage());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void validateToken(String token) {
        if (!jwtService.validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }
    }

    private void authenticate(String token, HttpServletRequest request) {
        String username = jwtService.extractUsername(token);

        if (username == null ||
                SecurityContextHolder.getContext().getAuthentication() != null) {
            return;
        }

        UserDetails userDetails = credentialUserDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
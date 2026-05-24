
package com.server.ticketmanagement.filters;

import com.server.ticketmanagement.services.impl.UserDetailServiceImpl;
import com.server.ticketmanagement.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailServiceImpl userDetails;
    private final JwtUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of("/login", "/register", "/send-reset-otp", "/reset-password", "/logout");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String servletPath = request.getServletPath();
        if (PUBLIC_URLS.contains(servletPath)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = null;
        String email = null;

        // Check auth header
        final String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
        }

        // Check cookies
        if(jwt == null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c: cookies) {
                    if ("jwt".equals(c.getName())) {
                        jwt = c.getValue();
                        break;
                    }
                }
            }
        }

        // Validate token and set security context
        if (jwt != null) {
            email = jwtUtil.extractEmail(jwt);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails details = userDetails.loadUserByUsername(email);
                if(jwtUtil.validateToken(jwt, details)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

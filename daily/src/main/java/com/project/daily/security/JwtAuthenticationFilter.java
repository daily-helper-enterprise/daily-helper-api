package com.project.daily.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.project.daily.services.MemberDetailsService;
import com.project.daily.utils.JwtUtil;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final MemberDetailsService memberDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, MemberDetailsService memberDetailsService) {
        this.jwtUtil = jwtUtil;
        this.memberDetailsService = memberDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String header = request.getHeader("Authorization");
        String token = null;

        if (header != null && header.startsWith("Bearer ")) {
            token = header.substring(7);
        }

        System.out.println(">>> PASSOU NO JWT FILTER");
        System.out.println("HEADER = " + header);
        System.out.println("TOKEN = " + token);
        System.out.println("TOKEN VALIDO? " + jwtUtil.validateToken(token));

        if (token != null 
            && jwtUtil.validateToken(token)
            && SecurityContextHolder.getContext().getAuthentication() == null) {

            String username = jwtUtil.getUsernameFromToken(token);

            System.out.println("USERNAME DO TOKEN = " + username);

            UserDetails userDetails = memberDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

            System.out.println(">>> AUTENTICANDO USUARIO NO CONTEXTO");

            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        filterChain.doFilter(request, response);
    }

}

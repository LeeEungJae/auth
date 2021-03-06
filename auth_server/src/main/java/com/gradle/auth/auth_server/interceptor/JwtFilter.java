package com.gradle.auth.auth_server.interceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gradle.auth.auth_server.component.JwtTokenEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private JwtTokenEncoder jwtTokenEncoder;
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public JwtFilter(JwtTokenEncoder jwtTokenEncoder, RedisTemplate<String, Object> redisTemplate) {
        this.jwtTokenEncoder = jwtTokenEncoder;
        this.redisTemplate = redisTemplate;
    }

    public Authentication getAuthentication(String token) {
        Map<String, Object> parseInfo = jwtTokenEncoder.getUserParseInfo(token);
        System.out.println("parseinfo: " + parseInfo);
        List<String> rs = (List) parseInfo.get("role");
        Collection<GrantedAuthority> tmp = new ArrayList<>();
        for (String a : rs) {
            tmp.add(new SimpleGrantedAuthority(a));
        }
        UserDetails userDetails = User.builder().username(String.valueOf(parseInfo.get("username"))).authorities(tmp)
                .password("asd").build();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        return usernamePasswordAuthenticationToken;
    }

    @Bean
    public FilterRegistrationBean JwtRequestFilterRegistration(JwtFilter filter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().contains("/api/signup");

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {

            System.out.println(request.getMethod());
            System.out.println("REQUEST : " + request.getHeader("Authorization"));
            String requestTokenHeader = request.getHeader("Authorization");

            logger.info("tokenHeader: " + requestTokenHeader);
            String username = null;
            String jwtToken = null;

            if (requestTokenHeader != null) {
                jwtToken = requestTokenHeader;
                logger.info("token in requestfilter: " + jwtToken);

                try {
                    username = jwtTokenEncoder.getUsernameFromToken(jwtToken);
                    System.out.println("username " + username);
                } catch (IllegalArgumentException e) {
                    logger.warn("Unable to get JWT Token");
                } catch (ExpiredJwtException e) {
                }
            } else {
                logger.warn("JWT Token does not begin with Bearer String");
            }

            if (username == null) {
                logger.info("token maybe expired: username is null.");
            } else if (redisTemplate.opsForValue().get(jwtToken) != null) {
                logger.warn("this token already logout!");
            } else {

                Authentication authen = getAuthentication(jwtToken);

                SecurityContextHolder.getContext().setAuthentication(authen);
                response.setHeader("username", username);
            }
            filterChain.doFilter(request, response);
        }
    }

}

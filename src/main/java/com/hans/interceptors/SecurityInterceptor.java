package com.hans.interceptor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.hans.annotations.Secured;
import com.hans.exceptions.HttpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityInterceptor extends HandlerInterceptorAdapter {

    Logger logger = LoggerFactory.getLogger(SecurityInterceptor.class);

    private Pattern pattern;
    private JWTVerifier verifier;

    public SecurityInterceptor(String key) {
        this.pattern = Pattern.compile("^Bearer (.+?)$");
        this.verifier = JWT.require(Algorithm.HMAC256(key)).withIssuer("com.hans.security").build();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            Secured secured = handlerMethod.getMethodAnnotation(Secured.class);

            if (secured != null) {
                String token = request.getHeader("Authorization");

                Matcher matcher = this.pattern.matcher(token);

                if (matcher.find()) {
                    String jwt = matcher.group(1);
                    try {
                        DecodedJWT decoded = verifier.verify(jwt);
                        request.setAttribute("username", decoded.getSubject());
                        return true;
                    } catch (IllegalArgumentException | JWTVerificationException e) {
                        throw new HttpException("Bearer token could not be verified", HttpStatus.UNAUTHORIZED);
                    }
                }

                throw new HttpException("You must provide bearer token on Authorization header", HttpStatus.UNAUTHORIZED);
            }
        }

        return true;
    }
}

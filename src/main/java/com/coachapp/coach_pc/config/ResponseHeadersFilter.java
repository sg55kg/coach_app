package com.coachapp.coach_pc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ResponseHeadersFilter implements Filter {


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        System.out.println(request.getHeader("Origin"));

        if (request.getHeader("Origin") != null && (request.getHeader("Origin").equals("https://localhost:5173") ||
                request.getHeader("Origin").equals("https://coachablefitness.com") ||
                request.getHeader("Origin").equals("https://www.coachablefitness.com"))
        ) {
            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        }


        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");

        chain.doFilter(req, res);
        removeHttpOnlyFlag((HttpServletResponse) res);
    }

    private void removeHttpOnlyFlag(HttpServletResponse res) {
        var setCookieHeaderName = "set-cookie";
        var setCookieHeader = res.getHeader(setCookieHeaderName);

        if (setCookieHeader != null) {
            setCookieHeader = setCookieHeader.replace("; HttpOnly", "");
            res.setHeader(setCookieHeaderName, setCookieHeader);
        }
    }
}
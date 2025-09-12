package com.tracker_app.tracker.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.IOException;

@Component
public class ApiAccessLogFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(ApiAccessLogFilter.class);

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        try {
            // Put context info into MDC
            MDC.put("method", request.getMethod());
            String uri = request.getRequestURI();
            String query = request.getQueryString();
            if (query != null && !query.isEmpty()) {
                uri += "?" + query;
            }
            MDC.put("uri", uri);

            String forwarded = request.getHeader("X-Forwarded-For");
            String clientIp = (forwarded != null) ? forwarded.split(",")[0].trim() : request.getRemoteAddr();
            MDC.put("clientIp", clientIp);
            MDC.put("requestIp", request.getRemoteAddr());

            String userAgent = request.getHeader("User-Agent");
            MDC.put("userAgent", (userAgent != null) ? userAgent : "unknown");

            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            MDC.put("durationMs", String.valueOf(duration));
            MDC.put("status", String.valueOf(response.getStatus()));

            // Log after the request is processed
            logger.info("API Access Log");

            // Clear MDC to avoid leaking info to other requests
            MDC.clear();
        }
    }
}

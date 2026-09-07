package com.clonecoding.mission.global.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // IP별 요청 기록
    private final Map<String, List<Long>> requestHistory =
            new ConcurrentHashMap<>();

    // 1분
    private static final long TIME_WINDOW = 60_000L;

    // 1분에 최대 60회
    private static final int MAX_REQUESTS = 60;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();

        // /admin/**만 검사
        if (uri.startsWith("/admin/")) {

            String clientIp = getClientIp(request);

            long now = System.currentTimeMillis();

            List<Long> requests = requestHistory.computeIfAbsent(
                    clientIp,
                    key -> new ArrayList<>()
            );

            synchronized (requests) {

                // 1분이 지난 요청 기록 삭제
                requests.removeIf(
                        timestamp -> now - timestamp >= TIME_WINDOW
                );

                // 60회 초과
                if (requests.size() >= MAX_REQUESTS) {

                    response.setStatus(429);

                    response.setContentType(
                            "application/json;charset=UTF-8"
                    );

                    response.getWriter().write(
                            "{\"message\":\"요청이 너무 많습니다. 잠시 후 다시 시도해주세요.\"}"
                    );

                    return;
                }

                // 현재 요청 기록
                requests.add(now);
            }
        }

        // 정상 요청
        filterChain.doFilter(request, response);
    }

    /**
     * 클라이언트 IP 확인
     */
    private String getClientIp(HttpServletRequest request) {

        String xForwardedFor =
                request.getHeader("X-Forwarded-For");

        if (xForwardedFor != null
                && !xForwardedFor.isBlank()
                && !"unknown".equalsIgnoreCase(xForwardedFor)) {

            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp =
                request.getHeader("X-Real-IP");

        if (xRealIp != null
                && !xRealIp.isBlank()
                && !"unknown".equalsIgnoreCase(xRealIp)) {

            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
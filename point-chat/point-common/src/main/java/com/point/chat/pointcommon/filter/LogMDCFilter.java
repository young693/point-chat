package com.point.chat.pointcommon.filter;

import cn.hutool.core.util.IdUtil;
import com.point.chat.pointcommon.constants.CommConstant;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;

import java.io.IOException;


@Slf4j
public class LogMDCFilter implements Filter {
    
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
//        System.out.println("到达过滤器!" + servletRequest.getRemoteHost());
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpServletRequest.getRequestURI();
        if (!requestURI.startsWith("/api/")) { // 静态文件直接放行
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }
        String traceId = getRequestId(httpServletRequest);
        log.info("请求开始,traceId:{},url:{}", traceId, requestURI);
        MDC.put(CommConstant.TRACE_ID, traceId);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            log.info("请求结束,traceId:{},url:{}", traceId, requestURI);
            MDC.remove(CommConstant.TRACE_ID);
        }
    }
    
    public static String getRequestId(HttpServletRequest request) {
        String traceId;
        String parameterRequestId = request.getParameter(CommConstant.TRACE_ID);
        String headerRequestId = request.getHeader(CommConstant.TRACE_ID);
        // 根据请求参数或请求头判断是否有“request-id”，有则使用，无则创建
        if (parameterRequestId == null && headerRequestId == null) {
            traceId = IdUtil.simpleUUID();
        } else {
            traceId = parameterRequestId != null ? parameterRequestId : headerRequestId;
        }
        return traceId;
    }
}

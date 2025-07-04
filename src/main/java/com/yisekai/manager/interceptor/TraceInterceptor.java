package com.yisekai.manager.interceptor;

import cn.hutool.core.lang.UUID;
import com.yisekai.manager.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class TraceInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String traceId = request.getHeader(Constants.TRACE_ID);
        if (StringUtils.isEmpty(traceId)) {
            traceId = UUID.fastUUID().toString(true);
        }
        request.setAttribute("interfaceStartTime", System.currentTimeMillis());
        MDC.put(Constants.TRACE_ID, traceId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        String uri = request.getRequestURI();
        String method = request.getMethod();
        long interfaceStartTime = (Long) request.getAttribute("interfaceStartTime");
        long interfaceEndTime = System.currentTimeMillis();

        long times = interfaceEndTime - interfaceStartTime;
        if (times > 1000) {
            log.info(String.format("==========【%s】，%s，耗时：%s，请检查是否异常", uri, method, times));
        } else {
            log.info(String.format("==========【%s】，%s，耗时：%s", uri, method, times));
        }
        MDC.remove(Constants.TRACE_ID);
    }
}
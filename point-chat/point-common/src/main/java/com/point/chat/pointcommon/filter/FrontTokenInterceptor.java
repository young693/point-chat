package com.point.chat.pointcommon.filter;

import com.alibaba.fastjson.JSON;
import com.point.chat.pointcommon.constants.TokenConstant;
import com.point.chat.pointcommon.entity.ResultBean;
import com.point.chat.pointcommon.manager.TokenManager;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 *  移动端管理端 token验证拦截器 使用前注意需要一个@Bean手动注解，否则注入无效
 */
public class FrontTokenInterceptor implements HandlerInterceptor {
    @Resource
    private TokenManager tokenManager;

    //程序处理之前需要处理的业务
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("拦截器处理");
        String token = tokenManager.getTokenFormRequest(request);
        // token存在时，校验token
        boolean result = tokenManager.checkToken(token, TokenConstant.TOKEN_USER_REDIS);
        if(!result){
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            // response中写入未登录信息
            response.getWriter().write(JSON.toJSONString(ResultBean.unauthorized()));
            return false;
        }
        return true;
    }
}

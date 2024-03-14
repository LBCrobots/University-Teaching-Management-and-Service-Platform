package com.example.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.example.base.BaseResult;
import com.example.student.service.ISysUserService;
import com.example.utils.HutoolJWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Slf4j
@Component
@RequiredArgsConstructor
public class PassportInterceptor implements HandlerInterceptor {

    private final ISysUserService sysUserService;

    @Override //目标资源方法运行前运行, 返回true: 放行, 放回false, 不放行
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        //1.获取请求url。
        String url = req.getRequestURL().toString();
        log.info("请求的url: {}",url);

        //2.判断请求url中是否包含login，如果包含，说明是登录操作，放行。注册同
        if(url.contains("login")){
            log.info("登录操作, 放行...");
            return true;
        }else if (url.contains("register")){
            log.info("注册操作, 放行...");
            // 在这里进行拦截器的逻辑判断，如果不符合条件，直接返回拦截结果。
            // 例如，如果用户名已存在，返回未登录的信息。
            String username = (String) req.getSession().getAttribute("username"); // 请替换成实际的获取用户名的方式
            if (StringUtils.hasLength(username) && sysUserService.getUser(username)) {
                log.info("用户名已存在, 返回注册失败错误信息");
                BaseResult error = BaseResult.fail("REGISTER_FAILED_USER_EXISTS");
                String registerFailed = JSONObject.toJSONString(error);
                resp.getWriter().write(registerFailed);
                return false;
            }
            // 注册操作符合条件，继续执行。
            return true;
        } else if (url.contains("uploadFile")) {
            log.info("图片请求，放行...");
            return true;
        }

        //3.获取请求头中的令牌（token）。
        String jwt = req.getHeader("token");
        log.info("jwt是：{}",jwt);

        //4.判断令牌是否存在，如果不存在，返回错误结果（未登录）。
        if(!StringUtils.hasLength(jwt)){
            log.info("请求头token为空,返回未登录的信息");
            BaseResult error = BaseResult.fail("NOT_LOGIN");
            //手动转换 对象--json --------> 阿里巴巴fastJSON
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }

        //5.解析token，如果解析失败，返回错误结果（未登录）。
        try {
            HutoolJWTUtil.parseToken(jwt);
        } catch (Exception e) {//jwt解析失败
            e.printStackTrace();
            log.info("解析令牌失败, 返回未登录错误信息");
            BaseResult error = BaseResult.fail("NOT_LOGIN");
            //手动转换 对象--json --------> 阿里巴巴fastJSON
            String notLogin = JSONObject.toJSONString(error);
            resp.getWriter().write(notLogin);
            return false;
        }

        //6.放行。
        log.info("令牌合法, 放行");
        return true;
    }

    @Override //目标资源方法运行后运行
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle ...");
    }

    @Override //视图渲染完毕后运行, 最后运行
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion...");
    }
}

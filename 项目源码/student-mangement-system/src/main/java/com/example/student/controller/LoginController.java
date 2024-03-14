package com.example.student.controller;

import com.example.base.BaseResult;
import com.example.student.domain.SysUser;
import com.example.student.service.ISysUserService;
import com.example.student.service.TokenService;
import com.example.utils.HutoolJWTUtil;
import com.example.utils.Md5Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**功能描述：系统后台登录前端控制器*/
@Slf4j
@RestController
public class LoginController {

    private final ISysUserService sysUserService;
    private final TokenService tokenService;
    public LoginController(ISysUserService sysUserService, TokenService tokenService) {
        this.sysUserService = sysUserService;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public BaseResult login(@RequestBody SysUser sysUser, HttpServletRequest request){
        log.info("SysUser:{}",sysUser);
        SysUser dbSysUser = sysUserService.login(sysUser);
        log.info("dbSysUser:{}",dbSysUser);
        if(dbSysUser==null){
            return BaseResult.fail("登录失败，账号不存在");
        } else if (!dbSysUser.getPassword().equals(Md5Util.MD5(sysUser.getPassword()))) {
            return BaseResult.fail("登录失败，密码不正确");
        } else if (dbSysUser.getStatus()==0) {
            return BaseResult.fail("登录失败，账号被封禁");
        }
        // 生成token
        String token = HutoolJWTUtil.createToken(dbSysUser);
        request.getServletContext().setAttribute("token",token);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("username",dbSysUser.getUsername());
        resultMap.put("realname",dbSysUser.getRealname());
        resultMap.put("token",token);
        tokenService.save(token);
        resultMap.put("email",dbSysUser.getEmail());
        resultMap.put("sex",dbSysUser.getSex());
        resultMap.put("userIcon",dbSysUser.getUserIcon());
        resultMap.put("createTime",dbSysUser.getCreateTime());
        resultMap.put("role",dbSysUser.getSysRole());
        return BaseResult.success("登录成功",resultMap);
    }

    /*@PostMapping("register")
    public BaseResult register(@RequestBody SysUser sysUser) {
        SysUser existingUser = sysUserService.login(sysUser);
        if (existingUser != null) {
            return BaseResult.fail("注册失败，用户名已存在");
        }

        *//*sysUser.setPassword(Md5Util.MD5(sysUser.getPassword()));*//*
        sysUser.setStatus(1);
        boolean success = sysUserService.addUser(sysUser);

        if (success) {
            return BaseResult.success("注册成功");
        } else {
            return BaseResult.fail("注册失败，请稍后重试");
        }
    }*/

    /**
     * 退出系统
     * @param request
     * @return
     */
    @GetMapping("loginOut")
    public BaseResult loginOut(HttpServletRequest request){
        request.getServletContext().removeAttribute("token");
        tokenService.deleteToken();
        return BaseResult.success("退出成功");
    }
}

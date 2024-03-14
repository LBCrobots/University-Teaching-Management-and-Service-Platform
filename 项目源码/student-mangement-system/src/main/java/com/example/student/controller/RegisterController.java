package com.example.student.controller;

/*import cn.xueden.annotation.generation.CreateByGenerators;*/
import com.example.base.BaseResult;
import com.example.student.domain.SysUser;
import com.example.student.service.IRegisterService;
import com.example.student.service.ISysUserService;
import com.example.utils.HutoolJWTUtil;
import com.example.utils.Md5Util;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/register")
public class RegisterController {

    private final ISysUserService sysUserService;
    private final IRegisterService registerService;

    @PostMapping
    public BaseResult register(@RequestBody SysUser sysUser, HttpServletRequest request){
        String url = request.getRequestURL().toString();
        log.info("注册的url是:{}",url);
        if (sysUser == null) {
            return BaseResult.fail("注册失败，用户信息为空");
        }
        SysUser dbSysUser = registerService.getByUsername(sysUser.getUsername());
        if (dbSysUser != null) {
            log.info("dbSysUser不是空");
            return BaseResult.fail("注册失败，账号已存在");
        }

        // 生成token
        String token = HutoolJWTUtil.createToken(sysUser);
        request.getServletContext().setAttribute("token",token);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        sysUser.setCreateTime(new Timestamp(System.currentTimeMillis()));
        sysUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        resultMap.put("username",sysUser.getUsername());
        resultMap.put("realname",sysUser.getRealname());
        resultMap.put("token",token);
        resultMap.put("email",sysUser.getEmail());
        resultMap.put("sex",sysUser.getSex());
        resultMap.put("userIcon",sysUser.getUserIcon());
        resultMap.put("createTime",sysUser.getCreateTime());
        resultMap.put("updateTime",sysUser.getUpdateTime());
        resultMap.put("role",sysUser.getSysRole());

        // 将用户保存到数据库
        registerService.addRegister(sysUser);
        SysUser updateSysUser = registerService.getByUsername(sysUser.getUsername());
        if (updateSysUser != null) {
            /*Long uid = updateSysUser.getId();
            registerService.UpdateRoleId(uid);*/
            // 返回包含用户信息的 BaseResult
            return BaseResult.success("注册成功", updateSysUser);
        } else {
            return BaseResult.fail("注册失败，请稍后重试");
        }
        /*return BaseResult.success("注册成功123",resultMap);*/
    }
}

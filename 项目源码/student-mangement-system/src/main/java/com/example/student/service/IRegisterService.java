package com.example.student.service;

import com.example.student.domain.SysUser;

public interface IRegisterService {

    SysUser getByUsername(String username);
    public void addRegister(SysUser sysUser);

    void UpdateRoleId(Long id);
}

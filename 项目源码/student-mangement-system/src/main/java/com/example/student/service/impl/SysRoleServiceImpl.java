package com.example.student.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.example.student.domain.SysRole;


import com.example.student.repository.SysRoleRepository;
import com.example.student.service.IRoleService;
import com.example.student.service.dto.RoleQueryCriteria;

import com.example.utils.PageUtil;
import com.example.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**功能描述：系统角色接口实现类*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SysRoleServiceImpl implements IRoleService {

    private final SysRoleRepository sysRoleRepository;


    /**
     * 获取角色列表数据
     * @param queryCriteria
     * @param pageable
     * @return
     */
    @Override
    public Object getList(RoleQueryCriteria queryCriteria, Pageable pageable) {
        Page<SysRole> page = sysRoleRepository.findAll((root, query, criteriaBuilder) -> QueryHelp.getPredicate(root,queryCriteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page);
    }

    /**
     * 新增角色信息
     * @param sysRole
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addRole(SysRole sysRole) {
        SysRole dbSysRole = sysRoleRepository.save(sysRole);
        return dbSysRole.getId()!=null;
    }

    /**
     * 根据ID获取角色信息
     * @param id
     * @return
     */
    @Override
    public SysRole getById(Long id) {
        return sysRoleRepository.findById(id).orElseGet(SysRole::new);
    }


    /**
     * 更新角色信息
     * @param sysRole
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editRole(SysRole sysRole) {
        SysRole dbSysRole = sysRoleRepository.getReferenceById(sysRole.getId());
        BeanUtil.copyProperties(sysRole,dbSysRole, CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
        sysRoleRepository.save(dbSysRole);
    }

    /**
     * 根据ID删除角色信息
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Long id) {
        sysRoleRepository.deleteById(id);
    }

    /**
     * 获取所有角色
     * @return
     */
    @Override
    public List<SysRole> queryAll() {
        return sysRoleRepository.findAll();
    }
}

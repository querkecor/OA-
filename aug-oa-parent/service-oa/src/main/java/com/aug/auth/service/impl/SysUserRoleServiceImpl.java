package com.aug.auth.service.impl;

import com.aug.auth.service.SysRoleService;
import com.aug.model.system.SysRole;
import com.aug.model.system.SysUser;
import com.aug.model.system.SysUserRole;
import com.aug.auth.mapper.SysUserRoleMapper;
import com.aug.auth.service.SysUserRoleService;
import com.aug.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 用户角色 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-26
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

    @Resource
    private SysRoleService sysRoleService;

    @Override
    public Map<String,List<SysRole>> getRoleDataByUserId(Long userId) {
        List<SysRole> allRoleList = sysRoleService.list();
        List<SysUserRole> existUserRoleList = this.list(
                new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId).
                        select(SysUserRole::getRoleId));
        List<Long> existRoleIdList = existUserRoleList.stream().map(SysUserRole::getRoleId).collect(Collectors.toList());

        List<SysRole> assginRoleList = new ArrayList<>();
        for (SysRole sysRole : allRoleList) {
            if (existRoleIdList.contains(sysRole.getId())) {
                assginRoleList.add(sysRole);
            }
        }
        Map<String,List<SysRole>> map = new HashMap<>();
        map.put("assignRoleList", assginRoleList);
        map.put("allRolesList", allRoleList);
        return map;
    }


    @Override
    @Transactional
    public boolean doAssign(AssginRoleVo assginRoleVo) {
        boolean remove = this.remove(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, assginRoleVo.getUserId()));

        List<SysUserRole> sysUserRoleList = new ArrayList<>();
        if (remove) {
            List<Long> roleIdList = assginRoleVo.getRoleIdList();
            Long userId = assginRoleVo.getUserId();
            for (Long aLong : roleIdList) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setRoleId(aLong);
                sysUserRole.setUserId(userId);
                sysUserRoleList.add(sysUserRole);
            }
            return this.saveBatch(sysUserRoleList);
        }
        return false;
    }
}

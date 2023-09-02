package com.aug.auth.service.impl;

import com.aug.auth.mapper.SysUserMapper;
import com.aug.auth.service.SysUserService;
import com.aug.model.system.SysUser;
import com.aug.security.custom.LoginUserInfoHelper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-26
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public boolean updateStatus(Long id, Integer status) {
        SysUser user = this.getById(id);
        if (status == 1) {
            user.setStatus(status);
        } else {
            user.setStatus(0);
        }
        return this.updateById(user);
    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        return this.getOne(wrapper);
    }

    @Override
    public Map<String, Object> getCurrentUser() {
        Map<String,Object> map = new HashMap<>();
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = this.getById(userId);
        map.put("name",sysUser.getName());
        map.put("phone",sysUser.getPhone());
        return map;
    }
}

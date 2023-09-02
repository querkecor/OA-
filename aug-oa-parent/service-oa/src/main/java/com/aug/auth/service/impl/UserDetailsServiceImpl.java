package com.aug.auth.service.impl;

import com.aug.auth.service.SysMenuService;
import com.aug.auth.service.SysUserService;
import com.aug.common.config.exception.AugException;
import com.aug.model.system.SysUser;
import com.aug.security.custom.CustomUser;
import com.aug.security.custom.UserDetailsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author querkecor
 * @date 2023/8/18
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserService.getByUsername(username);
        if (user == null) {
            throw new AugException(201, "用户名不存在");
        }
        if (user.getStatus() == 0) {
            throw new AugException(201,"用户已被禁用，请联系管理员");
        }
        List<SimpleGrantedAuthority> authList = this.getAuthList(user.getId());
        return new CustomUser(user, authList);
    }

    public List<SimpleGrantedAuthority> getAuthList(Long userId) {
        List<String> userPermsList = sysMenuService.findUserPermsList(userId);
        List<SimpleGrantedAuthority> authList = new ArrayList<>();
        for (String perm : userPermsList) {
            authList.add(new SimpleGrantedAuthority(perm));
        }
        return authList;
    }
}

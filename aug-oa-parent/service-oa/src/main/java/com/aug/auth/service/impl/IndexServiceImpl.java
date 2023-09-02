package com.aug.auth.service.impl;

import com.aug.auth.service.IndexService;
import com.aug.auth.service.SysUserService;
import com.aug.common.config.exception.AugException;
import com.aug.common.jwt.JwtHelper;
import com.aug.common.utils.MD5;
import com.aug.model.system.SysUser;
import com.aug.vo.system.LoginVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author querkecor
 * @date 2023/8/14
 */
@Service
public class IndexServiceImpl implements IndexService {

    @Resource
    private SysUserService sysUserService;

    @Override
    public String login(LoginVo loginVo) {

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (loginVo.getUsername() == null) {
            throw new AugException(201, "请输入用户名");
        }
        wrapper.eq(SysUser::getUsername, loginVo.getUsername());
        SysUser user = sysUserService.getOne(wrapper);
        if (user == null) {
            throw new AugException(201, "用户名不存在");
        }
        if (loginVo.getPassword() == null) {
            throw new AugException(201, "请输入密码");
        }
        String password = MD5.encrypt(loginVo.getPassword());
        if (!password.equals(user.getPassword())) {
            throw new AugException(201,"请输入正确的密码");
        }
        // 判断用户是否被禁用 0 禁用，1 可用
        if (user.getStatus() == 0) {
            throw new AugException(201,"用户已被禁用，请联系管理员");
        }

        return JwtHelper.createToken(user.getId(), user.getUsername());
    }


}

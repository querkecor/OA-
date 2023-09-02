package com.aug.auth.service;

import com.aug.model.system.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-26
 */
public interface SysUserService extends IService<SysUser> {

    boolean updateStatus(Long id, Integer status);

    SysUser getByUsername(String username);

    Map<String, Object> getCurrentUser();
}

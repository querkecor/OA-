package com.aug.auth.service;

import com.aug.model.system.SysRole;
import com.aug.model.system.SysUserRole;
import com.aug.vo.system.AssginRoleVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户角色 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-26
 */
public interface SysUserRoleService extends IService<SysUserRole> {

    Map<String,List<SysRole>> getRoleDataByUserId(Long userId);

    boolean doAssign(AssginRoleVo assginRoleVo);
}

package com.aug.auth.service;

import com.aug.model.system.SysMenu;
import com.aug.model.system.SysRoleMenu;
import com.aug.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 角色菜单 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-30
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

    List<SysMenu> getMenuByRoleId(Long roleId);

    boolean doAssign(AssginMenuVo assignMenuVo);
}

package com.aug.auth.service.impl;

import com.aug.auth.helper.MenuHelper;
import com.aug.auth.mapper.SysMenuMapper;
import com.aug.model.system.SysMenu;
import com.aug.model.system.SysRoleMenu;
import com.aug.auth.mapper.SysRoleMenuMapper;
import com.aug.auth.service.SysRoleMenuService;
import com.aug.vo.system.AssginMenuVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜单 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-30
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> getMenuByRoleId(Long roleId) {
        List<SysMenu> allMenuList = sysMenuMapper.selectList(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getStatus, 1));

        List<SysRoleMenu> roleMenuList = this.list(
                new LambdaQueryWrapper<SysRoleMenu>()
                        .eq(SysRoleMenu::getRoleId, roleId)
                        .select(SysRoleMenu::getMenuId));
        List<Long> menuIdLIst = roleMenuList.stream()
                .map(SysRoleMenu::getMenuId)
                .collect(Collectors.toList());

        for (SysMenu sysMenu : allMenuList) {
            if (menuIdLIst.contains(sysMenu.getId())) {
                sysMenu.setSelect(true);
            }
        }

        return MenuHelper.buildTree(allMenuList);
    }

    @Override
    public boolean doAssign(AssginMenuVo assignMenuVo) {
        this.remove(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getRoleId, assignMenuVo.getRoleId()));
        List<Long> menuIdList = assignMenuVo.getMenuIdList();
        List<SysRoleMenu> sysRoleMenuList = new ArrayList<>();
        for (Long menuId : menuIdList) {
            SysRoleMenu sysRoleMenu = new SysRoleMenu();
            sysRoleMenu.setRoleId(assignMenuVo.getRoleId());
            sysRoleMenu.setMenuId(menuId);
            sysRoleMenuList.add(sysRoleMenu);
        }
        return this.saveBatch(sysRoleMenuList);
    }
}

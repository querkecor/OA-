package com.aug.auth.service.impl;

import com.aug.common.config.exception.AugException;
import com.aug.model.system.SysMenu;
import com.aug.auth.mapper.SysMenuMapper;
import com.aug.auth.service.SysMenuService;
import com.aug.auth.helper.MenuHelper;
import com.aug.vo.system.MetaVo;
import com.aug.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-30
 */
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenu> findNodes() {
        List<SysMenu> sysMenuList = this.list();
        if (CollectionUtils.isEmpty(sysMenuList)) {
            return null;
        }
        return MenuHelper.buildTree(sysMenuList);
    }

    @Override
    public boolean removeMenuById(Long id) {
        int count = this.count(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, id));
        if (count > 0) {
            throw new AugException(201, "菜单不能删除");
        }
        return this.removeById(id);
    }

    @Override
    public List<RouterVo> findUserMenuListByUserId(Long userId) {
        List<SysMenu> menuList = sysMenuMapper.getUserMenuListByUserId(userId);

        List<SysMenu> routeList = MenuHelper.buildTree(menuList);

        return this.buildRouter(routeList);
    }

    @Override
    public List<String> findUserPermsList(Long userId) {
        List<SysMenu> menuList = sysMenuMapper.getUserMenuListByUserId(userId);

        return menuList.stream()
                .filter(item -> item.getType() == 2).map(SysMenu::getPerms).collect(Collectors.toList());
    }

    private List<RouterVo> buildRouter(List<SysMenu> routeList) {
        List<RouterVo> routerVoList = new ArrayList<>();
        for (SysMenu sysMenu : routeList) {
            RouterVo routerVo = new RouterVo();
            routerVo.setPath(getRouterPath(sysMenu));
            routerVo.setHidden(false);
            routerVo.setAlwaysShow(false);
            routerVo.setComponent(sysMenu.getComponent());
            routerVo.setMeta(new MetaVo(sysMenu.getName(), sysMenu.getIcon()));
            List<SysMenu> children = sysMenu.getChildren();
            if (sysMenu.getType() == 1) {
                List<SysMenu> hiddenMenuList = children.stream().filter(item -> !StringUtils.isEmpty(item.getComponent())).collect(Collectors.toList());
                for (SysMenu hiddenMenu : hiddenMenuList) {
                    RouterVo hiddenRouter = new RouterVo();
                    hiddenRouter.setHidden(true);
                    hiddenRouter.setAlwaysShow(false);
                    hiddenRouter.setPath(getRouterPath(hiddenMenu));
                    hiddenRouter.setComponent(hiddenMenu.getComponent());
                    hiddenRouter.setMeta(new MetaVo(hiddenMenu.getName(),hiddenMenu.getIcon()));
                    routerVoList.add(hiddenRouter);
                }
            }else {
                if (!CollectionUtils.isEmpty(children)) {
                    if (children.size() > 0) {
                        routerVo.setAlwaysShow(true);
                    }
                    routerVo.setChildren(buildRouter(children));
                }
            }
            routerVoList.add(routerVo);
        }
        return routerVoList;
    }

    public String getRouterPath(SysMenu menu) {
        String routerPath = "/" + menu.getPath();
        if(menu.getParentId().intValue() != 0) {
            routerPath = menu.getPath();
        }
        return routerPath;
    }


}

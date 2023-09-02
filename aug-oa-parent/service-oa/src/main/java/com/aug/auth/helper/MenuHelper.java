package com.aug.auth.helper;

import com.aug.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author querkecor
 * @date 2023/4/30
 */

public class MenuHelper {

    /**
     * 使用递归方法建菜单
     * @param sysMenuList
     * @return
     */
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {

        List<SysMenu> treeNodes = new ArrayList<>();
        for (SysMenu sysMenu : sysMenuList) {
            if (sysMenu.getParentId() == 0) {
                treeNodes.add(findChildren(sysMenu, sysMenuList));
            }
        }
        return treeNodes;
    }

    public static SysMenu findChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<>());

        for (SysMenu menu : sysMenuList) {
            if (sysMenu.getId().intValue() == menu.getParentId().intValue()) {
                if (menu.getChildren() == null) {
                    menu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(findChildren(menu,sysMenuList));
            }
        }
        return sysMenu;
    }
}

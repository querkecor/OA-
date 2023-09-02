package com.aug.auth.service;

import com.aug.model.system.SysMenu;
import com.aug.vo.system.RouterVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-04-30
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 菜单树形数据
     * @return
     */
    List<SysMenu> findNodes();

    boolean removeMenuById(Long id);

    List<RouterVo> findUserMenuListByUserId(Long id);

    List<String> findUserPermsList(Long userId);
}

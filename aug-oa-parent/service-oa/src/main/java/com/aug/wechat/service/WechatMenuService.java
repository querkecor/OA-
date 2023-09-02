package com.aug.wechat.service;


import com.aug.model.wechat.Menu;
import com.aug.vo.wechat.MenuVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-09-01
 */
public interface WechatMenuService extends IService<Menu> {

    List<MenuVo> findMenuInfo();

    void syncMenu();

    void removeMenu();

}

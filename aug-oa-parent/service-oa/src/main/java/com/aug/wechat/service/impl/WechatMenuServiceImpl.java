package com.aug.wechat.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.aug.model.wechat.Menu;
import com.aug.vo.wechat.MenuVo;
import com.aug.wechat.mapper.WechatMenuMapper;
import com.aug.wechat.service.WechatMenuService;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.SneakyThrows;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 菜单 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-09-01
 */
@Service
public class WechatMenuServiceImpl extends ServiceImpl<WechatMenuMapper, Menu> implements WechatMenuService {

    @Resource
    private WxMpService wxMpService;

    @Override
    public List<MenuVo> findMenuInfo() {
        List<MenuVo> parentMenuVoList = new ArrayList<>();
        List<Menu> menuList = this.list();
        List<Menu> parentMenuList = menuList.stream().filter(ml -> ml.getParentId() == 0).collect(Collectors.toList());
        for (Menu menu : parentMenuList) {
            MenuVo menuVo = new MenuVo();
            BeanUtils.copyProperties(menu, menuVo);

            List<Menu> childrenList = menuList.stream()
                    .filter(ml -> Objects.equals(ml.getParentId(), menu.getId()))
                    .sorted(Comparator.comparing(Menu::getSort))
                    .collect(Collectors.toList());
            List<MenuVo> childrenVoList = new ArrayList<>();
            for (Menu childrenMenu : childrenList) {
                MenuVo childrenVo = new MenuVo();
                BeanUtils.copyProperties(childrenMenu, childrenVo);
                childrenVoList.add(childrenVo);
            }
            menuVo.setChildren(childrenVoList);
            parentMenuVoList.add(menuVo);
        }
        return parentMenuVoList;
    }

    @Override
    public void syncMenu() {
        List<MenuVo> menuVoList = this.findMenuInfo();
        // 菜单
        JSONArray buttonList = new JSONArray();
        for (MenuVo oneMenuVo : menuVoList) {
            JSONObject one = new JSONObject();
            one.put("name", oneMenuVo.getName());
            if (CollectionUtils.isEmpty(oneMenuVo.getChildren())) {
                one.put("type", oneMenuVo.getType());
                one.put("url", "http://oaweb.v5.idcfengye.com/#" + oneMenuVo.getUrl());
            } else {
                JSONArray subButton = new JSONArray();
                for (MenuVo twoMenuVo : oneMenuVo.getChildren()) {
                    JSONObject view = new JSONObject();
                    view.put("type", twoMenuVo.getType());
                    if (twoMenuVo.getType().equals("view")) {
                        view.put("name", twoMenuVo.getName());
                        // H5页面地址
                        view.put("url", "http://oaweb.v5.idcfengye.com#" + twoMenuVo.getUrl());
                    } else {
                        view.put("name", twoMenuVo.getName());
                        view.put("key", twoMenuVo.getMeunKey());
                    }
                    subButton.add(view);
                }
                one.put("sub_button", subButton);
            }
            buttonList.add(one);
        }
        // 菜单
        JSONObject button = new JSONObject();
        button.put("button", buttonList);
        try {
            wxMpService.getMenuService().menuCreate(button.toJSONString());
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeMenu() {
        try {
            wxMpService.getMenuService().menuDelete();
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
}

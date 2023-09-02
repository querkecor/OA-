package com.aug.wechat.controller;


import com.aug.common.result.Result;
import com.aug.model.wechat.Menu;
import com.aug.vo.wechat.MenuVo;
import com.aug.wechat.service.WechatMenuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单 前端控制器
 * </p>
 *
 * @author querkecor
 * @since 2023-09-01
 */
@RestController
@RequestMapping("/admin/wechat/menu")
public class WechatMenuController {

    @Resource
    private WechatMenuService wechatMenuService;

    @GetMapping("findMenuInfo")
    public Result<List<MenuVo>> findMenuInfo() {
        List<MenuVo> menuModel = wechatMenuService.findMenuInfo();
        return Result.success(menuModel);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        Menu menu = wechatMenuService.getById(id);
        if (menu != null) {
            return Result.success(menu);
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result updateById(@RequestBody Menu menu) {
        boolean update = wechatMenuService.updateById(menu);
        if (update) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result save(@RequestBody Menu menu) {
        boolean save = wechatMenuService.save(menu);
        if (save) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean remove = wechatMenuService.removeById(id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "同步菜单")
    @GetMapping("syncMenu")
    public Result createMenu() {
        wechatMenuService.syncMenu();
        return Result.success();
    }

    @PreAuthorize("hasAuthority('bnt.menu.removeMenu')")
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("removeMenu")
    public Result removeMenu() {
        wechatMenuService.removeMenu();
        return Result.success();
    }
}



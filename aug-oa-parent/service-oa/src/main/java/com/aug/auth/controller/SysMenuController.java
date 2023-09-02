package com.aug.auth.controller;


import com.aug.auth.service.SysMenuService;
import com.aug.auth.service.SysRoleMenuService;
import com.aug.common.result.Result;
import com.aug.model.system.SysMenu;
import com.aug.vo.system.AssginMenuVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 菜单表 前端控制器
 * </p>
 *
 * @author querkecor
 * @since 2023-04-30
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/admin/system/sysMenu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRoleMenuService sysRoleMenuService;


    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation(value = "获取菜单")
    @GetMapping("/findNodes")
    public Result<List<SysMenu>> findNodes() {
        List<SysMenu> nodes = sysMenuService.findNodes();
        return Result.success(nodes);
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.add')")
    @ApiOperation(value = "新增菜单")
    @PostMapping("/save")
    public Result<String> saveMenu(@RequestBody SysMenu SysMenu) {
        boolean result = sysMenuService.save(SysMenu);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.update')")
    @ApiOperation(value = "修改菜单")
    @PutMapping("update")
    public Result<String> updateById(@RequestBody SysMenu permission) {
        boolean result = sysMenuService.updateById(permission);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.remove')")
    @ApiOperation(value = "删除菜单")
    @DeleteMapping("remove/{id}")
    public Result<String> remove(@PathVariable Long id) {
        sysMenuService.removeById(id);
        return Result.success();
    }

    @PreAuthorize("hasAuthority('bnt.sysMenu.list')")
    @ApiOperation(value = "根据角色获取菜单")
    @GetMapping("toAssign/{roleId}")
    public Result<List<SysMenu>> toAssign(@PathVariable("roleId")Long roleId) {
        List<SysMenu> menuList = sysRoleMenuService.getMenuByRoleId(roleId);
        return Result.success(menuList);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @ApiOperation(value = "给角色分配权限")
    @PostMapping("/doAssign")
    public Result<String> doAssign(@RequestBody AssginMenuVo assignMenuVo) {
        boolean result = sysRoleMenuService.doAssign(assignMenuVo);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}


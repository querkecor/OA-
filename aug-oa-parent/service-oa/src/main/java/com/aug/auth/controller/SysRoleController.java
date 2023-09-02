package com.aug.auth.controller;

import com.aug.auth.service.SysRoleService;
import com.aug.auth.service.SysUserRoleService;
import com.aug.common.result.Result;
import com.aug.model.system.SysRole;
import com.aug.vo.system.AssginRoleVo;
import com.aug.vo.system.SysRoleQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author querkecor
 * @date 2023/4/23
 */

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @ApiOperation("查询所有角色")
    @GetMapping("/findAll")
    public Result<List<SysRole>> findAll() {
        List<SysRole> list = sysRoleService.list();
        return Result.success(list);
    }

    /**
     * @return
     * @page 当前页数
     * @limit 每页显示记录数
     */
    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result<Page<SysRole>> pageQueryRole(@PathVariable("page") Integer page,
                                               @PathVariable("limit") Integer limit,
                                               SysRoleQueryVo sysRoleQueryVo) {
        Page<SysRole> pageParam = new Page<>(page, limit);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        Page<SysRole> pageInfo = sysRoleService.page(pageParam, wrapper);

        return Result.success(pageInfo);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.add')")
    @ApiOperation("添加角色信息")
    @PostMapping("/save/role")
    public Result<String> saveRole(@RequestBody SysRole sysRole) {
        boolean saveResult = sysRoleService.save(sysRole);

        if (saveResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.list')")
    @ApiOperation("根据id查询角色")
    @GetMapping("/get/{id}")
    public Result<SysRole> getRoleById(@PathVariable("id") Integer sysRoleId) {
        SysRole role = sysRoleService.getById(sysRoleId);
        return Result.success(role);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.update')")
    @ApiOperation("修改角色信息")
    @PutMapping("/update/role")
    public Result<String> updateRole(@RequestBody SysRole sysRole) {
        boolean saveResult = sysRoleService.updateById(sysRole);
        if (saveResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("根据id删除角色")
    @DeleteMapping("/remove/{id}")
    public Result<String> deleteRole(@PathVariable("id") Integer id) {
        boolean removeResult = sysRoleService.removeById(id);

        if (removeResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.remove')")
    @ApiOperation("批量删除角色")
    @DeleteMapping("/batchRemove")
    public Result<String> batchDeleteRole(@RequestBody List<Integer> sysRoleIdList) {
        boolean removeResult = sysRoleService.removeByIds(sysRoleIdList);

        if (removeResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "根据用户获取角色数据")
    @GetMapping("/toAssign/{userId}")
    public Result<Map<String,List<SysRole>>> toAssign(@PathVariable("userId") Long userId) {
        Map<String,List<SysRole>> roleNameList = sysUserRoleService.getRoleDataByUserId(userId);
        return Result.success(roleNameList);
    }

    @PreAuthorize("hasAuthority('bnt.sysRole.assignAuth')")
    @ApiOperation("分配角色")
    @PostMapping("/doAssign")
    public Result<String> doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        boolean result = sysUserRoleService.doAssign(assginRoleVo);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}

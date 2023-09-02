package com.aug.auth.controller;


import com.aug.auth.service.SysUserRoleService;
import com.aug.auth.service.SysUserService;
import com.aug.common.config.exception.AugException;
import com.aug.common.result.Result;
import com.aug.common.utils.MD5;
import com.aug.model.system.SysUser;
import com.aug.vo.system.SysUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author querkecor
 * @since 2023-04-26
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result<Page<SysUser>> pageQueryUser(@PathVariable("page") Integer page,
                                               @PathVariable("limit") Integer limit,
                                               SysUserQueryVo sysUserQueryVo) {
        // 创建分页模型
        Page<SysUser> pageParam = new Page<>(page, limit);
        // 封装查询条件
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        String keyword = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        if (!StringUtils.isEmpty(keyword)) {
            wrapper.like(SysUser::getUsername, keyword);
        }
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime, createTimeBegin);
        }
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.le(SysUser::getUpdateTime, createTimeEnd);
        }
        // 调用mp方法实现分页查询
        Page<SysUser> userPage = sysUserService.page(pageParam, wrapper);

        return Result.success(userPage);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.list')")
    @ApiOperation(value = "获取用户")
    @GetMapping("/get/{id}")
    public Result<SysUser> get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.success(user);
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.add')")
    @ApiOperation(value = "保存用户")
    @PostMapping("/save")
    public Result<String> save(@RequestBody SysUser user) {
        if (user.getPassword() == null) {
            throw new AugException(201,"请输入有效用户信息");
        }
        String password = MD5.encrypt(user.getPassword());
        user.setPassword(password);
        boolean saveResult = sysUserService.save(user);
        if (saveResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation(value = "更新用户")
    @PutMapping("/update")
    public Result<String> updateById(@RequestBody SysUser user) {
        boolean updateResult = sysUserService.updateById(user);
        if (updateResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result<String> remove(@PathVariable Long id) {
        boolean removeResult = sysUserService.removeById(id);
        if (removeResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.remove')")
    @ApiOperation(value = "删除用户")
    @DeleteMapping("/batchRemove")
    public Result<String> batchRemove(List<Long> idList) {
        boolean removeResult = sysUserService.removeByIds(idList);
        if (removeResult) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @PreAuthorize("hasAuthority('bnt.sysUser.update')")
    @ApiOperation(value = "更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result<String> updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        boolean result = sysUserService.updateStatus(id, status);
        if (result) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}

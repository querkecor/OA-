package com.aug.auth.controller;

import com.aug.auth.service.IndexService;
import com.aug.auth.service.SysMenuService;
import com.aug.auth.service.SysUserService;
import com.aug.common.config.exception.AugException;
import com.aug.common.jwt.JwtHelper;
import com.aug.common.result.Result;
import com.aug.model.system.SysMenu;
import com.aug.model.system.SysUser;
import com.aug.vo.system.LoginVo;
import com.aug.vo.system.RouterVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author querkecor
 * @date 2023/4/25
 */
@Api("后台登录系统")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Resource
    private IndexService indexService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysMenuService sysMenuService;


    @PostMapping("/login")
    public Result<Map<String,Object>> login(@RequestBody LoginVo loginVo) {
        String token = indexService.login(loginVo);

        if (token != null) {
            Map<String,Object> map = new HashMap<>();
            map.put("token",token);
            return Result.success(map);
        } else {
            return Result.fail();
        }
    }

    @GetMapping("info")
    public Result<Map<String,Object>> info(@RequestHeader("token") String token) {
        Map<String, Object> map = new HashMap<>();

        Long userId = JwtHelper.getUserId(token);
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            throw new AugException(201, "用户名不存在");
        }

        List<RouterVo> routerVoList = sysMenuService.findUserMenuListByUserId(userId);
        List<String> permsList = sysMenuService.findUserPermsList(userId);

        map.put("roles", "[admin]");
        map.put("name", user.getUsername());
        map.put("buttons", permsList);
        map.put("routers", routerVoList);
        map.put("avatar", "https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        return Result.success(map);
    }

    @PostMapping("logout")
    public Result<String> logout() {
        return Result.success();
    }
}

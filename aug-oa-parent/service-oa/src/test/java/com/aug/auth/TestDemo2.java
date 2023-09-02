package com.aug.auth;

import com.aug.auth.service.SysRoleService;
import com.aug.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author querkecor
 * @date 2023/4/23
 */

@SpringBootTest
public class TestDemo2 {

    @Resource
    private SysRoleService sysRoleService;

    @Test
    public void getAll() {
        List<SysRole> list = sysRoleService.list();
        for (SysRole sysRole : list) {
            System.out.println(sysRole);
        }
    }
}

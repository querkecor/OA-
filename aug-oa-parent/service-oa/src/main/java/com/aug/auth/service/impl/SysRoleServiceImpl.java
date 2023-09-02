package com.aug.auth.service.impl;

import com.aug.auth.mapper.SysRoleMapper;
import com.aug.auth.service.SysRoleService;
import com.aug.model.system.SysRole;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author querkecor
 * @date 2023/4/23
 */

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
}

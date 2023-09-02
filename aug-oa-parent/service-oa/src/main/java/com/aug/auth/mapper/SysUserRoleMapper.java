package com.aug.auth.mapper;

import com.aug.model.system.SysRole;
import com.aug.model.system.SysUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户角色 Mapper 接口
 * </p>
 *
 * @author querkecor
 * @since 2023-04-26
 */
@Mapper
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysRole> getRoleDataByUserId(Long userId);

}

package com.aug.auth.service;

import com.aug.model.system.SysUser;
import com.aug.vo.system.LoginVo;

/**
 * @author querkecor
 * @date 2023/8/14
 */

public interface IndexService {

    String login(LoginVo loginVo);

}

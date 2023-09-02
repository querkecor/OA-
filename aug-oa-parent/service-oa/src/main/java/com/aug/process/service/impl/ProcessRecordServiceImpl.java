package com.aug.process.service.impl;

import com.aug.auth.service.SysUserService;
import com.aug.common.config.exception.AugException;
import com.aug.model.process.ProcessRecord;
import com.aug.model.system.SysUser;
import com.aug.process.mapper.ProcessRecordMapper;
import com.aug.process.service.ProcessRecordService;
import com.aug.security.custom.LoginUserInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-31
 */
@Service
public class ProcessRecordServiceImpl extends ServiceImpl<ProcessRecordMapper, ProcessRecord> implements ProcessRecordService {

    @Resource
    private SysUserService sysUserService;

    /**
     * 保存节点操作记录
     * @param processId     流程Id
     * @param status        流程当前状态
     * @param description   流程当前节点操作说明
     * @return 操作结果
     */
    @Override
    public boolean record(Long processId, Integer status, String description) {
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        if (sysUser == null) {
            throw new AugException(503,"当前用户访问出错");
        }
        ProcessRecord processRecord = new ProcessRecord();
        processRecord.setProcessId(processId);
        processRecord.setDescription(description);
        processRecord.setStatus(status);
        processRecord.setOperateUserId(sysUser.getId());
        processRecord.setOperateUser(sysUser.getName());
        return this.save(processRecord);
    }
}

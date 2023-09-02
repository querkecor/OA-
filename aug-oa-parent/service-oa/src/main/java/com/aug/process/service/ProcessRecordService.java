package com.aug.process.service;

import com.aug.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-31
 */
public interface ProcessRecordService extends IService<ProcessRecord> {


    boolean record(Long processId,Integer status,String description);
}

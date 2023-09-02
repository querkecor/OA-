package com.aug.process.service;

import com.aug.model.process.ProcessType;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-28
 */
public interface ProcessTypeService extends IService<ProcessType> {

    List<ProcessType> findProcessType();
}

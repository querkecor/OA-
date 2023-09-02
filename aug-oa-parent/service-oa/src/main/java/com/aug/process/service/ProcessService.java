package com.aug.process.service;


import com.aug.model.process.Process;
import com.aug.model.process.ProcessType;
import com.aug.vo.process.ApprovalVo;
import com.aug.vo.process.ProcessFormVo;
import com.aug.vo.process.ProcessQueryVo;
import com.aug.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-29
 */
public interface ProcessService extends IService<Process> {

    IPage<ProcessVo> select(Page<ProcessVo> processPage, ProcessQueryVo processQueryVo);

    void deployByZip(String deployPath);


    void startUp(ProcessFormVo processFormVo);

    IPage<ProcessVo> findPending(Page<Process> pageParam);

    Map<String, Object> show(Long id);

    void approve(ApprovalVo approvalVo);

    IPage<ProcessVo> findProcessed(Page<Process> pageParam);

    IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam);
}

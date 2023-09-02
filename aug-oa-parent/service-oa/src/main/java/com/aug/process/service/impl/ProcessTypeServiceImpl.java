package com.aug.process.service.impl;

import com.aug.common.config.exception.AugException;
import com.aug.model.process.ProcessTemplate;
import com.aug.model.process.ProcessType;
import com.aug.process.mapper.ProcessTypeMapper;
import com.aug.process.service.ProcessTemplateService;
import com.aug.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-28
 */
@Service
public class ProcessTypeServiceImpl extends ServiceImpl<ProcessTypeMapper, ProcessType> implements ProcessTypeService {

    @Resource
    private ProcessTypeService processTypeService;

    @Resource
    private ProcessTemplateService processTemplateService;

    @Override
    public List<ProcessType> findProcessType() {
        List<ProcessType> processTypes = processTypeService.list();

        if (processTypes == null) {
            return null;
        }

        for (ProcessType processType : processTypes) {
            LambdaQueryWrapper<ProcessTemplate> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProcessTemplate::getProcessTypeId,processType.getId());
            wrapper.eq(ProcessTemplate::getStatus,1);
            List<ProcessTemplate> processTemplates = processTemplateService.list(wrapper);
            processType.setProcessTemplateList(processTemplates);
        }

        return processTypes;
    }

}

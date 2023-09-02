package com.aug.process.service.impl;

import com.aug.model.process.ProcessTemplate;
import com.aug.model.process.ProcessType;
import com.aug.process.mapper.ProcessTemplateMapper;
import com.aug.process.mapper.ProcessTypeMapper;
import com.aug.process.service.ProcessService;
import com.aug.process.service.ProcessTemplateService;
import com.aug.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 审批模板 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-28
 */
@Service
public class ProcessTemplateServiceImpl extends ServiceImpl<ProcessTemplateMapper, ProcessTemplate> implements ProcessTemplateService {

    @Resource
    private ProcessTypeService processTypeService;

    @Resource
    private ProcessService processService;

    @Override
    public IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> processTemplatePage) {
        Page<ProcessTemplate> selectPage = baseMapper.selectPage(processTemplatePage, null);
        List<ProcessTemplate> processTemplates = selectPage.getRecords();
        List<Long> processTypeId = new ArrayList<>();
        for (ProcessTemplate processTemplate : processTemplates) {
            processTypeId.add(processTemplate.getProcessTypeId());
        }
        List<ProcessType> processTypes = processTypeService.getBaseMapper().selectBatchIds(processTypeId);
        List<ProcessTemplate> collect = processTemplates.stream()
                .map(ptl -> processTypes.stream()
                        .filter(pt -> pt != null && Objects.equals(ptl.getProcessTypeId(), pt.getId()))
                        .findFirst()
                        .map(pt -> {
                            ptl.setProcessTypeName(pt.getName());
                            return ptl;
                        }).orElse(null))
                .collect(Collectors.toList());
        return selectPage.setRecords(collect);
    }

    @Override
    public boolean publish(Long id) {
        ProcessTemplate processTemplate = this.getById(id);
        processTemplate.setStatus(1);

        // 流程定义部署
        if (!StringUtils.isEmpty(processTemplate.getProcessDefinitionPath())) {
            processService.deployByZip(processTemplate.getProcessDefinitionPath());
        }

        return this.updateById(processTemplate);
    }
}

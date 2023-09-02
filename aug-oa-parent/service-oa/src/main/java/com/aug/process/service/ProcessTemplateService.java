package com.aug.process.service;

import com.aug.model.process.ProcessTemplate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批模板 服务类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-28
 */
public interface ProcessTemplateService extends IService<ProcessTemplate> {

    IPage<ProcessTemplate> selectPage(Page<ProcessTemplate> processTemplatePage);

    boolean publish(Long id);
}


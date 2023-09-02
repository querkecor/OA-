package com.aug.process.controller;


import com.aug.common.result.Result;
import com.aug.model.process.Process;
import com.aug.process.service.ProcessService;
import com.aug.vo.process.ProcessQueryVo;
import com.aug.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author querkecor
 * @since 2023-08-29
 */
@Api(tags = "审批流管理")
@RestController
@RequestMapping(value = "/admin/process")
public class ProcessController {

    @Resource
    private ProcessService processService;

    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable("page") Long page,
                        @PathVariable("limit") Long limit,
                        ProcessQueryVo processQueryVo) {

        Page<ProcessVo> processPage = new Page<>(page, limit);
        IPage<ProcessVo> processModel = processService.select(processPage, processQueryVo);
        if (processModel != null) {
            return Result.success(processModel);
        } else {
            return Result.fail();
        }
    }

}


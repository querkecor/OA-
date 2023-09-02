package com.aug.process.controller;


import com.aug.common.result.Result;
import com.aug.model.process.ProcessType;
import com.aug.process.service.ProcessTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 审批类型 前端控制器
 * </p>
 *
 * @author querkecor
 * @since 2023-08-28
 */
@Api(value = "审批类型", tags = "审批类型")

@RestController
@RequestMapping(value = "/admin/process/processType")
public class ProcessTypeController {

    @Resource
    private ProcessTypeService processTypeService;

    @ApiOperation(value = "获取全部审批分类")
    @GetMapping("findAll")
    public Result<List<ProcessType>> findAll() {
        List<ProcessType> list = processTypeService.list();
        if (list != null) {
            return Result.success(list);
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result<IPage<ProcessType>> index(@PathVariable("page")Long page,
                        @PathVariable("limit")Long limit) {

        Page<ProcessType> pageParam = new Page<>(page, limit);
        IPage<ProcessType> processTypePage = processTypeService.page(pageParam);
        return Result.success(processTypePage);
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result<ProcessType> get(@PathVariable Long id) {
        ProcessType processType = processTypeService.getById(id);
        if (processType != null) {
            return Result.success(processType);
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result<String> save(@RequestBody ProcessType processType) {
        boolean save = processTypeService.save(processType);
        if (save) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }


    @ApiOperation(value = "修改")
    @PutMapping("update")
    public Result<String> updateById(@RequestBody ProcessType processType) {
        boolean update = processTypeService.updateById(processType);
        if (update) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }


    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result<String> remove(@PathVariable Long id) {
        boolean remove = processTypeService.removeById(id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

}


package com.aug.process.controller;


import com.aug.common.config.exception.AugException;
import com.aug.common.result.Result;
import com.aug.model.process.ProcessTemplate;
import com.aug.process.service.ProcessTemplateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 审批模板 前端控制器
 * </p>
 *
 * @author querkecor
 * @since 2023-08-28
 */
@RestController
@RequestMapping(value = "/admin/process/processTemplate")
public class ProcessTemplateController {

    @Resource
    private ProcessTemplateService processTemplateService;

    @ApiOperation(value = "获取分页列表")
    @GetMapping("{page}/{limit}")
    public Result<IPage<ProcessTemplate>> index(@PathVariable("page") Long page,
                                               @PathVariable("limit") Long limit) {
        Page<ProcessTemplate> processTemplatePage = new Page<>(page, limit);
        IPage<ProcessTemplate> selectPage = processTemplateService.selectPage(processTemplatePage);
        if (selectPage != null) {
            return Result.success(selectPage);
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "获取")
    @GetMapping("get/{id}")
    public Result<ProcessTemplate> get(@PathVariable Long id) {
        ProcessTemplate processTemplate = processTemplateService.getById(id);
        if (processTemplate != null) {
            return Result.success(processTemplate);
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "新增")
    @PostMapping("save")
    public Result<String> save(@RequestBody ProcessTemplate processTemplate) {
        boolean save = processTemplateService.save(processTemplate);
        if (save) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "修改")
    @PostMapping("update")
    public Result<String> update(@RequestBody ProcessTemplate processTemplate) {
        boolean update = processTemplateService.updateById(processTemplate);
        if (update) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping("remove/{id}")
    public Result<String> remove(@PathVariable("id") Long id) {
        boolean remove = processTemplateService.removeById(id);
        if (remove) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "上传流程定义")
    @PostMapping("/uploadProcessDefinition")
    public Result uploadProcessDefinition(MultipartFile file) {
        try {
            String path = new File(ResourceUtils.
                                    getURL("classpath:").
                                    getPath()).
                                    getAbsolutePath();
            File templatePath = new File(path + "/processes/");
            boolean mkdirResult = true;
            if (!templatePath.exists()) {
                mkdirResult = templatePath.mkdir();
            }
            if (mkdirResult) {
                String fileName = file.getOriginalFilename();
                if (fileName == null) {
                    return Result.fail().message("上传出错");
                }
                File zipFile = new File(path + "/processes/" + fileName);
                file.transferTo(zipFile);
                Map<String, Object> map = new HashMap<>();

                //根据上传地址后续部署流程定义，文件名称为流程定义的默认key
                map.put("processDefinitionPath", "processes/" + fileName);
                map.put("processDefinitionKey", fileName.substring(0, fileName.lastIndexOf(".")));
                return Result.success(map);
            }
            return Result.fail().message("上传失败");
        } catch (Exception e) {
            return Result.fail().message("上传失败");
        }
    }

    @ApiOperation(value = "发布")
    @GetMapping("/publish/{id}")
    public Result publish(@PathVariable Long id) {
        boolean publish = processTemplateService.publish(id);
        if (publish) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}


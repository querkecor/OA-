package com.aug.process.controller.api;

import com.aug.auth.service.SysUserService;
import com.aug.common.result.Result;
import com.aug.model.process.Process;
import com.aug.model.process.ProcessTemplate;
import com.aug.model.process.ProcessType;
import com.aug.process.service.ProcessRecordService;
import com.aug.process.service.ProcessService;
import com.aug.process.service.ProcessTemplateService;
import com.aug.process.service.ProcessTypeService;
import com.aug.vo.process.ApprovalVo;
import com.aug.vo.process.ProcessFormVo;
import com.aug.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author querkecor
 * @date 2023/8/30
 */
@Api(tags = "审批流管理")
@RestController
@RequestMapping(value="/admin/process")
@CrossOrigin  //跨域
public class ProcessApiController {

    @Resource
    private ProcessTypeService ProcessTypeService;

    @Resource
    private ProcessTemplateService processTemplateService;

    @Resource
    private SysUserService SysUserService;

    @Resource
    private ProcessService processService;

    @GetMapping("/findPending/{page}/{limit}")
    public Result findPending(@PathVariable("page")Long page,
                              @PathVariable("limit")Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pending = processService.findPending(pageParam);
        if (!Objects.isNull(pending)) {
            return Result.success(pending);
        } else {
            return Result.fail();
        }
    }

    @PostMapping("startUp")
    public Result startUp(@RequestBody ProcessFormVo processFormVo) {
        processService.startUp(processFormVo);
        return Result.success();
    }

    @GetMapping("findProcessType")
    public Result findProcessTye() {
        List<ProcessType>  processTypes= ProcessTypeService.findProcessType();
        if (processTypes != null) {
            return Result.success(processTypes);
        } else {
            return Result.fail().message("暂无模板，请先联系管理添加模板");
        }
    }

    @GetMapping("getProcessTemplate/{processTemplateId}")
    public Result getProcessTemplate(@PathVariable("processTemplateId")Long processTemplateId) {
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        if (processTemplate != null) {
            return Result.success(processTemplate);
        } else {
            return Result.fail().message("找不到该条记录");
        }
    }

    @GetMapping("show/{id}")
    public Result show(@PathVariable("id")Long id) {
        Map<String,Object> map = processService.show(id);
        return Result.success(map);
    }

    @PostMapping("approve")
    public Result approve(@RequestBody ApprovalVo approvalVo) {
        processService.approve(approvalVo);
        return Result.success();
    }

    @GetMapping("findProcessed/{page}/{limit}")
    public Result findProcessed(@PathVariable("page")Long page,
                                @PathVariable("limit")Long limit) {
        Page<Process> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findProcessed(pageParam);
        return Result.success(pageModel);
    }

    @ApiOperation(value = "已发起")
    @GetMapping("/findStarted/{page}/{limit}")
    public Result findStarted(@PathVariable Long page,
                              @PathVariable Long limit) {
        Page<ProcessVo> pageParam = new Page<>(page, limit);
        IPage<ProcessVo> pageModel = processService.findStarted(pageParam);
        return Result.success(pageModel);
    }

    @GetMapping("getCurrentUser")
    public Result getCurrentUser() {
       Map<String,Object> map =  SysUserService.getCurrentUser();
       return Result.success(map);
    }
}

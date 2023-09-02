package com.aug.process.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aug.auth.service.SysUserService;
import com.aug.common.config.exception.AugException;
import com.aug.model.process.Process;
import com.aug.model.process.ProcessRecord;
import com.aug.model.process.ProcessTemplate;
import com.aug.model.process.ProcessType;
import com.aug.model.system.SysUser;
import com.aug.process.mapper.ProcessMapper;
import com.aug.process.service.ProcessRecordService;
import com.aug.process.service.ProcessService;
import com.aug.process.service.ProcessTemplateService;
import com.aug.process.service.ProcessTypeService;
import com.aug.security.custom.LoginUserInfoHelper;
import com.aug.vo.process.ApprovalVo;
import com.aug.vo.process.ProcessFormVo;
import com.aug.vo.process.ProcessQueryVo;
import com.aug.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * <p>
 * 审批类型 服务实现类
 * </p>
 *
 * @author querkecor
 * @since 2023-08-29
 */
@Service
@Slf4j
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private HistoryService historyService;

    @Resource
    private TaskService taskService;

    @Resource
    private ProcessTemplateService processTemplateService;

    @Resource
    private ProcessRecordService processRecordService;

    @Resource
    private SysUserService sysUserService;

    @Override
    public IPage<ProcessVo> select(Page<ProcessVo> processPage, ProcessQueryVo processQueryVo) {

        return baseMapper.selectPageVo(processPage, processQueryVo);
    }

    @Override
    public void deployByZip(String deployPath) {
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(deployPath);
        if (inputStream == null) {
            throw new AugException(402, "发布错误，找不到当前发布任务所需的流程定义信息");
        }
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 流程部署
        Deployment deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream).deploy();
        log.info(deployment.getId());
        log.info(deployment.getName());
    }

    @Override
    public void startUp(ProcessFormVo processFormVo) {
        // 根据Id获取对应的对象
        Long userId = LoginUserInfoHelper.getUserId();
        SysUser sysUser = sysUserService.getById(userId);
        Long processTemplateId = processFormVo.getProcessTemplateId();
        ProcessTemplate processTemplate = processTemplateService.getById(processTemplateId);
        // 设置process对象属性值
        Process process = new Process();
        BeanUtils.copyProperties(processFormVo, process);
        String workNo = System.currentTimeMillis() + "";
        process.setProcessCode(workNo);
        process.setUserId(userId);
        process.setStatus(1);
        process.setTitle(sysUser.getName() + "发起" + processTemplate.getName() + "申请");

        // 记录流程任务信息
        this.save(process);

        String processDefinitionKey = processTemplate.getProcessDefinitionKey();
        String businessKey = String.valueOf(process.getId());
        String formValues = processFormVo.getFormValues();
        JSONObject jsonObject = JSON.parseObject(formValues);
        JSONObject formDate = jsonObject.getJSONObject("formData");
        Map<String, Object> map = new HashMap<>(formDate);

        // 开启任务流程实例
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, map);

        List<Task> currentTaskList = this.getCurrentTaskList(processInstance.getProcessInstanceId());
        List<String> assigneeList = new ArrayList<>();
        for (Task task : currentTaskList) {
            assigneeList.add(task.getAssignee());

            // TODO 微信公众号推送信息通知审批人
        }
        process.setDescription("等待" + StringUtils.join(assigneeList, ',') + "审批");
        process.setProcessInstanceId(processInstance.getProcessInstanceId());
        // 更新流程任务信息
        this.updateById(process);

        // 记录任务发起节点信息
        processRecordService.record(process.getId(), 1, "发起申请");

    }

    /**
     * 根据当前用户查询其所需处理的task任务，并传化ProcessVo返回
     *
     * @param pageParam 这里所需，只是为了分页数据，如不设置分页，可不入参
     * @return 当前用户所需处理的task任务，以IPage分页形式返回
     */
    @Override
    public IPage<ProcessVo> findPending(Page<Process> pageParam) {
        TaskQuery query = taskService.createTaskQuery()
                                     .taskAssignee(LoginUserInfoHelper.getUsername())
                                     .orderByTaskCreateTime().desc();

        List<Task> taskList = query.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize());
        long totalCount = query.count();

        List<ProcessVo> processList = new ArrayList<>();
        // 根据流程的业务Id查询实体并关联
        for (Task item : taskList) {
            String processInstanceId = item.getProcessInstanceId();
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (processInstance == null) {
                continue;
            }
            // 业务key
            String businessKey = processInstance.getBusinessKey();
            if (businessKey == null) {
                continue;
            }
            Process process = this.getById(Long.parseLong(businessKey));
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process, processVo);
            processVo.setTaskId(item.getId());
            processVo.setName(sysUserService.getById(process.getUserId()).getName());
            processList.add(processVo);
        }
        IPage<ProcessVo> page = new Page<>(pageParam.getCurrent(), pageParam.getSize(), totalCount);
        page.setRecords(processList);
        return page;

    }

    @Override
    public Map<String, Object> show(Long id) {
        Map<String, Object> map = new HashMap<>();

        Process process = this.getById(id);
        LambdaQueryWrapper<ProcessRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ProcessRecord::getProcessId, process.getId());
        List<ProcessRecord> processRecordList = processRecordService.list(wrapper);

        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());

        List<Task> taskList = taskService.createTaskQuery().processInstanceId(process.getProcessInstanceId()).list();
        boolean isApprove = false;
        if (taskList != null) {
            for (Task task : taskList) {
                if (Objects.equals(task.getAssignee(), LoginUserInfoHelper.getUsername())) {
                    isApprove = true;
                    break;
                }
            }
        }
        map.put("process", process);
        map.put("processRecordList", processRecordList);
        map.put("processTemplate", processTemplate);
        map.put("isApprove", isApprove);

        return map;
    }

    /**
     * 根据 approvalVo 中的status值对process进行审批
     * 其中approvalVo中的taskId为this.findPending()方法传递至前端中保存
     *
     * @param approvalVo 审批所需的数据
     */
    @Override
    public void approve(ApprovalVo approvalVo) {
        SysUser sysUser = sysUserService.getById(LoginUserInfoHelper.getUserId());
        Integer status = approvalVo.getStatus();
        String description;
        if (status == 2) {
            taskService.complete(approvalVo.getTaskId());
            description = sysUser.getName() + "通过审批";
        } else if (status == -1) {
            this.endTask(approvalVo.getTaskId());
            description = sysUser.getName() + "驳回申请";
        } else {
            throw new AugException(403, "非法操作");
        }
        processRecordService.record(approvalVo.getProcessId(), approvalVo.getStatus(), description);

        Process process = this.getById(approvalVo.getProcessId());
        String processInstanceId = this.getById(approvalVo.getProcessId()).getProcessInstanceId();
        List<Task> taskList = this.getCurrentTaskList(processInstanceId);
        if (!CollectionUtils.isEmpty(taskList)) {
            List<String> assigneeList = new ArrayList<>();
            for (Task task : taskList) {
                assigneeList.add(task.getAssignee());

                // TODO 公众号推送消息
            }
            process.setDescription("等待" + StringUtils.join(assigneeList, ',') + "审批");
            process.setStatus(1);
        } else {
            if (approvalVo.getStatus() == 2) {
                process.setDescription("审批完成（同意）");
                process.setStatus(2);
            } else {
                process.setDescription("审批完成（拒绝）");
                process.setStatus(-1);
            }

        }
        // 推送消息给申请人
        this.updateById(process);

    }

    @Override
    public IPage<ProcessVo> findProcessed(Page<Process> pageParam) {
        HistoricTaskInstanceQuery historicTaskInstanceQuery = historyService.createHistoricTaskInstanceQuery()
                                                                            .taskAssignee(LoginUserInfoHelper.getUsername())
                                                                            .finished()
                                                                            .orderByTaskCreateTime().desc();
        List<HistoricTaskInstance> historicTaskList = historicTaskInstanceQuery.listPage((int) ((pageParam.getCurrent() - 1) * pageParam.getSize()), (int) pageParam.getSize());
        long total = historicTaskInstanceQuery.count();

        List<ProcessVo> processVoList = new ArrayList<>();
        for (HistoricTaskInstance historicTask : historicTaskList) {
            LambdaQueryWrapper<Process> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Process::getProcessInstanceId,historicTask.getProcessInstanceId());
            Process process = this.getOne(wrapper);
            ProcessVo processVo = new ProcessVo();
            BeanUtils.copyProperties(process,processVo);

            processVoList.add(processVo);
        }

        Page<ProcessVo> pageModel = new Page<>(pageParam.getCurrent(), pageParam.getSize(), total);
        pageModel.setRecords(processVoList);
        return pageModel;
    }

    @Override
    public IPage<ProcessVo> findStarted(Page<ProcessVo> pageParam) {
        ProcessQueryVo processQueryVo = new ProcessQueryVo();
        processQueryVo.setUserId(LoginUserInfoHelper.getUserId());
        IPage<ProcessVo> pageModel = baseMapper.selectPageVo(pageParam, processQueryVo);
        return pageModel;
    }

    /**
     * 流程结束方法
     *
     * @param taskId 当前任务节点Id
     */
    private void endTask(String taskId) {
        //  当前任务
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        List endEventList = bpmnModel.getMainProcess().findFlowElementsOfType(EndEvent.class);
        // 并行任务可能为null
        if (CollectionUtils.isEmpty(endEventList)) {
            return;
        }
        FlowNode endFlowNode = (FlowNode) endEventList.get(0);
        FlowNode currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(task.getTaskDefinitionKey());

        //  临时保存当前活动的原始方向
        List originalSequenceFlowList = new ArrayList<>();
        originalSequenceFlowList.addAll(currentFlowNode.getOutgoingFlows());
        //  清理活动方向
        currentFlowNode.getOutgoingFlows().clear();

        //  建立新方向
        SequenceFlow newSequenceFlow = new SequenceFlow();
        newSequenceFlow.setId("newSequenceFlowId");
        newSequenceFlow.setSourceFlowElement(currentFlowNode);
        newSequenceFlow.setTargetFlowElement(endFlowNode);
        List newSequenceFlowList = new ArrayList<>();
        newSequenceFlowList.add(newSequenceFlow);
        //  当前节点指向新的方向
        currentFlowNode.setOutgoingFlows(newSequenceFlowList);

        //  完成当前任务
        taskService.complete(task.getId());
    }

    // 当前任务列表
    private List<Task> getCurrentTaskList(String processInstanceId) {
        return taskService.createTaskQuery().processInstanceId(processInstanceId).list();
    }


}

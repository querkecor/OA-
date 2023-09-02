package com.aug.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author querkecor
 * @date 2023/8/26
 */

@SpringBootTest
public class ProcessTest {

    @Resource
    private RepositoryService repositoryService;

    @Resource
    private RuntimeService runtimeService;

    @Resource
    private TaskService taskService;

    @Resource
    private HistoryService historyService;

    @Test
    public void deployProcess() {
        Deployment deploy = repositoryService
                .createDeployment()
                .addClasspathResource("process/ask-for-leave.bpmn20.xml")
                .addClasspathResource("process/ask-for-leave.png")
                .name("请假申请流程")
                .deploy();

        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    @Test
    public void startUPProcess() {
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ask_for_leave");
        System.out.println("流程定义Id： "+processInstance.getProcessDefinitionId());
        System.out.println("流程实例Id： "+processInstance.getId());
        System.out.println("流程活动Id： "+processInstance.getActivityId());
    }

    @Test
    public void findTaskList() {
        String assign = "zhangsan";
        List<Task> list = taskService.createTaskQuery()
                .taskAssignee(assign)
                .list();

        for (Task task : list) {
            System.out.println(task.getProcessDefinitionId());
            System.out.println(task.getId());
            System.out.println(task.getAssignee());
            System.out.println(task.getName());
        }
    }

    @Test
    public void completeTask() {
        String assign = "zhangsan";
        Task task = taskService.createTaskQuery()
                .taskAssignee(assign)
                .singleResult();

        taskService.complete(task.getId());
    }

    @Test
    public void findCompleteTaskList() {
        String assign = "zhangsan";
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(assign)
                .finished()
                .list();

        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println(historicTaskInstance.getProcessInstanceId());
            System.out.println(historicTaskInstance.getId());
            System.out.println(historicTaskInstance.getAssignee());
            System.out.println(historicTaskInstance.getName());

        }
    }

}

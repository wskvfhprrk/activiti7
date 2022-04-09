package com.hejz;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;

import java.util.List;

public class ActivitiTest {

    /**
     * 使用activiti提供的就认方式米创建。9sq1的表
     */
    /*@Test
    public void createTable() {
        *//**
     * 需要使用avtiviti提供的工具类 ProcessEngines,使用方法getDefaultProcessEngine
     * getDefaultProcessEngine会默认从resources下读取名为actviti.cfg.xml
     * 根据建表规则会如果没有25张表的情况下会建表的
     *//*
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        System.out.println("初始化完成，建表");
        System.out.println("processEngine==" + processEngine);
    }*/

    /**
     * 创建流程
     */
    @Test
    public void CreateProcessEngine() {
        /**
         * 需要使用avtiviti提供的工具类 ProcessEngines,使用方法getDefaultProcessEngine
         * getDefaultProcessEngine会默认从resources下读取名为actviti.cfg.xml
         * 根据建表规则会如果没有25张表的情况下会建表的
         */
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        Deployment deployment = processEngine.getRepositoryService().createDeployment()
                .name("请假流程")
                //名字必须以.bpmn结尾，如果.xml结尾流程不往下走
                .addClasspathResource("bpmn/mybpmn.bpmn20.bpmn")
                .addClasspathResource("bpmn/mybpmn.png")
                .deploy();
        System.out.println(deployment);
    }

    /**
     * 启动流程实例
     */
    @Test
    public void startProcessEngine() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        ProcessInstance instance = processEngine.getRuntimeService().startProcessInstanceByKey("mybpmn");
        System.out.println(instance.getProcessDefinitionId());
        System.out.println(instance.getProcessDefinitionName());
    }

    /**
     * 根据用户查询该用户的待办任务
     */
    @Test
    public void searchProcessEngine() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<Task> list = processEngine.getTaskService().createTaskQuery().processDefinitionKey("mybpmn").taskAssignee("zs").list();
        for (Task task : list) {
            System.out.println(task.getId());
            System.out.println(task.getName());
        }
    }

    /**
     * 用户查询并完成任务
     */
    @Test
    public void completeTask() {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        获取taskService
        TaskService taskService = processEngine.getTaskService();
//      根据key和assignee查询任务
        String assignee = "ww";
        List<Task> list = taskService.createTaskQuery().processDefinitionKey("mybpmn").taskAssignee(assignee).list();
        if (list.isEmpty()) {
            System.out.println(assignee + "没有任务");
            return;
        }
        for (Task task : list) {
            System.out.println(task.getId());
            System.out.println(task.getName());
            System.out.println(task.getAssignee());
            //完成任务
            taskService.complete(task.getId());
            System.out.println(task.getId() + "任务已经完成");
        }
    }

    /**
     * 删除ProcessDefinition
     */
    @Test
    public void deleteProcessEngine(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        List<ProcessDefinition> list = processEngine.getRepositoryService().createProcessDefinitionQuery().processDefinitionKey("mybpmn").list();
        if(list.isEmpty())return;
        for (ProcessDefinition definition : list) {
            System.out.println(definition.getId());
            System.out.println(definition.getName());
            System.out.println(definition.getDeploymentId());
            //根据DeploymentId删除ProcessDefinition
            processEngine.getRepositoryService().deleteDeployment(definition.getDeploymentId());
        }

    }
}

# OA办公系统
技术栈：SpringBoot+MyBatisPlus+Spring Security+Redis+Activiti+MySQL+Vue

1.后端工程架构：
​父工程：guigu-oa-parent 依赖版本
  1).​子模块 common(公共模块)
    (1).​common-util 核心工具类
    (2).​service-util 业务模块工具类
    (3).spring-sercurity 认证授权模块
​  2).子模块 model 实体类
  3).子模块 service-oa 业务模块
  
2.项目功能模块：
  1).管理端
    (1).系统管理
      ①.用户管理
      ②.角色管理
      ③.权限管理
      ④.菜单管理
    (2).审批模块
      ①.审批类型管理
      ②.审批模板管理
      ③.审批列表
    (3).公众号菜单管理
  2).员工端
    (1).微信授权登录
    (2).显示所有审批类型和模板
    (3).发起申请
    (4).消息推送
    (5).待处理和已处理
    (6).查询审批详情和审批操作

技术点：
认证授权：Spring Security+JWT+Redis 实现单点登录，权限权限管理
自动化审批工作流：Activiti 实现自动化审批管理



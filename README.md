# onlineJudgeExam
spring boot, vue, online judge, program

f**k csdn, 8964 8964

#### 介绍
基于SpringBoot+Vue3前后分离项目的在线考试系统；

#### 软件架构
前端：

+ vue3
+ typescript
+ vue-router
+ pinia
+ vite

后端：

+ SpringBoot 2.7
+ SpringSecurity
+ Mybatis
+ MySQL 5.7
+ Redis

#### 测试账号
> 教师：
>  + 账号：teacher
>  + 密码：123456
>
> 学生
>  + 账号：student
>  + 密码：123456


#### 安装教程

> 前端采用pnpm包管理工具、后端采用maven管理依赖

+ 启动后端服务（确保安装MySQL以及Redis）
  + 打开exam_springboot项目
  + 利用maven下载依赖
  + 导入数据库脚本`online_exam.sql`
  + 修改配置`exam-springboot/src/main/resources/application.yml`
    + 数据库连接
    + 邮箱验证信息
      + 修改`username以及password`（需要开通邮箱权限，可自行百度）
  + 运行`com/zz/Application.java`即可

+ 启动前端服务
  + 打开exam_vue项目，执行如下指令：

    ```shell
    # 安装包依赖
    pnpm install
    # 启动服务
    pnpm run dev
    ```


感谢开源社区：
https://github.com/JQ6374/online_exam
https://github.com/criyle/go-judge

package com.it.cloud.activiti.procdef;

import com.it.cloud.modules.activiti.service.IActReModelService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class DeployProcessDefinitionTest {

    @Autowired
    private RepositoryService repositoryService;

    /**
     * 部署流程定义--Classpath
     */
    @Test
    public void deployWithClasspath() {
        Deployment deployment = repositoryService.createDeployment()// 创建部署对象
                .name("helloworld演示")// 流程名称
                .addClasspathResource("diagrams/helloworld.bpmn20.xml")// 加载资源文件，一次只能加载一个文件
                .addClasspathResource("diagrams/helloworld.png")// 可选
                .deploy();// 部署
        System.out.println("部署ID：" + deployment.getId());//1
        System.out.println("部署时间：" + deployment.getDeploymentTime());
    }

    /**
     * 部署流程定义--Inputstream
     */
    @Test
    public void deployWithInputStream() throws FileNotFoundException {
        String bpmnPath = "diagrams/helloworld.bpmn";
        String pngPath = "diagrams/helloworld.png";

        // 读取资源作为一个输入流
        FileInputStream bpmnfileInputStream = new FileInputStream(bpmnPath);
        FileInputStream pngfileInputStream = new FileInputStream(pngPath);

        Deployment deployment = repositoryService.createDeployment()
                .addInputStream("helloworld.bpmn",bpmnfileInputStream)
                .addInputStream("helloworld.png", pngfileInputStream)
                .deploy();
        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署时间："+deployment.getDeploymentTime());
    }

    /**
     * 部署流程定义--zip
     */
    @Test
    public void deployWithZip() {
        // 从classpath获取资源文件
        InputStream inputStream = this.getClass()
                .getClassLoader()
                .getResourceAsStream("diagrams/helloworld.zip");

        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        Deployment deployment = repositoryService.createDeployment() // 创建部署
                .addZipInputStream(zipInputStream) // 添加zip输入流
                .name("helloworld流程") // 流程名称
                .deploy(); // 部署
        System.out.println("流程部署ID:" + deployment.getId());
        System.out.println("流程部署Name:" + deployment.getName());
    }


}

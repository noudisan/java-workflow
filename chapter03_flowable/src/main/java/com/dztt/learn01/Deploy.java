package com.dztt.learn01;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.ui.modeler.service.ModelServiceImpl;
import org.flowable.ui.modeler.serviceapi.ModelService;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.zip.ZipInputStream;

public class Deploy {
    /**
     * 流程资源xml部署：
     */
    public Deployment deploy1(String filePath) {
        try {
            ProcessEngine processEngine = getProcessEngine();

            //流程引擎会将XML文件存储在数据库中，这样可以在需要的时候获取它。
            //流程定义转换为内部的、可执行的对象模型，这样使用它就可以启动流程实例。
            RepositoryService repositoryService = processEngine.getRepositoryService();
            DeploymentBuilder deploymentBuilder = repositoryService
                    .createDeployment()
                    .addClasspathResource(filePath);
            Deployment deployment = deploymentBuilder.deploy();
            // logger.info("成功：部署工作流成：" + filePath);
            return deployment;
        } catch (Exception e) {
            // logger.error("失败：部署工作流：" + e);
            return null;
        } // end catch
    }

    private ProcessEngine getProcessEngine() {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("sa")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        // ProcessEngine由ProcessEngineConfiguration实例创建。该实例可以配置与调整流程引擎的设置。
        return cfg.buildProcessEngine();
    }

    public void deploy2(){
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("diagrams/approve.zip");
        ZipInputStream zipInputStream = new ZipInputStream(in);

        ProcessEngine processEngine = getProcessEngine();
        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
                .createDeployment()//创建部署对象
                .addZipInputStream(zipInputStream)//使用zip方式部署，将approve.bpmn和approve.png压缩成zip格式的文件
                .deploy();//完成部署

        System.out.println("部署ID："+deployment.getId());
        System.out.println("部署时间："+deployment.getDeploymentTime());
    }

    public Object deploy3() throws UnsupportedEncodingException {
       // 通过act_de_model中存放的Modeler内容来部署，例如代码（此代码别人写的，作者不详，但在此感谢此作者）：
        ModelService modelService = new ModelServiceImpl();
        String modelId ="";
        org.flowable.ui.modeler.domain.Model modelData =modelService.getModel(modelId);
        //获取模型
        byte[] bytes = modelService.getBpmnXML(modelData);

        if (bytes == null) {
            return "模型数据为空，请先设计流程并成功保存，再进行发布。";
        }

        BpmnModel model=modelService.getBpmnModel(modelData);
        if(model.getProcesses().size()==0){
            return "数据模型不符要求，请至少设计一条主线流程。";
        }
        byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        //发布流程
        String processName = modelData.getName() + ".bpmn20.xml";

        ProcessEngine processEngine = getProcessEngine();
        RepositoryService repositoryService = processEngine.getRepositoryService();

        Deployment  deploy=  repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes, "UTF-8"))
                .deploy();

        return "部署成功";
    }

}

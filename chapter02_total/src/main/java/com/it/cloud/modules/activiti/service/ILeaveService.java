package com.it.cloud.modules.activiti.service;

import com.it.cloud.common.base.Result;
import com.it.cloud.common.utils.PageUtils;
import com.it.cloud.modules.activiti.entity.ActHiTaskinst;
import com.it.cloud.modules.activiti.entity.LeaveEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.it.cloud.modules.activiti.entity.dto.LeaveApplyDTO;
import com.it.cloud.modules.activiti.entity.dto.LeaveOperateDTO;
import org.activiti.engine.history.HistoricActivityInstance;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 司马缸砸缸了
 * @since 2019-08-28
 */
public interface ILeaveService extends IService<LeaveEntity> {

    /**
     * 我的申请
     *
     * @param params
     * @return
     */
    PageUtils queryMyApplyPage(Map<String, Object> params);

    /**
     * 保存请假单
     *
     * @param leaveEntity
     */
    void saveLeave(LeaveEntity leaveEntity);

    /**
     * 提交申请
     *
     * @param leaveApplyDTO
     * @return
     */
    Result apply(LeaveApplyDTO leaveApplyDTO);

    /**
     * 待办任务
     *
     * @param params
     * @return
     */
    PageUtils queryTodoPage(Map<String, Object> params);

    /**
     * 已办任务
     *
     * @param params
     * @return
     */
    PageUtils queryDonePage(Map<String, Object> params);

    /**
     * 运行中的流程
     *
     * @param params
     * @return
     */
    PageUtils queryRunningPage(Map<String, Object> params);

    /**
     * 结束的流程
     *
     * @param params
     * @return
     */
    PageUtils queryFinishPage(Map<String, Object> params);

    /**
     * 历史活动节点
     *
     * @param taskId
     * @return
     */
    List<HistoricActivityInstance> historyActivity(String taskId);

    /**
     * 历史任务
     *
     * @param id
     * @return
     */
    List<ActHiTaskinst> historyTaskList(String id);

    /**
     * 完成任务
     *
     * @param leaveOperate
     * @return
     */
    Result complete(LeaveOperateDTO leaveOperate);
}

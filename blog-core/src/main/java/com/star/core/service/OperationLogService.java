package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.OperationLogDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.OperationLog;
import com.star.core.vo.ConditionVO;

/**
 * @Author: zzStar
 * @Date: 2021/8/21
 * @Description:
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 查询日志列表
     *
     * @param condition 条件
     * @return 日志列表
     */
    PageData<OperationLogDTO> listOperationLogs(ConditionVO condition);

}

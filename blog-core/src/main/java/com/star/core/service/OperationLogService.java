package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.OperationLogDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.OperationLog;
import com.star.core.entity.Resource;
import com.star.core.entity.UserInfo;
import com.star.core.vo.ConditionVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @Author: zzStar
 * @Date: 2021/8/21
 * @Description:
 */
public interface OperationLogService extends IService<OperationLog> {

    /**
     * 查询操作日志
     *
     * @param condition 条件
     * @return 操作日志
     */
    PageData<OperationLogDTO> listOperationLogs(ConditionVO condition);

    /**
     * 埋点操作日志
     *
     * @param request
     * @param response
     * @param user
     * @param resource
     * @throws Exception
     */
    void pointOperationLog(HttpServletRequest request, HttpServletResponse response, UserInfo user, Resource resource) throws Exception;

}

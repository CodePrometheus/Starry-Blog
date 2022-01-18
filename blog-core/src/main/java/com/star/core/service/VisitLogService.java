package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.PageData;
import com.star.core.dto.VisitLogDTO;
import com.star.core.entity.Resource;
import com.star.core.entity.UserInfo;
import com.star.core.entity.VisitLog;
import com.star.core.vo.ConditionVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zzStar
 * @Date: 01-04-2022 01:40
 */
public interface VisitLogService extends IService<VisitLog> {

    /**
     * 查询访问日志
     *
     * @param condition 条件
     * @return 访问日志
     */
    PageData<VisitLogDTO> listVisitLogs(ConditionVO condition);

    /**
     * 埋点访问日志
     *
     * @param request
     * @param response
     * @param user
     * @param resource
     * @throws Exception
     */
    void pointVisitLog(HttpServletRequest request, HttpServletResponse response, UserInfo user, Resource resource) throws Exception;

}

package com.star.core.hook;

import com.star.core.entity.Resource;
import com.star.core.entity.UserInfo;
import com.star.core.service.OperationLogService;
import com.star.core.service.VisitLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zzStar
 * @Date: 01-03-2022 22:18
 */
public class DefaultLogPointerImpl implements LogPointer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLogPointerImpl.class);

    @javax.annotation.Resource
    private VisitLogService visitLogService;

    @javax.annotation.Resource
    private OperationLogService operationLogService;

    public DefaultLogPointerImpl(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    @Override
    public void doPoint(HttpServletRequest request, HttpServletResponse response, UserInfo user, Resource resource, boolean isAdmin) {
        try {
            if (isAdmin) {
                operationLogService.pointOperationLog(request, response, user, resource);
            } else {
                visitLogService.pointVisitLog(request, response, user, resource);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}

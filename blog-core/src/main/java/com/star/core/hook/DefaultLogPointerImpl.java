package com.star.core.hook;

import com.star.admin.service.AdminOperationLogService;
import com.star.admin.service.VisitLogService;
import com.star.inf.entity.BlogResource;
import com.star.inf.entity.UserInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: zzStar
 * @Date: 01-03-2022 22:18
 */
public class DefaultLogPointerImpl implements LogPointer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultLogPointerImpl.class);

    @Resource
    private VisitLogService visitLogServiceImpl;

    @Resource
    private AdminOperationLogService operationLogServiceImpl;

    public DefaultLogPointerImpl(AdminOperationLogService operationLogService) {
        this.operationLogServiceImpl = operationLogService;
    }

    @Override
    public void doPoint(HttpServletRequest request, HttpServletResponse response, UserInfo user, BlogResource blogResource, boolean isAdmin) {
        try {
            if (isAdmin) {
                operationLogServiceImpl.pointOperationLog(request, response, user, blogResource);
            } else {
                visitLogServiceImpl.pointVisitLog(request, response, user, blogResource);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
    }

}

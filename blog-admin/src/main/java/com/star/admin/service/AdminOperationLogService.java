package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.IpUtils;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.OperationLogDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.BlogResource;
import com.star.inf.entity.OperationLog;
import com.star.inf.entity.UserInfo;
import com.star.inf.mapper.OperationLogMapper;
import com.star.inf.mapper.ResourceMapper;
import com.star.inf.vo.ConditionVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Author: zzStar
 * @Date: 2021/8/21
 * @Description:
 */
@Service
public class AdminOperationLogService extends ServiceImpl<OperationLogMapper, OperationLog> {

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private ResourceMapper resourceMapper;

    /**
     * 查询操作日志
     *
     * @param condition 条件
     * @return 操作日志
     */
    public PageData<OperationLogDTO> listOperationLogs(ConditionVO condition) {
        Page<OperationLog> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<OperationLog> operationLog = this.page(page, new LambdaQueryWrapper<OperationLog>().
                like(StringUtils.isNotBlank(condition.getKeywords()),
                        OperationLog::getOptMethod, condition.getKeywords()).or().like(StringUtils.isNotBlank(condition.getKeywords()),
                        OperationLog::getOptDesc, condition.getKeywords()).orderByDesc(OperationLog::getCreateTime));
        List<OperationLogDTO> operationLogList = BeanCopyUtils.copyList(operationLog.getRecords(), OperationLogDTO.class);
        return new PageData<>(operationLogList, operationLog.getTotal());
    }

    /**
     * 埋点操作日志
     *
     * @param request
     * @param response
     * @param user
     * @param blogResource
     * @throws Exception
     */
    public void pointOperationLog(HttpServletRequest request, HttpServletResponse response,
                                  UserInfo user, BlogResource blogResource) throws Exception {
        OperationLog log = new OperationLog();
        log.setUserId(user.getId());
        log.setNickname(user.getNickname());
        String ip = IpUtils.getIpAddr(request);
        log.setIpAddr(ip);
        log.setIpSource(IpUtils.getIpSource(ip));

        if (Objects.nonNull(blogResource)) {
            String resourceName = blogResource.getResourceName();
            if (Objects.nonNull(blogResource.getParentId())) {
                BlogResource source = resourceMapper.selectById(blogResource.getParentId());
                resourceName = source.getResourceName();
            }
            log.setOptModule(resourceName);
            log.setOptDesc(blogResource.getResourceName());
        }

        log.setOptUrl(request.getRequestURI());
        log.setRequestParam(objectMapper.writeValueAsString(request.getParameterMap()));
        log.setRequestMethod(Objects.requireNonNull(request).getMethod());
        log.setResponseData(String.valueOf(response.getStatus()));
        log.setCreateTime(LocalDateTime.now());
        this.save(log);
    }

}

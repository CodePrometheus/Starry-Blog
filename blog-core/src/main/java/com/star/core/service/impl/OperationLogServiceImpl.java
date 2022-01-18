package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.common.tool.IpUtil;
import com.star.core.dto.OperationLogDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.OperationLog;
import com.star.core.entity.Resource;
import com.star.core.entity.UserInfo;
import com.star.core.mapper.OperationLogMapper;
import com.star.core.mapper.ResourceMapper;
import com.star.core.service.OperationLogService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.ConditionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Author: zzStar
 * @Date: 2021/8/21
 * @Description:
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @javax.annotation.Resource
    private ObjectMapper objectMapper;
    @javax.annotation.Resource
    private ResourceMapper resourceMapper;

    @Override
    public PageData<OperationLogDTO> listOperationLogs(ConditionVO condition) {
        Page<OperationLog> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<OperationLog> operationLog = this.page(page, new LambdaQueryWrapper<OperationLog>().like(StringUtils.isNotBlank(condition.getKeywords()),
                OperationLog::getOptMethod, condition.getKeywords()).or().like(StringUtils.isNotBlank(condition.getKeywords()),
                OperationLog::getOptDesc, condition.getKeywords()).orderByDesc(OperationLog::getCreateTime));
        List<OperationLogDTO> operationLogList = BeanCopyUtil.copyList(operationLog.getRecords(), OperationLogDTO.class);
        return new PageData<>(operationLogList, operationLog.getTotal());
    }

    @Override
    public void pointOperationLog(HttpServletRequest request, HttpServletResponse response, UserInfo user, Resource resource) throws Exception {
        OperationLog log = new OperationLog();
        log.setUserId(user.getId());
        log.setNickname(user.getNickname());
        String ip = IpUtil.getIpAddr(request);
        log.setIpAddr(ip);
        log.setIpSource(IpUtil.getIpSource(ip));

        if (Objects.nonNull(resource)) {
            String resourceName = resource.getResourceName();
            if (Objects.nonNull(resource.getParentId())) {
                Resource source = resourceMapper.selectById(resource.getParentId());
                resourceName = source.getResourceName();
            }
            log.setOptModule(resourceName);
            log.setOptDesc(resource.getResourceName());
        }

        log.setOptUrl(request.getRequestURI());
        log.setRequestParam(objectMapper.writeValueAsString(request.getParameterMap()));
        log.setRequestMethod(Objects.requireNonNull(request).getMethod());
        log.setResponseData(String.valueOf(response.getStatus()));
        log.setCreateTime(LocalDateTime.now());
        this.save(log);
    }

}

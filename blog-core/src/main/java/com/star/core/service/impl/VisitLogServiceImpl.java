package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.common.tool.IpUtil;
import com.star.core.dto.PageData;
import com.star.core.dto.VisitLogDTO;
import com.star.core.entity.Resource;
import com.star.core.entity.UserInfo;
import com.star.core.entity.VisitLog;
import com.star.core.mapper.VisitLogMapper;
import com.star.core.service.VisitLogService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.ConditionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zzStar
 * @Date: 01-04-2022 01:41
 */
@Service
public class VisitLogServiceImpl extends ServiceImpl<VisitLogMapper, VisitLog> implements VisitLogService {

    @javax.annotation.Resource
    private IpUtil ipUtil;

    @javax.annotation.Resource
    private ObjectMapper objectMapper;

    @Override
    public PageData<VisitLogDTO> listVisitLogs(ConditionVO condition) {
        Page<VisitLog> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<VisitLog> visitLog = this.page(page, new LambdaQueryWrapper<VisitLog>().like(StringUtils.isNotBlank(condition.getKeywords()),
                VisitLog::getRequestMethod, condition.getKeywords()).or().like(StringUtils.isNotBlank(condition.getKeywords()), VisitLog::getVisitDesc,
                condition.getKeywords()).orderByDesc(VisitLog::getCreateTime));
        List<VisitLogDTO> visitLogList = BeanCopyUtil.copyList(visitLog.getRecords(), VisitLogDTO.class);
        return new PageData<>(visitLogList, visitLog.getTotal());
    }

    @Override
    public void pointVisitLog(HttpServletRequest request, HttpServletResponse response, UserInfo user, Resource resource) throws Exception {
        VisitLog log = new VisitLog();
        log.setUserId(user.getId());
        log.setNickname(user.getNickname());
        String ip = IpUtil.getIpAddr(request);
        log.setIpAddr(ip);
        log.setIpSource(IpUtil.getIpSource(ip));
        Map<String, String> userAgentMap = ipUtil.parseOsAndBrowser();
        log.setOs(userAgentMap.get("os"));
        log.setBrowser(userAgentMap.get("browser"));
        log.setContent(resource.getResourceName());
        log.setVisitUrl(request.getRequestURI());
        log.setRequestParam(objectMapper.writeValueAsString(request.getParameterMap()));
        log.setRequestMethod(Objects.requireNonNull(request).getMethod());
        log.setResponseData(String.valueOf(response.getStatus()));
        log.setCreateTime(LocalDateTime.now());
        this.save(log);
    }

}

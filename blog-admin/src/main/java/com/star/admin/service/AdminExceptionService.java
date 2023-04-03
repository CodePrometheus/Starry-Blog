package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.admin.entity.ExceptionLog;
import com.star.admin.entity.ExceptionLogEvent;
import com.star.admin.mapper.ExceptionLogMapper;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.PageData;
import com.star.inf.vo.ConditionVO;
import jakarta.annotation.Resource;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 02-02-2023
 */
@Service
public class AdminExceptionService extends ServiceImpl<ExceptionLogMapper, ExceptionLog> {

    @Resource
    private ExceptionLogMapper exceptionLogMapper;

    @Async
    @EventListener
    public void exceptionLogHook(ExceptionLogEvent event) {
        exceptionLogMapper.insert(((ExceptionLog) event.getSource()));
    }

    /**
     * 异常日志列表
     *
     * @param conditionVO
     * @return
     */
    public PageData<ExceptionLog> getExceptionLogs(ConditionVO conditionVO) {
        Page<ExceptionLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<ExceptionLog> exceptionLogPage = this.page(page, new LambdaQueryWrapper<ExceptionLog>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), ExceptionLog::getOptDesc, conditionVO.getKeywords())
                .orderByDesc(ExceptionLog::getId));
        List<ExceptionLog> exceptionLogs = BeanCopyUtils.copyList(exceptionLogPage.getRecords(), ExceptionLog.class);
        return new PageData<>(exceptionLogs, exceptionLogPage.getTotal());
    }

}


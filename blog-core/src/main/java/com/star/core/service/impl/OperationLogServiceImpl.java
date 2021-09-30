package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.dto.OperationLogDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.OperationLog;
import com.star.core.mapper.OperationLogMapper;
import com.star.core.service.OperationLogService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.ConditionVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2021/8/21
 * @Description:
 */
@Service
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {

    @Override
    public PageData<OperationLogDTO> listOperationLogs(ConditionVO condition) {
        Page<OperationLog> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<OperationLog> operationLog = this.page(page, new LambdaQueryWrapper<OperationLog>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), OperationLog::getOptMethod, condition.getKeywords())
                .or()
                .like(StringUtils.isNotBlank(condition.getKeywords()), OperationLog::getOptDesc, condition.getKeywords())
                .orderByDesc(OperationLog::getCreateTime));
        List<OperationLogDTO> operationLogList = BeanCopyUtil.copyList(operationLog.getRecords(), OperationLogDTO.class);
        return new PageData<>(operationLogList, operationLog.getTotal());
    }

}

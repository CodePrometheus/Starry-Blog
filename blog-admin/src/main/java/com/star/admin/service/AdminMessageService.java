package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtil;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.MessageBackDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.Message;
import com.star.inf.mapper.MessageMapper;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.ReviewVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminMessageService extends ServiceImpl<MessageMapper, Message> {

    @Resource
    private MessageMapper messageMapper;

    /**
     * 查看后台留言
     *
     * @param condition 条件
     * @return 留言列表
     */
    public PageData<MessageBackDTO> listMessageBack(ConditionVO condition) {
        Page<Message> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        LambdaQueryWrapper<Message> messagePageQuery = new LambdaQueryWrapper<Message>().
                like(StringUtils.isNotBlank(condition.getKeywords()),
                        Message::getNickname, condition.getKeywords())
                .eq(Objects.nonNull(condition.getIsReview()), Message::getIsReview, condition.getIsReview())
                .orderByDesc(Message::getUpdateTime);
        Page<Message> messagePage = messageMapper.selectPage(page, messagePageQuery);
        // 转换DTO
        List<MessageBackDTO> messageBackDTOList = BeanCopyUtil.copyList(messagePage.getRecords(), MessageBackDTO.class);
        return new PageData<>(messageBackDTOList, messagePage.getTotal());
    }

    /**
     * 审核留言
     *
     * @param reviewVO 审查签证官
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateMessagesReview(ReviewVO reviewVO) {
        // 修改留言审核状态
        List<Message> messageList = reviewVO.getIdList().stream().map(v -> Message.builder()
                        .id(v).isReview(reviewVO.getIsReview()).build())
                .collect(Collectors.toList());
        this.updateBatchById(messageList);
    }

}

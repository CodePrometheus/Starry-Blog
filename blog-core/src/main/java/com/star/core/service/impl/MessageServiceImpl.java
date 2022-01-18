package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtil;
import com.star.core.dto.MessageBackDTO;
import com.star.core.dto.MessageDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.Message;
import com.star.core.mapper.MessageMapper;
import com.star.core.service.BlogInfoService;
import com.star.core.service.MessageService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.MessageVO;
import com.star.core.vo.ReviewVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.CommonConst.TRUE;

/**
 * @Author: zzStar
 * @Date: 12-20-2020 10:11
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveMessage(MessageVO messageVO) {
        Integer isMessageReview = blogInfoService.getWebsiteConfig().getIsMessageReview();
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        Message message = BeanCopyUtil.copyObject(messageVO, Message.class);
        message.setIpAddress(ipAddr);
        message.setIpSource(ipSource);
        message.setIsReview(isMessageReview == TRUE ? FALSE : TRUE);
        messageMapper.insert(message);
    }

    @Override
    public List<MessageDTO> listMessages() {
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent, Message::getTime)
                .eq(Message::getIsReview, TRUE));
        return BeanCopyUtil.copyList(messageList, MessageDTO.class);
    }

    @Override
    public PageData<MessageBackDTO> listMessageBack(ConditionVO condition) {
        Page<Message> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        LambdaQueryWrapper<Message> messagePageQuery = new LambdaQueryWrapper<Message>().like(StringUtils.isNotBlank(condition.getKeywords()),
                        Message::getNickname, condition.getKeywords())
                .eq(Objects.nonNull(condition.getIsReview()), Message::getIsReview, condition.getIsReview())
                .orderByDesc(Message::getUpdateTime);
        Page<Message> messagePage = messageMapper.selectPage(page, messagePageQuery);
        // 转换DTO
        List<MessageBackDTO> messageBackDTOList = BeanCopyUtil.copyList(messagePage.getRecords(), MessageBackDTO.class);
        return new PageData<>(messageBackDTOList, messagePage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateMessagesReview(ReviewVO reviewVO) {
        // 修改留言审核状态
        List<Message> messageList = reviewVO.getIdList().stream().map(v -> Message.builder()
                        .id(v).isReview(reviewVO.getIsReview()).build())
                .collect(Collectors.toList());
        this.updateBatchById(messageList);
    }
}

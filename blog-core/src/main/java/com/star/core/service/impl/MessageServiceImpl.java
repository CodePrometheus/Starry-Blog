package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtil;
import com.star.core.entity.Message;
import com.star.core.mapper.MessageMapper;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.MessageVO;
import com.star.core.service.MessageService;
import com.star.core.dto.MessageBackDTO;
import com.star.core.dto.MessageDTO;
import com.star.core.dto.PageData;
import com.star.core.util.BeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 12-20-2020 10:11
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveMessage(MessageVO messageVO) {
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        Message message = Message.builder()
                .ipAddress(ipAddr)
                .ipAddress(ipSource)
                .nickname(messageVO.getNickname())
                .avatar(messageVO.getAvatar())
                .messageContent(messageVO.getMessageContent())
                .time(messageVO.getTime())
                .createTime(new Date()).build();
        messageMapper.insert(message);
    }

    @Override
    public List<MessageDTO> listMessages() {
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent, Message::getTime));
        return BeanCopyUtil.copyList(messageList, MessageDTO.class);
    }

    @Override
    public PageData listMessageBackDTO(ConditionVO condition) {
        Page<Message> page = new Page<>(condition.getCurrent(), condition.getSize());
        LambdaQueryWrapper<Message> messagePageQuery = new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getMessageContent, Message::getAvatar,
                        Message::getCreateTime, Message::getIpSource, Message::getIpAddress)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Message::getNickname, condition.getKeywords())
                .orderByDesc(Message::getCreateTime);
        Page<Message> messagePage = messageMapper.selectPage(page, messagePageQuery);
        //转换DTO
        List<MessageBackDTO> messageBackDTOList = BeanCopyUtil.copyList(messagePage.getRecords(), MessageBackDTO.class);
        return new PageData<>(messageBackDTOList, (int) messagePage.getTotal());
    }

}

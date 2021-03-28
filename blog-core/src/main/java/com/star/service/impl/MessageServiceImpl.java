package com.star.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.domain.entity.Message;
import com.star.domain.mapper.MessageMapper;
import com.star.domain.vo.ConditionVO;
import com.star.domain.vo.MessageVO;
import com.star.exception.StarryException;
import com.star.service.MessageService;
import com.star.service.dto.MessageBackDTO;
import com.star.service.dto.MessageDTO;
import com.star.service.dto.PageDTO;
import com.star.tool.IpUtil;
import com.star.util.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 12-20-2020 10:11
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private HttpServletRequest request;

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void saveMessage(MessageVO messageVO) {
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        messageMapper.insert(new Message(messageVO, ipAddr, ipSource));
    }

    @Override
    public List<MessageDTO> listMessages() {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "nickname", "avatar", "message_content", "time");
        return BeanCopyUtil.copyList(messageMapper.selectList(queryWrapper), MessageDTO.class);
    }

    @Override
    public PageDTO listMessageBackDTO(ConditionVO condition) {
        Page<Message> page = new Page<>(condition.getCurrent(), condition.getSize());
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "nickname", "avatar", "ip_address", "ip_source", "message_content", "create_time")
                .like(condition.getKeywords() != null, "nickname", condition.getKeywords())
                .orderByDesc("create_time");
        Page<Message> messagePage = messageMapper.selectPage(page, queryWrapper);
        //转换DTO
        List<MessageBackDTO> messageBackDTOList = BeanCopyUtil.copyList(messagePage.getRecords(), MessageBackDTO.class);
        return new PageDTO(messageBackDTOList, (int) messagePage.getTotal());
    }

}

package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtil;
import com.star.core.domain.entity.Message;
import com.star.core.domain.mapper.MessageMapper;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.MessageVO;
import com.star.core.service.MessageService;
import com.star.core.service.dto.MessageBackDTO;
import com.star.core.service.dto.MessageDTO;
import com.star.core.service.dto.PageDTO;
import com.star.core.util.BeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

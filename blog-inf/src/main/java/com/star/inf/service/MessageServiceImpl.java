package com.star.inf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.IpUtils;
import com.star.inf.dto.MessageDTO;
import com.star.inf.entity.Message;
import com.star.inf.mapper.MessageMapper;
import com.star.inf.vo.MessageVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.CommonConst.TRUE;

/**
 * @Author: zzStar
 * @Date: 12-20-2020 10:11
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IService<Message> {

    @Resource
    private BlogInfoServiceImpl blogInfoServiceImpl;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private HttpServletRequest request;

    /**
     * @param messageVO
     */
    @Transactional(rollbackFor = StarryException.class)
    public void saveMessage(MessageVO messageVO) {
        Integer isMessageReview = blogInfoServiceImpl.getWebsiteConfig().getIsMessageReview();
        String ipAddr = IpUtils.getIpAddr(request);
        String ipSource = IpUtils.getIpSource(ipAddr);
        Message message = BeanCopyUtils.copyObject(messageVO, Message.class);
        message.setIpAddress(ipAddr);
        message.setIpSource(ipSource);
        message.setIsReview(isMessageReview == TRUE ? FALSE : TRUE);
        messageMapper.insert(message);
    }

    /**
     * @return
     */
    public List<MessageDTO> listMessages() {
        List<Message> messageList = messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .select(Message::getId, Message::getNickname, Message::getAvatar, Message::getMessageContent, Message::getTime)
                .eq(Message::getIsReview, TRUE));
        return BeanCopyUtils.copyList(messageList, MessageDTO.class);
    }

}

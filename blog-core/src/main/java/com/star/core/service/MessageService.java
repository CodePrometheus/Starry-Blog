package com.star.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.MessageBackDTO;
import com.star.core.dto.MessageDTO;
import com.star.core.entity.Message;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.MessageVO;
import com.star.core.dto.PageDTO;

import java.util.List;

/**
 * @author zzStar
 */
public interface MessageService extends IService<Message> {

    /**
     * 添加留言弹幕
     *
     * @param messageVO 留言对象
     */
    void saveMessage(MessageVO messageVO);

    /**
     * 查看留言弹幕
     *
     * @return 留言列表
     */
    List<MessageDTO> listMessages();

    /**
     * 查看后台留言
     *
     * @param condition 条件
     * @return 留言列表
     */
    PageDTO<MessageBackDTO> listMessageBackDTO(ConditionVO condition);

}

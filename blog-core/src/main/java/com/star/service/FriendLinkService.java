package com.star.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.domain.entity.FriendLink;
import com.star.domain.vo.ConditionVO;
import com.star.service.dto.FriendLinkBackDTO;
import com.star.service.dto.FriendLinkDTO;
import com.star.service.dto.PageDTO;

import java.util.List;

/**
 * @author zzStar
 */
public interface FriendLinkService extends IService<FriendLink> {

    /**
     * 查看友链列表
     *
     * @return 友链列表
     */
    List<FriendLinkDTO> listFriendLinks();

    /**
     * 查看后台友链列表
     *
     * @param condition 条件
     * @return 友链列表
     */
    PageDTO<FriendLinkBackDTO> listFriendLinkDTO(ConditionVO condition);

}

package com.star.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.entity.FriendLink;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.FriendLinkVO;
import com.star.core.dto.FriendLinkBackDTO;
import com.star.core.dto.FriendLinkDTO;
import com.star.core.dto.PageData;

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
    PageData<FriendLinkBackDTO> listFriendLink(ConditionVO condition);

    /**
     * 保存或修改友联
     *
     * @param friendLinkVO
     */
    void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO);

}

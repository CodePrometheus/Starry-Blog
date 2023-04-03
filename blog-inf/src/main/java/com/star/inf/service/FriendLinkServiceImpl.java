package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.BeanCopyUtils;
import com.star.inf.dto.FriendLinkDTO;
import com.star.inf.entity.FriendLink;
import com.star.inf.mapper.LinkMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 友链业务
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:40
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<LinkMapper, FriendLink> implements IService<FriendLink> {

    @Resource
    private LinkMapper linkMapper;

    public List<FriendLinkDTO> listFriendLinks() {
        List<FriendLink> friendLinkList = linkMapper.selectList(null);
        return BeanCopyUtils.copyList(friendLinkList, FriendLinkDTO.class);
    }

}

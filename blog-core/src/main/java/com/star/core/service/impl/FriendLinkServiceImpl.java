package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.domain.entity.FriendLink;
import com.star.core.domain.mapper.LinkMapper;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.service.FriendLinkService;
import com.star.core.service.dto.FriendLinkBackDTO;
import com.star.core.service.dto.FriendLinkDTO;
import com.star.core.service.dto.PageDTO;
import com.star.core.util.BeanCopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 友链业务
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:40
 */
@Service
public class FriendLinkServiceImpl extends ServiceImpl<LinkMapper, FriendLink> implements FriendLinkService {

    @Resource
    private LinkMapper linkMapper;

    @Override
    public List<FriendLinkDTO> listFriendLinks() {
        QueryWrapper<FriendLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "link_name", "link_avatar", "link_address", "link_intro");
        return BeanCopyUtil.copyList(linkMapper.selectList(queryWrapper), FriendLinkDTO.class);
    }

    @Override
    public PageDTO listFriendLinkDTO(ConditionVO condition) {
        Page<FriendLink> page = new Page<>(condition.getCurrent(), condition.getSize());
        QueryWrapper<FriendLink> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "link_name", "link_avatar", "link_address", "link_intro", "create_time")
                .like(condition.getKeywords() != null, "link_name", condition.getKeywords());
        Page<FriendLink> friendLinkPage = linkMapper.selectPage(page, queryWrapper);
        //转换DTO
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtil.copyList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageDTO(friendLinkBackDTOList, (int) friendLinkPage.getTotal());
    }

}

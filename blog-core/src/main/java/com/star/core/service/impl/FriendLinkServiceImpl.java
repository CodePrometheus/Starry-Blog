package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.core.entity.FriendLink;
import com.star.core.mapper.LinkMapper;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.FriendLinkVO;
import com.star.core.service.FriendLinkService;
import com.star.core.dto.FriendLinkBackDTO;
import com.star.core.dto.FriendLinkDTO;
import com.star.core.dto.PageDTO;
import com.star.core.util.BeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

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
        List<FriendLink> friendLinkList = linkMapper.selectList(new LambdaQueryWrapper<FriendLink>()
                .select(FriendLink::getId, FriendLink::getLinkName, FriendLink::getLinkAvatar
                        , FriendLink::getLinkIntro, FriendLink::getLinkAddress));
        return BeanCopyUtil.copyList(friendLinkList, FriendLinkDTO.class);
    }

    @Override
    public PageDTO<FriendLinkBackDTO> listFriendLinkDTO(ConditionVO condition) {
        Page<FriendLink> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<FriendLink> friendLinkPage = linkMapper.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .select(FriendLink::getId, FriendLink::getLinkName, FriendLink::getLinkIntro,
                        FriendLink::getLinkAddress, FriendLink::getLinkAvatar, FriendLink::getCreateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), FriendLink::getLinkName, condition.getKeywords()));
        // 转换DTO
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtil.copyList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageDTO<>(friendLinkBackDTOList, (int) friendLinkPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        Integer count = linkMapper.selectCount(new LambdaQueryWrapper<FriendLink>()
                .eq(FriendLink::getLinkName, friendLinkVO.getLinkName()));
        if (count > 0) {
            throw new StarryException("友链已存在");
        }
        FriendLink friendLink = FriendLink.builder()
                .id(friendLinkVO.getId())
                .createTime(Objects.isNull(friendLinkVO.getId()) ? new Date() : null)
                .linkAddress(friendLinkVO.getLinkAddress())
                .linkAvatar(friendLinkVO.getLinkAvatar())
                .linkIntro(friendLinkVO.getLinkIntro())
                .linkName(friendLinkVO.getLinkName())
                .build();
        this.saveOrUpdate(friendLink);
    }

}

package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.dto.FriendLinkBackDTO;
import com.star.core.dto.FriendLinkDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.FriendLink;
import com.star.core.mapper.LinkMapper;
import com.star.core.service.FriendLinkService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.FriendLinkVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        List<FriendLink> friendLinkList = linkMapper.selectList(null);
        return BeanCopyUtil.copyList(friendLinkList, FriendLinkDTO.class);
    }

    @Override
    public PageData<FriendLinkBackDTO> listFriendLink(ConditionVO condition) {
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<FriendLink> friendLinkPage = linkMapper.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), FriendLink::getLinkName,
                        condition.getKeywords()));
        // 转换DTO
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtil.copyList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageData<>(friendLinkBackDTOList, friendLinkPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        FriendLink friendLink = BeanCopyUtil.copyObject(friendLinkVO, FriendLink.class);
        this.saveOrUpdate(friendLink);
    }

}

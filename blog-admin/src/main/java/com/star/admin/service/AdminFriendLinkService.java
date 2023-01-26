package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.BeanCopyUtil;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.FriendLinkBackDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.FriendLink;
import com.star.inf.mapper.LinkMapper;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.FriendLinkVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminFriendLinkService extends ServiceImpl<LinkMapper, FriendLink> {

    @Resource
    private LinkMapper linkMapper;

    /**
     * 查看后台友链列表
     *
     * @param condition 条件
     * @return 友链列表
     */
    public PageData<FriendLinkBackDTO> listFriendLink(ConditionVO condition) {
        Page<FriendLink> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<FriendLink> friendLinkPage = linkMapper.selectPage(page, new LambdaQueryWrapper<FriendLink>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), FriendLink::getLinkName,
                        condition.getKeywords()));
        // 转换DTO
        List<FriendLinkBackDTO> friendLinkBackDTOList = BeanCopyUtil.copyList(friendLinkPage.getRecords(), FriendLinkBackDTO.class);
        return new PageData<>(friendLinkBackDTOList, friendLinkPage.getTotal());
    }

    /**
     * 保存或修改友联
     *
     * @param friendLinkVO
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdateFriendLink(FriendLinkVO friendLinkVO) {
        FriendLink friendLink = BeanCopyUtil.copyObject(friendLinkVO, FriendLink.class);
        this.saveOrUpdate(friendLink);
    }

}

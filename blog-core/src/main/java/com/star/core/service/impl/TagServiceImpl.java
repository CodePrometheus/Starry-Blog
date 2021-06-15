package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.core.domain.entity.ArticleTag;
import com.star.core.domain.entity.Tag;
import com.star.core.domain.mapper.ArticleTagMapper;
import com.star.core.domain.mapper.TagMapper;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.TagVO;
import com.star.core.service.TagService;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.TagDTO;
import com.star.core.util.BeanCopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @Author: zzStar
 * @Date: 12-20-2020 11:03
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Override
    public PageDTO<TagDTO> listTags() {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>().select(Tag::getId, Tag::getTagName));
        // 查询标签列表
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagList, TagDTO.class);
        // 查询标签数量
        Integer count = tagMapper.selectCount(null);
        return new PageDTO<>(tagDTOList, count);
    }

    @Override
    public PageDTO<Tag> listTagBackDTO(ConditionVO condition) {
        Page<Tag> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Tag> tagPage = tagMapper.selectPage(page, new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getTagName, Tag::getCreateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()), Tag::getTagName, condition.getKeywords())
                .orderByDesc(Tag::getCreateTime));
        return new PageDTO<>(tagPage.getRecords(), (int) tagPage.getTotal());
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 查询标签下是否有文章
        Integer count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        if (count > 0) {
            throw new StarryException("删除失败，该标签下存在文章");
        }
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveOrUpdateTag(TagVO tagVO) {
        Integer count = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .eq(Tag::getTagName, tagVO.getTagName()));
        if (count > 0) {
            throw new StarryException("标签名已存在");
        }
        Tag tag = Tag.builder()
                .id(tagVO.getId())
                .tagName(tagVO.getTagName())
                .createTime(Objects.isNull(tagVO.getId()) ? new Date() : null).build();
        this.saveOrUpdate(tag);
    }

}

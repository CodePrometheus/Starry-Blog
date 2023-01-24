package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.core.dto.PageData;
import com.star.core.dto.TagBackDTO;
import com.star.core.dto.TagDTO;
import com.star.core.entity.ArticleTag;
import com.star.core.entity.Tag;
import com.star.core.mapper.ArticleTagMapper;
import com.star.core.mapper.TagMapper;
import com.star.core.service.TagService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.TagVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public PageData<TagDTO> listTags() {
        List<Tag> tagList = tagMapper.selectList(null);
        List<TagDTO> tags = BeanCopyUtil.copyList(tagList, TagDTO.class);
        // 查询标签数量
        Long count = tagMapper.selectCount(null);
        return new PageData<>(tags, count);
    }

    @Override
    public PageData<TagBackDTO> listTagBack(ConditionVO condition) {
        Long tagCount = tagMapper.selectCount(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(condition.getKeywords()),
                        Tag::getTagName, condition.getKeywords()));
        if (tagCount == 0) {
            return new PageData<>();
        }
        List<TagBackDTO> tagList = tagMapper.listTagBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageData<>(tagList, tagCount);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void deleteTag(List<Integer> tagIdList) {
        // 查询标签下是否有文章
        Long count = articleTagMapper.selectCount(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getTagId, tagIdList));
        if (count > 0) {
            throw new StarryException("删除失败，该标签下存在文章");
        }
        tagMapper.deleteBatchIds(tagIdList);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveOrUpdateTag(TagVO tagVO) {
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId).eq(Tag::getTagName, tagVO.getTagName()));
        if (Objects.nonNull(existTag)
                && !existTag.getId().equals(tagVO.getId())) {
            throw new StarryException("标签名已存在");
        }
        Tag tag = BeanCopyUtil.copyObject(tagVO, Tag.class);
        this.saveOrUpdate(tag);
    }

    @Override
    public List<TagDTO> listTagsBySearch(ConditionVO condition) {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Tag::getTagName, condition.getKeywords())
                .orderByDesc(Tag::getUpdateTime));
        return BeanCopyUtil.copyList(tagList, TagDTO.class);
    }

}

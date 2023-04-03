package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.PageData;
import com.star.inf.dto.TagBackDTO;
import com.star.inf.dto.TagDTO;
import com.star.inf.entity.ArticleTag;
import com.star.inf.entity.Tag;
import com.star.inf.mapper.ArticleTagMapper;
import com.star.inf.mapper.TagMapper;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.TagVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminTagService extends ServiceImpl<TagMapper, Tag> {

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    /**
     * 查询后台标签
     *
     * @param condition 条件
     * @return 标签列表
     */
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

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
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

    /**
     * 保存或更新标签
     *
     * @param tagVO
     */
    @Transactional(rollbackFor = StarryException.class)
    public void saveOrUpdateTag(TagVO tagVO) {
        Tag existTag = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId).eq(Tag::getTagName, tagVO.getTagName()));
        if (Objects.nonNull(existTag)
                && !existTag.getId().equals(tagVO.getId())) {
            throw new StarryException("标签名已存在");
        }
        Tag tag = BeanCopyUtils.copyObject(tagVO, Tag.class);
        this.saveOrUpdate(tag);
    }

    /**
     * 搜索文章标签
     *
     * @param condition 条件
     * @return {@link List<TagDTO>} 标签列表
     */
    public List<TagDTO> listTagsBySearch(ConditionVO condition) {
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Tag::getTagName, condition.getKeywords())
                .orderByDesc(Tag::getUpdateTime));
        return BeanCopyUtils.copyList(tagList, TagDTO.class);
    }

}

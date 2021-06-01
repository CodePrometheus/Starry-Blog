package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.domain.entity.ArticleTag;
import com.star.core.domain.entity.Tag;
import com.star.core.domain.mapper.ArticleTagMapper;
import com.star.core.domain.mapper.TagMapper;
import com.star.core.domain.vo.ConditionVO;
import com.star.common.exception.StarryException;
import com.star.core.service.TagService;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.TagDTO;
import com.star.core.util.BeanCopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 12-20-2020 11:03
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Override
    public PageDTO<TagDTO> listTags() {
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tag_name");
        // 查询标签列表
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagMapper.selectList(queryWrapper), TagDTO.class);
        // 查询标签数量
        Integer count = tagMapper.selectCount(null);
        return new PageDTO(tagDTOList, count);
    }

    @Override
    public PageDTO<Tag> listTagBackDTO(ConditionVO condition) {
        Page<Tag> page = new Page<>(condition.getCurrent(), condition.getSize());
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "tag_name", "create_time")
                .like(condition.getKeywords() != null, "tag_name", condition.getKeywords());
        Page<Tag> tagPage = tagMapper.selectPage(page, queryWrapper);
        return new PageDTO(tagPage.getRecords(), (int) tagPage.getTotal());
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void deleteTag(List<Integer> tagIdList) {
        // 查询标签下是否有文章
        QueryWrapper<ArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").in("tag_id", tagIdList);
        if (!articleTagMapper.selectList(queryWrapper).isEmpty()) {
            throw new StarryException("删除失败，该标签下存在文章");
        }
        tagMapper.deleteBatchIds(tagIdList);
    }

}

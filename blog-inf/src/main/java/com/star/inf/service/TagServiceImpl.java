package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.BeanCopyUtil;
import com.star.inf.dto.PageData;
import com.star.inf.dto.TagDTO;
import com.star.inf.entity.Tag;
import com.star.inf.mapper.TagMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 12-20-2020 11:03
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements IService<Tag> {

    @Resource
    private TagMapper tagMapper;

    /**
     * @return
     */
    public PageData<TagDTO> listTags() {
        List<Tag> tagList = tagMapper.selectList(null);
        List<TagDTO> tags = BeanCopyUtil.copyList(tagList, TagDTO.class);
        // 查询标签数量
        Long count = tagMapper.selectCount(null);
        return new PageData<>(tags, count);
    }

}

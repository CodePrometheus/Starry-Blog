package com.star.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.Tag;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.TagDTO;

import java.util.List;

/**
 * @author zzStar
 */
public interface TagService extends IService<Tag> {

    /**
     * 查询标签列表
     *
     * @return 标签列表
     */
    PageDTO<TagDTO> listTags();

    /**
     * 查询后台标签
     *
     * @param condition 条件
     * @return 标签列表
     */
    PageDTO<Tag> listTagBackDTO(ConditionVO condition);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    void deleteTag(List<Integer> tagIdList);

}
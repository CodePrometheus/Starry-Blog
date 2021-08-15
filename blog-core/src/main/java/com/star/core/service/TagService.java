package com.star.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.entity.Tag;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.TagVO;
import com.star.core.dto.PageData;
import com.star.core.dto.TagDTO;

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
    PageData<TagDTO> listTags();

    /**
     * 查询后台标签
     *
     * @param condition 条件
     * @return 标签列表
     */
    PageData<Tag> listTagBackDTO(ConditionVO condition);

    /**
     * 删除标签
     *
     * @param tagIdList 标签id集合
     */
    void deleteTag(List<Integer> tagIdList);

    /**
     * 保存或更新标签
     *
     * @param tagVO
     */
    void saveOrUpdateTag(TagVO tagVO);

}

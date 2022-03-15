package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.MomentDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.Moment;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.vo.MomentVO;
import com.star.core.vo.TopVO;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:15 PM
 */
public interface MomentService extends IService<Moment> {

    /**
     * 前台根据id查看动态
     *
     * @param momentId 动态id
     * @return 动态
     */
    MomentDTO getMomentById(Integer momentId);

    /**
     * 前台获取动态
     *
     * @return 动态
     */
    PageData<MomentDTO> listMoments();

    /**
     * 点赞动态
     *
     * @param momentId 动态id
     */
    void saveMomentLike(Integer momentId);

    /**
     * 添加或修改动态
     *
     * @param moment 动态对象
     */
    void saveOrUpdateMoment(MomentVO moment);

    /**
     * 修改文章置顶
     *
     * @param topVO
     */
    void updateMomentTop(TopVO topVO);

    /**
     * 删除或恢复动态
     *
     * @param deleteVO 逻辑删除对象
     */
    void updateMomentDelete(DeleteVO deleteVO);

    /**
     * 物理删除动态
     *
     * @param momentIdList 动态id集合
     */
    void deleteMoment(List<Integer> momentIdList);

    /**
     * 后台获取动态
     *
     * @param conditionVO
     * @return
     */
    PageData<MomentDTO> listMomentBack(ConditionVO conditionVO);

    /**
     * 后台根据id查看动态
     *
     * @param momentId
     * @return MomentDTO
     */
    MomentDTO getBackMomentById(Integer momentId);


    /**
     * 获取首页动态列表
     *
     * @return {@link List<String>} 动态列表
     */
    List<String> listHomeMoments();

}

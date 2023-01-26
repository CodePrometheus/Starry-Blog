package com.star.admin.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.BeanCopyUtil;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.MomentDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.Moment;
import com.star.inf.mapper.MomentMapper;
import com.star.inf.utils.UserUtils;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.DeleteVO;
import com.star.inf.vo.MomentVO;
import com.star.inf.vo.TopVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminMomentService extends ServiceImpl<MomentMapper, Moment> {

    @Resource
    private MomentMapper momentMapper;

    /**
     * 后台获取动态
     *
     * @param condition
     * @return
     */
    public PageData<MomentDTO> listMomentBack(ConditionVO condition) {
        Long momentCount = momentMapper.selectCount(new LambdaQueryWrapper<Moment>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Moment::getMomentContent,
                        condition.getKeywords()));
        if (momentCount == 0) {
            return new PageData<>();
        }
        List<MomentDTO> momentList = momentMapper.listMomentBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        momentList.forEach(v -> {
            if (Objects.nonNull(v.getImages())) {
                v.setImgList(JSON.parseObject(v.getImages(), List.class));
            }
        });
        return new PageData<>(momentList, momentCount);
    }

    /**
     * 后台根据id查看动态
     *
     * @param momentId
     * @return MomentDTO
     */
    public MomentDTO getBackMomentById(Integer momentId) {
        MomentDTO momentBack = momentMapper.getBackMomentById(momentId);
        if (Objects.nonNull(momentBack.getImages())) {
            momentBack.setImgList(JSON.parseObject(momentBack.getImages(), List.class));
        }
        return momentBack;
    }

    /**
     * 添加或修改动态
     *
     * @param momentVO 动态对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMoment(MomentVO momentVO) {
        Moment moment = BeanCopyUtil.copyObject(momentVO, Moment.class);
        moment.setUserId(UserUtils.getUserInfoId());
        this.saveOrUpdate(moment);
    }

    /**
     * 修改动态置顶
     *
     * @param topVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMomentTop(TopVO topVO) {
        Moment build = Moment.builder().id(topVO.getId()).isTop(topVO.getIsTop()).build();
        momentMapper.updateById(build);
    }

    /**
     * 删除或恢复动态
     *
     * @param deleteVO 逻辑删除对象
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMomentDelete(DeleteVO deleteVO) {
        List<Moment> momentList = deleteVO.getIdList().stream().map(id ->
                Moment.builder().id(id)
                        .isTop(FALSE)
                        .isDelete(deleteVO.getIsDelete()).build()
        ).collect(Collectors.toList());
        this.updateBatchById(momentList);
    }


    /**
     * 物理删除动态
     *
     * @param momentIdList 动态id集合
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMoment(List<Integer> momentIdList) {
        momentMapper.deleteBatchIds(momentIdList);
    }

}

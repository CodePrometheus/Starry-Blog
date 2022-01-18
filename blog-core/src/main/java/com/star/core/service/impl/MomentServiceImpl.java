package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.RedisUtil;
import com.star.core.dto.MomentDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.Moment;
import com.star.core.mapper.MomentMapper;
import com.star.core.service.MomentService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.util.UserUtil;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.vo.MomentVO;
import com.star.core.vo.TopVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.RedisConst.MOMENT_LIKE_COUNT;
import static com.star.common.constant.RedisConst.MOMENT_USER_LIKE;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:27 PM
 */
@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {

    @Resource
    private MomentService momentService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private MomentMapper momentMapper;

    @Override
    public PageData<MomentDTO> listMomentBack(ConditionVO condition) {
        Long momentCount = momentMapper.selectCount(new LambdaQueryWrapper<Moment>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Moment::getMomentContent,
                        condition.getKeywords()));
        if (momentCount == 0) {
            return new PageData<>();
        }
        List<MomentDTO> momentList = momentMapper.listMomentBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageData<>(momentList, momentCount);
    }

    @Override
    public MomentDTO getMomentById(Integer momentId) {
        Moment moment = momentMapper.selectById(momentId);
        return BeanCopyUtil.copyObject(moment, MomentDTO.class);
    }

    @Override
    public PageData<MomentDTO> listMoments() {
        Page<Moment> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<Moment> momentPage = momentMapper.selectPage(page, new LambdaQueryWrapper<Moment>()
                .select(Moment::getId, Moment::getMomentContent, Moment::getCreateTime
                        , Moment::getIsTop, Moment::getLike)
                .orderByDesc(Moment::getCreateTime)
                .eq(Moment::getIsDelete, FALSE));
        List<MomentDTO> momentList = BeanCopyUtil.copyList(momentPage.getRecords(), MomentDTO.class);
        return new PageData<>(momentList, momentPage.getTotal());
    }

    @Override
    public void saveMomentLike(Integer momentId) {
        String likeKey = MOMENT_USER_LIKE + UserUtil.getUserInfoId();
        if (redisUtil.sIsMember(likeKey, momentId)) {
            redisUtil.sRemove(likeKey, momentId);
            redisUtil.hDecr(MOMENT_LIKE_COUNT, momentId.toString(), 1L);
        } else {
            redisUtil.sAdd(likeKey, momentId);
            redisUtil.hIncr(MOMENT_LIKE_COUNT, momentId.toString(), 1L);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMoment(MomentVO momentVO) {
        Moment moment = BeanCopyUtil.copyObject(momentVO, Moment.class);
        momentService.saveOrUpdate(moment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMomentTop(TopVO topVO) {
        Moment build = Moment.builder().id(topVO.getId()).isTop(topVO.getIsTop()).build();
        momentMapper.updateById(build);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMomentDelete(DeleteVO deleteVO) {
        List<Moment> momentList = deleteVO.getIdList().stream().map(id ->
                Moment.builder().id(id)
                        .isTop(FALSE)
                        .isDelete(deleteVO.getIsDelete()).build()
        ).collect(Collectors.toList());
        momentService.updateBatchById(momentList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMoment(List<Integer> momentIdList) {
        momentMapper.deleteBatchIds(momentIdList);
    }

}

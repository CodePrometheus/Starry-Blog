package com.star.core.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtils;
import com.star.core.dto.CommentCountDTO;
import com.star.core.dto.MomentDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.Comment;
import com.star.core.entity.Moment;
import com.star.core.mapper.CommentMapper;
import com.star.core.mapper.MomentMapper;
import com.star.core.service.MomentService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.HTMLUtil;
import com.star.core.util.PageUtils;
import com.star.core.util.UserUtil;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.vo.MomentVO;
import com.star.core.vo.TopVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.CommonConst.TRUE;
import static com.star.common.constant.RedisConst.MOMENT_LIKE_COUNT;
import static com.star.common.constant.RedisConst.MOMENT_USER_LIKE;
import static com.star.common.enums.StatusEnum.PUBLIC;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:27 PM
 */
@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements MomentService {

    @Resource
    private CommentMapper commentMapper;
    @Resource
    private RedisUtils redisUtils;
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
        momentList.forEach(v -> {
            if (Objects.nonNull(v.getImages())) {
                v.setImgList(JSON.parseObject(v.getImages(), List.class));
            }
        });
        return new PageData<>(momentList, momentCount);
    }

    @Override
    public MomentDTO getBackMomentById(Integer momentId) {
        MomentDTO momentBack = momentMapper.getBackMomentById(momentId);
        if (Objects.nonNull(momentBack.getImages())) {
            momentBack.setImgList(JSON.parseObject(momentBack.getImages(), List.class));
        }
        return momentBack;
    }

    @Override
    public List<String> listHomeMoments() {
        return momentMapper.selectList(new LambdaQueryWrapper<Moment>()
                        .eq(Moment::getStatus, PUBLIC.getStatus())
                        .orderByAsc(Moment::getIsTop)
                        .orderByAsc(Moment::getId)
                        .last("limit 10"))
                .stream()
                .map(v -> v.getMomentContent().length() > 200 ?
                        HTMLUtil.deleteCommentTag(v.getMomentContent().substring(0, 200)) :
                        HTMLUtil.deleteCommentTag(v.getMomentContent()))
                .collect(Collectors.toList());
    }

    @Override
    public MomentDTO getMomentById(Integer momentId) {
        MomentDTO momentDTO = momentMapper.getMomentById(momentId);
        if (Objects.isNull(momentDTO)) {
            throw new StarryException("动态不存在");
        }
        momentDTO.setLikeCount((Integer) redisUtils.hGet(MOMENT_LIKE_COUNT, momentId.toString()));
        if (Objects.nonNull(momentDTO.getImages())) {
            momentDTO.setImgList(JSON.parseObject(momentDTO.getImages(), List.class));
        }
        return momentDTO;
    }

    @Override
    public PageData<MomentDTO> listMoments() {
        Long count = momentMapper.selectCount(new LambdaQueryWrapper<Moment>().eq(Moment::getStatus, PUBLIC.getStatus()));
        if (count == 0) {
            return new PageData<>();
        }
        List<MomentDTO> momentDTOS = momentMapper.listHomeMoments(PageUtils.getLimitCurrent(), PageUtils.getSize());

        // moment's comment
        List<Integer> momentIdList = momentDTOS.stream().map(MomentDTO::getId).collect(Collectors.toList());
        Map<Long, Long> commentCountMap = commentMapper.listMomentCountByMomentIds(momentIdList)
                .stream().collect(Collectors.toMap(CommentCountDTO::getId, CommentCountDTO::getCommentCount));
        if (CollectionUtils.isEmpty(commentCountMap.keySet())) {
            return new PageData<>(momentDTOS, count);
        }

        Map<String, Object> likeCountMap = redisUtils.hGetAll(MOMENT_LIKE_COUNT);

        List<Comment> commentList = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>().eq(Comment::getIsReview, TRUE)
                        .isNull(Comment::getArticleId)
        );

        momentDTOS.forEach(v -> {
            v.setLikeCount((Integer) likeCountMap.get(v.getId().toString()));
            v.setCommentCount(commentCountMap.get(v.getId()));
            if (Objects.nonNull(v.getImages())) {
                v.setImgList(JSON.parseObject(v.getImages(), List.class));
            }
        });
        return new PageData<>(momentDTOS, count);
    }

    private Map<Integer, List<Comment>> listCommentChildren(List<Comment> parentList) {
        return parentList.stream().distinct()
                .filter(v -> Objects.nonNull(v.getParentId()))
                .collect(Collectors.groupingBy(Comment::getParentId));
    }

    @Override
    public void saveMomentLike(Integer momentId) {
        String likeKey = MOMENT_USER_LIKE + UserUtil.getUserInfoId();
        if (redisUtils.sIsMember(likeKey, momentId)) {
            redisUtils.sRemove(likeKey, momentId);
            redisUtils.hDecr(MOMENT_LIKE_COUNT, momentId.toString(), 1L);
        } else {
            redisUtils.sAdd(likeKey, momentId);
            redisUtils.hIncr(MOMENT_LIKE_COUNT, momentId.toString(), 1L);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateMoment(MomentVO momentVO) {
        Moment moment = BeanCopyUtil.copyObject(momentVO, Moment.class);
        moment.setUserId(UserUtil.getUserInfoId());
        this.saveOrUpdate(moment);
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
        this.updateBatchById(momentList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMoment(List<Integer> momentIdList) {
        momentMapper.deleteBatchIds(momentIdList);
    }

}

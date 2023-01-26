package com.star.inf.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.HtmlUtils;
import com.star.common.tool.PageUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.CommentCountDTO;
import com.star.inf.dto.MomentDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.Comment;
import com.star.inf.entity.Moment;
import com.star.inf.mapper.CommentMapper;
import com.star.inf.mapper.MomentMapper;
import com.star.inf.utils.UserUtils;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.TRUE;
import static com.star.common.constant.RedisConst.MOMENT_LIKE_COUNT;
import static com.star.common.constant.RedisConst.MOMENT_USER_LIKE;
import static com.star.common.enums.StatusEnum.PUBLIC;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:27 PM
 */
@Service
public class MomentServiceImpl extends ServiceImpl<MomentMapper, Moment> implements IService<Moment> {

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private MomentMapper momentMapper;

    /**
     * @return
     */
    public List<String> listHomeMoments() {
        return momentMapper.selectList(new LambdaQueryWrapper<Moment>()
                        .eq(Moment::getStatus, PUBLIC.getStatus())
                        .orderByAsc(Moment::getIsTop)
                        .orderByAsc(Moment::getId)
                        .last("limit 10"))
                .stream()
                .map(v -> v.getMomentContent().length() > 200 ?
                        HtmlUtils.deleteCommentTag(v.getMomentContent().substring(0, 200)) :
                        HtmlUtils.deleteCommentTag(v.getMomentContent()))
                .collect(Collectors.toList());
    }

    /**
     * @param momentId
     * @return
     */
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

    /**
     * @return
     */
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

    /**
     * @param momentId
     */
    public void saveMomentLike(Integer momentId) {
        String likeKey = MOMENT_USER_LIKE + UserUtils.getUserInfoId();
        if (redisUtils.sIsMember(likeKey, momentId)) {
            redisUtils.sRemove(likeKey, momentId);
            redisUtils.hDecr(MOMENT_LIKE_COUNT, momentId.toString(), 1L);
        } else {
            redisUtils.sAdd(likeKey, momentId);
            redisUtils.hIncr(MOMENT_LIKE_COUNT, momentId.toString(), 1L);
        }
    }

}

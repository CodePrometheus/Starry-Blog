package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.constant.CommonConst;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtil;
import com.star.core.dto.*;
import com.star.core.entity.Article;
import com.star.core.entity.UserInfo;
import com.star.core.mapper.*;
import com.star.core.service.BlogInfoService;
import com.star.core.service.UniqueViewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.RedisConst.*;
import static com.star.common.enums.ArticleStatusEnum.PUBLIC;

/**
 * 博客信息
 *
 * @Author: zzStar
 * @Date: 12-19-2020 19:38
 */
@Service
@SuppressWarnings("unchecked")
public class BlogInfoServiceImpl implements BlogInfoService {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private UniqueViewService uniqueViewService;

    @Override
    public BlogHomeInfoDTO getBlogInfo() {
        // 查询博主信息
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getAvatar, UserInfo::getNickname, UserInfo::getIntro)
                .eq(UserInfo::getId, CommonConst.BLOGGER_ID));
        // 查询文章数量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        // 查询分类数量
        Integer categoryCount = categoryMapper.selectCount(null);
        // 查询标签数量
        Integer tagCount = tagMapper.selectCount(null);
        // 查询公告
        Object value = redisUtil.get(NOTICE);
        String notice = Objects.nonNull(value) ? value.toString() : PUSH;
        // 查询访问量
        String viewCount = Objects.requireNonNull(redisUtil.get(BLOG_VIEWS_COUNT)).toString();
        // 封装上列数据
        return BlogHomeInfoDTO.builder()
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .notice(notice)
                .viewsCount(viewCount).build();
    }

    @Override
    public BlogBackInfoDTO getBlogBackInfo() {
        // 查询访问量
        Integer viewsCount = (Integer) redisUtil.get(BLOG_VIEWS_COUNT);
        // 查询留言量
        Integer messageCount = messageMapper.selectCount(null);
        // 查询用户量
        Integer userCount = userInfoMapper.selectCount(null);
        // 查询文章量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        // 查询一周用户量
        List<UniqueViewDTO> uniqueViewList = uniqueViewService.listUniqueViews();
        // 查询分类数据
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategoryDTO();
        // 查询redis访问量前五的文章
        Map<String, Integer> articleViewsMap = redisUtil.hGetAll(ARTICLE_VIEWS_COUNT);

        // 将文章进行倒序排序
        List<Integer> articleIdList = Objects.requireNonNull(articleViewsMap).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .map(value -> Integer.valueOf(value.getKey()))
                .collect(Collectors.toList());

        // 提取前五篇文章
        int idx = Math.min(articleIdList.size(), 5);
        articleIdList = articleIdList.subList(0, idx);

        // 文章为空直接返回
        if (articleIdList.isEmpty()) {
            return BlogBackInfoDTO.builder()
                    .viewsCount(viewsCount)
                    .messageCount(messageCount)
                    .userCount(userCount)
                    .articleCount(articleCount)
                    .uniqueViewDTOList(uniqueViewList)
                    .categoryDTOList(categoryDTOList).build();
        }

        // 查询文章标题
        List<Article> articleList = articleMapper.listArticleRank(articleIdList);
        // 封装浏览量和标题
        List<ArticleRankDTO> articleRankDTOList = articleList.stream().map(article -> ArticleRankDTO.builder()
                        .articleTitle(article.getArticleTitle())
                        .viewsCount(articleViewsMap.get(article.getId().toString())).build())
                .collect(Collectors.toList());

        return BlogBackInfoDTO.builder()
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryDTOList(categoryDTOList)
                .uniqueViewDTOList(uniqueViewList)
                .articleRankDTOList(articleRankDTOList).build();
    }

    @Override
    public String getAbout() {
        Object value = redisUtil.get(ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateAbout(String aboutContent) {
        redisUtil.set(ABOUT, aboutContent);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateNotice(String notice) {
        redisUtil.set(NOTICE, notice);
    }

    @Override
    public String getNotice() {
        Object value = redisUtil.get(NOTICE);
        return Objects.nonNull(value) ? value.toString() : PUSH;
    }

}

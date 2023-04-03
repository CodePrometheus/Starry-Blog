package com.star.admin.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.*;
import com.star.inf.entity.Article;
import com.star.inf.entity.Tag;
import com.star.inf.entity.WebsiteConfig;
import com.star.inf.mapper.*;
import com.star.inf.service.UniqueViewServiceImpl;
import com.star.inf.vo.BlogInfoVo;
import com.star.inf.vo.WebsiteConfigVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.RedisConst.*;
import static com.star.common.enums.StatusEnum.PUBLIC;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminBlogInfoService {

    @Resource
    private WebsiteConfigMapper websiteConfigMapper;

    @Resource
    private RedisUtils redisUtils;

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
    private UniqueViewServiceImpl uniqueViewServiceImpl;

    /**
     * 获取后台首页数据
     *
     * @return 博客后台信息
     */
    public BlogBackInfoDTO getBlogBackInfo() {
        // 查询访问量
        Object count = redisUtils.get(BLOG_VIEWS_COUNT);
        Integer viewsCount = Integer.parseInt(Optional.of(count).orElse(0).toString());
        // 查询留言量
        Long messageCount = messageMapper.selectCount(null);
        // 查询用户量
        Long userCount = userInfoMapper.selectCount(null);
        // 查询文章量
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus()));
        // 查询一周用户量
        List<UniqueViewDTO> uniqueViewList = uniqueViewServiceImpl.listUniqueViews();
        // 查询文章统计
        List<ArticleStatisticsDTO> articleStatisticsList = articleMapper.listArticleStatistics();
        // 查询分类数据
        List<CategoryDTO> categoryList = categoryMapper.listCategory();
        // 查询标签数据
        List<Tag> tags = tagMapper.selectList(null);
        List<TagDTO> tagList = BeanCopyUtils.copyList(tags, TagDTO.class);
        // 查询redis访问量前五的文章
        Map<Object, Double> articleMap = redisUtils.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, 0L, 4L);
        BlogBackInfoDTO backInfo = BlogBackInfoDTO.builder()
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryList(categoryList)
                .tagList(tagList)
                .articleStatisticsList(articleStatisticsList)
                .uniqueViewList(uniqueViewList).build();

        if (CollectionUtils.isNotEmpty(articleMap)) {
            List<ArticleRankDTO> articleRankList = articleRankListQuery(articleMap);
            backInfo.setArticleRankList(articleRankList);
        }
        return backInfo;
    }

    private List<ArticleRankDTO> articleRankListQuery(Map<Object, Double> articleMap) {
        // 访问量前五具体数据
        List<Integer> articleIdList = new ArrayList<>();
        articleMap.forEach((k, v) -> articleIdList.add((Integer) k));
        return articleMapper.selectList(new LambdaQueryWrapper<Article>()
                        .select(Article::getId, Article::getArticleTitle)
                        .in(Article::getId, articleIdList))
                .stream().map(v -> ArticleRankDTO.builder()
                        .articleTitle(v.getArticleTitle())
                        .viewsCount(articleMap.get(v.getId()).intValue()).build())
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
    }

    /**
     * 修改关于我内容
     *
     * @param blogInfoVo 博客信息 (关于我内容)
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateAbout(BlogInfoVo blogInfoVo) {
        redisUtils.set(ABOUT, blogInfoVo.getAboutContent());
    }

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        WebsiteConfig config = WebsiteConfig.builder().id(1).config(JSON.toJSONString(websiteConfigVO)).build();
        websiteConfigMapper.updateById(config);
        // 删除缓存
        redisUtils.del(WEBSITE_CONFIG);
    }

}

package com.star.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtil;
import com.star.core.dto.*;
import com.star.core.entity.Article;
import com.star.core.entity.WebsiteConfig;
import com.star.core.mapper.*;
import com.star.core.service.BlogInfoService;
import com.star.core.service.PageService;
import com.star.core.service.UniqueViewService;
import com.star.core.vo.BlogInfoVo;
import com.star.core.vo.PageVO;
import com.star.core.vo.WebsiteConfigVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
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
    private WebsiteConfigMapper websiteConfigMapper;

    @Resource
    private PageService pageService;

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
        // 查询文章数量
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        // 查询分类数量
        Integer categoryCount = categoryMapper.selectCount(null);
        // 查询标签数量
        Integer tagCount = tagMapper.selectCount(null);
        // 查询访问量
        String viewCount = redisUtil.get(BLOG_VIEWS_COUNT).toString();
        // 查询网站配置
        WebsiteConfigVO websiteConfig = getWebsiteConfig();
        // 查询页面图片
        List<PageVO> pageList = pageService.listPages();
        // 封装上列数据
        return BlogHomeInfoDTO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewsCount(viewCount)
                .websiteConfig(websiteConfig)
                .pageList(pageList).build();
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
                .eq(Article::getIsDelete, FALSE));
        // 查询一周用户量
        List<UniqueViewDTO> uniqueViewList = uniqueViewService.listUniqueViews();
        // 查询文章统计
        List<ArticleStatisticsDTO> articleStatisticsList = articleMapper.listArticleStatistics();
        // 查询分类数据
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategory();
        // 查询redis访问量前五的文章
        Map<Object, Double> articleMap = redisUtil.zReverseRangeWithScore(ARTICLE_VIEWS_COUNT, Long.valueOf(0), Long.valueOf(4));

        // 文章为空直接返回
        if (CollectionUtils.isEmpty(articleMap)) {
            return BlogBackInfoDTO.builder()
                    .viewsCount(viewsCount)
                    .messageCount(messageCount)
                    .userCount(userCount)
                    .articleCount(articleCount)
                    .categoryList(categoryDTOList)
                    .articleStatisticsList(articleStatisticsList)
                    .uniqueViewList(uniqueViewList).build();
        }

        // 访问量前五具体数据
        List<Integer> articleIdList = new ArrayList<>();
        articleMap.forEach((k, v) -> articleIdList.add((Integer) k));
        List<ArticleRankDTO> articleRankList = articleMapper.selectList(new LambdaQueryWrapper<Article>().select(Article::getId, Article::getArticleTitle)
                        .in(Article::getId, articleIdList))
                .stream().map(v -> ArticleRankDTO.builder()
                        .articleTitle(v.getArticleTitle())
                        .viewsCount(articleMap.get(v.getId()).intValue()).build())
                .sorted(Comparator.comparingInt(ArticleRankDTO::getViewsCount).reversed())
                .collect(Collectors.toList());
        return BlogBackInfoDTO.builder().viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryList(categoryDTOList)
                .articleStatisticsList(articleStatisticsList)
                .uniqueViewList(uniqueViewList)
                .articleRankList(articleRankList).build();
    }

    @Override
    public String getAbout() {
        Object value = redisUtil.get(ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateAbout(BlogInfoVo blogInfoVo) {
        redisUtil.set(ABOUT, blogInfoVo.getAboutContent());
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // 获取缓存数据
        Object websiteConfig = redisUtil.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // 查数据库
            String config = websiteConfigMapper.selectById(1).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisUtil.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigVO;
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        WebsiteConfig config = WebsiteConfig.builder().id(1).config(JSON.toJSONString(websiteConfigVO)).build();
        websiteConfigMapper.updateById(config);
        // 删除缓存
        redisUtil.del(WEBSITE_CONFIG);
    }

}

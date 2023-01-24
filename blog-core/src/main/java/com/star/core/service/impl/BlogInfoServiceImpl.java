package com.star.core.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtils;
import com.star.common.tool.RedisUtils;
import com.star.common.tool.UserAgentUtil;
import com.star.core.dto.*;
import com.star.core.entity.Article;
import com.star.core.entity.Tag;
import com.star.core.entity.WebsiteConfig;
import com.star.core.mapper.*;
import com.star.core.service.BlogInfoService;
import com.star.core.service.PageService;
import com.star.core.service.UniqueViewService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.vo.BlogInfoVo;
import com.star.core.vo.PageVO;
import com.star.core.vo.WebsiteConfigVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.*;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.*;
import static com.star.common.constant.RedisConst.*;
import static com.star.common.enums.StatusEnum.PUBLIC;

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
    private UserAgentUtil userAgentUtil;
    @Resource
    private HttpServletRequest request;
    @Resource
    private WebsiteConfigMapper websiteConfigMapper;
    @Resource
    private PageService pageService;
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
    private UniqueViewService uniqueViewService;

    @Override
    public BlogHomeInfoDTO getBlogInfo() {
        // 查询文章数量
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));
        // 查询分类数量
        Long categoryCount = categoryMapper.selectCount(null);
        // 查询标签数量
        Long tagCount = tagMapper.selectCount(null);
        // 查询访问量
        Object count = redisUtils.get(BLOG_VIEWS_COUNT);
        String viewCount = Optional.ofNullable(count).orElse(0).toString();
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
        List<UniqueViewDTO> uniqueViewList = uniqueViewService.listUniqueViews();
        // 查询文章统计
        List<ArticleStatisticsDTO> articleStatisticsList = articleMapper.listArticleStatistics();
        // 查询分类数据
        List<CategoryDTO> categoryList = categoryMapper.listCategory();
        // 查询标签数据
        List<Tag> tags = tagMapper.selectList(null);
        List<TagDTO> tagList = BeanCopyUtil.copyList(tags, TagDTO.class);
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

    @Override
    public String getAbout() {
        Object value = redisUtils.get(ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateAbout(BlogInfoVo blogInfoVo) {
        redisUtils.set(ABOUT, blogInfoVo.getAboutContent());
    }

    @Override
    public WebsiteConfigVO getWebsiteConfig() {
        WebsiteConfigVO websiteConfigVO;
        // 获取缓存数据
        Object websiteConfig = redisUtils.get(WEBSITE_CONFIG);
        if (Objects.nonNull(websiteConfig)) {
            websiteConfigVO = JSON.parseObject(websiteConfig.toString(), WebsiteConfigVO.class);
        } else {
            // 查数据库
            String config = websiteConfigMapper.selectById(1).getConfig();
            websiteConfigVO = JSON.parseObject(config, WebsiteConfigVO.class);
            redisUtils.set(WEBSITE_CONFIG, config);
        }
        return websiteConfigVO;
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO) {
        WebsiteConfig config = WebsiteConfig.builder().id(1).config(JSON.toJSONString(websiteConfigVO)).build();
        websiteConfigMapper.updateById(config);
        // 删除缓存
        redisUtils.del(WEBSITE_CONFIG);
    }

    @Override
    public void report() {
        String ipAddr = IpUtils.getIpAddr(request);
        Map<String, String> userAgentMap = userAgentUtil.parseOsAndBrowser(request);
        String os = userAgentMap.get("os");
        String browser = userAgentMap.get("browser");
        // 生成唯一用户标识
        String uuid = ipAddr + browser + os;
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisUtils.sIsMember(UNIQUE_VISITOR, md5)) {
            // 统计游客地域分布
            String ipSource = IpUtils.getIpSource(ipAddr);
            if (StringUtils.isNotBlank(ipSource)) {
                ipSource = ipSource.substring(0, 2)
                        .replaceAll(PROVINCE, "")
                        .replaceAll(CITY, "");
                redisUtils.hIncr(VISITOR_AREA, ipSource, 1L);
            } else {
                redisUtils.hIncr(VISITOR_AREA, UNKNOWN, 1L);
            }
            redisUtils.incr(BLOG_VIEWS_COUNT, 1L);
            redisUtils.sAdd(UNIQUE_VISITOR, md5);
        }
    }

}

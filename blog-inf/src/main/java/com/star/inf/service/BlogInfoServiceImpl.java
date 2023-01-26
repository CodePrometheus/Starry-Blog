package com.star.inf.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.tool.BeanCopyUtil;
import com.star.common.tool.IpUtils;
import com.star.common.tool.RedisUtils;
import com.star.common.tool.UserAgentUtil;
import com.star.inf.dto.BlogHomeInfoDTO;
import com.star.inf.entity.Article;
import com.star.inf.mapper.*;
import com.star.inf.vo.PageVO;
import com.star.inf.vo.WebsiteConfigVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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
public class BlogInfoServiceImpl {

    @Resource
    private UserAgentUtil userAgentUtil;

    @Resource
    private HttpServletRequest request;

    @Resource
    private WebsiteConfigMapper websiteConfigMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private PageMapper pageMapper;

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
        List<PageVO> pageList = listPages();
        // 封装上列数据
        return BlogHomeInfoDTO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewsCount(viewCount)
                .websiteConfig(websiteConfig)
                .pageList(pageList).build();
    }

    @Transactional(rollbackFor = SecurityException.class)
    public List<PageVO> listPages() {
        List<PageVO> pageVOList;
        Object pageList = redisUtils.get(PAGE_COVER);
        if (Objects.nonNull(pageList)) {
            pageVOList = JSON.parseObject(pageList.toString(), List.class);
        } else {
            // 否则查数据库
            pageVOList = BeanCopyUtil.copyList(pageMapper.selectList(null), PageVO.class);
            redisUtils.set(PAGE_COVER, JSON.toJSONString(pageVOList));
        }
        return pageVOList;
    }

    public String getAbout() {
        Object value = redisUtils.get(ABOUT);
        return Objects.nonNull(value) ? value.toString() : "";
    }

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

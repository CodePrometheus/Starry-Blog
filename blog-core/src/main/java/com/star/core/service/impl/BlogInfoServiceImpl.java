package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.star.common.constant.UserConst;
import com.star.common.exception.StarryException;
import com.star.core.domain.entity.Article;
import com.star.core.domain.entity.UserInfo;
import com.star.core.domain.mapper.*;
import com.star.core.service.BlogInfoService;
import com.star.core.service.dto.ArticleRankDTO;
import com.star.core.service.dto.BlogBackInfoDTO;
import com.star.core.service.dto.BlogHomeInfoDTO;
import com.star.core.service.dto.CategoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 博客信息
 *
 * @Author: zzStar
 * @Date: 12-19-2020 19:38
 */
@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UniqueViewMapper uniqueViewMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public BlogHomeInfoDTO getBlogInfo() {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper();
        queryWrapper.select("avatar", "nickname", "intro").eq("id", UserConst.BLOGGER_ID);
        //查询博主信息
        UserInfo userInfo = userInfoMapper.selectOne(queryWrapper);
        //查询文章数量
        Integer articleCount = articleMapper.selectCount(null);
        //查询分类数量
        Integer categoryCount = categoryMapper.selectCount(null);
        //查询标签数量
        Integer tagCount = tagMapper.selectCount(null);
        //查询公告
        Object value = redisTemplate.boundValueOps("notice").get();
        String notice = value != null ? value.toString() : "发布你的第一篇公告吧";
        //查询访问量
        String viewsCount = redisTemplate.boundValueOps("blog_views_count").get().toString();
        return new BlogHomeInfoDTO(userInfo.getNickname(), userInfo.getAvatar(), userInfo.getIntro(), articleCount, categoryCount, tagCount, notice, viewsCount);
    }

    @Override
    public BlogBackInfoDTO getBlogBackInfo() {
        //查询访问量
        Integer viewsCount = (Integer) redisTemplate.boundValueOps("blog_views_count").get();
        //查询留言量
        Integer messageCount = messageMapper.selectCount(null);
        //查询用户量
        Integer userCount = userInfoMapper.selectCount(null);
        //查询文章量
        Integer articleCount = articleMapper.selectCount(null);
        //查询一周用户量
        List<Integer> uniqueViewList = uniqueViewMapper.listUniqueViews();
        //查询分类数据
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategoryDTO();
        //查询redis访问量前五的文章
        Map<String, Integer> articleViewsMap = redisTemplate.boundHashOps("article_views_count").entries();
        List<Map.Entry<String, Integer>> articleViewsList = new ArrayList<>(articleViewsMap.entrySet());
        Collections.sort(articleViewsList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
            }
        });
        //创建文章id集合
        List<Integer> articleIdList = new ArrayList<>();
        //提取前五篇文章id
        int index = articleViewsList.size() > 5 ? 5 : articleViewsList.size();
        for (Map.Entry<String, Integer> entry : articleViewsList.subList(0, index)) {
            articleIdList.add(Integer.valueOf(entry.getKey()));
        }
        //文章为空直接返回
        if (articleIdList.isEmpty() || articleIdList.size() == 0) {
            return new BlogBackInfoDTO(viewsCount, messageCount, userCount, articleCount, categoryDTOList, uniqueViewList);
        }
        //查询文章标题
        List<Article> articleList = articleMapper.listArticleRank(articleIdList);
        //封装浏览量和标题
        List<ArticleRankDTO> articleRankDTOList = new ArrayList<>();
        for (Article article : articleList) {
            articleRankDTOList.add(new ArticleRankDTO(article.getArticleTitle(), articleViewsMap.get(article.getId().toString())));
        }
        return new BlogBackInfoDTO(viewsCount, messageCount, userCount, articleCount, categoryDTOList, uniqueViewList, articleRankDTOList);
    }

    @Override
    public String getAbout() {
        Object value = redisTemplate.boundValueOps("about").get();
        return value != null ? value.toString() : "";
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void updateAbout(String aboutContent) {
        redisTemplate.boundValueOps("about").set(aboutContent);
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void updateNotice(String notice) {
        redisTemplate.boundValueOps("notice").set(notice);
    }

    @Override
    public String getNotice() {
        Object value = redisTemplate.boundValueOps("notice").get();
        return value != null ? value.toString() : "发布你的第一篇公告吧";
    }


}

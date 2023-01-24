package com.star.core.strategy.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.core.dto.ArticleSearchDTO;
import com.star.core.entity.Article;
import com.star.core.mapper.ArticleMapper;
import com.star.core.strategy.SearchStrategy;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.*;
import static com.star.common.enums.StatusEnum.PUBLIC;

/**
 * @Author: zzStar
 * @Date: 08-19-2021 23:20
 */
@Service("MysqlSearchStrategyImpl")
public class MysqlSearchStrategyImpl implements SearchStrategy {

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public List<ArticleSearchDTO> searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        // 搜索文章
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .and(v -> v.like(Article::getArticleTitle, keywords)
                        .or()
                        .like(Article::getArticleContent, keywords)));
        // 处理高亮
        return articleList.stream().map(v -> {
            String articleContent = v.getArticleContent();
            int idx = v.getArticleContent().indexOf(keywords);
            if (idx != -1) {
                // 获取关键词前面的文字
                int preIdx = idx > 25 ? idx - 25 : 0;
                String preText = v.getArticleContent().substring(preIdx, idx);
                // 获取关键词到后面的文字
                int last = idx + keywords.length();
                int postLength = v.getArticleContent().length() - last;
                int postIdx = postLength > 175 ? last + 175 : last + postLength;
                String postText = v.getArticleContent().substring(idx, postIdx);
                articleContent = (preText + postText).replaceAll(keywords, PRE_TAG + keywords + SPAN);
            }
            // 文章标题高亮
            String articleTitle = v.getArticleTitle().replaceAll(keywords, PRE_TAG + keywords + SPAN);
            return ArticleSearchDTO.builder().id(String.valueOf(v.getId()))
                    .articleTitle(articleTitle)
                    .articleContent(articleContent).build();
        }).collect(Collectors.toList());
    }

}

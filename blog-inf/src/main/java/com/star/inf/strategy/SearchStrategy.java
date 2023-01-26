package com.star.inf.strategy;

import com.star.inf.dto.ArticleSearchDTO;

import java.util.List;

/**
 * 搜索策略
 *
 * @Author: zzStar
 * @Date: 08-19-2021 23:02
 */
public interface SearchStrategy {

    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return {@link List<ArticleSearchDTO>} 文章列表
     */
    List<ArticleSearchDTO> searchArticle(String keywords);

}

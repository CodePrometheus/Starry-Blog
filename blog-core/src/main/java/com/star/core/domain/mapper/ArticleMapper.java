package com.star.core.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.domain.entity.Article;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.service.dto.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 文章
 * @Author: zzStar
 * @Date: 12-16-2020 20:03
 */
@Repository
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 查询首页文章
     *
     * @param current 当前页码
     * @return 首页文章集合
     */
    List<ArticleHomeDTO> listArticles(Long current);

    /**
     * 根据条件查询文章
     *
     * @param condition 查询条件
     * @return 文章集合
     */
    List<ArticlePreviewDTO> listArticlesByCondition(@Param("condition") ConditionVO condition);

    /**
     * 根据id查询文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    ArticleDTO getArticleById(Integer articleId);

    /**
     * 查询后台文章总量
     *
     * @param condition 条件
     * @return 文章总量
     */
    Integer countArticlesBack(@Param("condition") ConditionVO condition);

    /**
     * 根据条件后台查询文章
     *
     * @param condition 条件
     * @return 文章集合
     */
    List<ArticleBackDTO> listArticlesBack(@Param("condition") ConditionVO condition);

    /**
     * 查询文章排行
     *
     * @param articleIdList 文章ID
     * @return
     */
    List<Article> listArticleRank(@Param("articleIdList") List<Integer> articleIdList);

    /**
     * 查看文章的推荐文章
     *
     * @param articleId 文章id
     * @return 推荐文章
     */
    List<ArticleRecommendDTO> listArticleRecommends(@Param("articleId") Integer articleId);

}

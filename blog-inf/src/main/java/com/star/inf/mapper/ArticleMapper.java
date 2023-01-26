package com.star.inf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.inf.dto.*;
import com.star.inf.entity.Article;
import com.star.inf.vo.ConditionVO;
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
     * @param size    大小
     * @return 首页文章集合
     */
    List<ArticleHomeDTO> listArticles(@Param("current") Long current, @Param("size") Long size);

    /**
     * 根据条件查询文章
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    List<ArticlePreviewDTO> listArticlesByCondition(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * 根据id查询文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    ArticleDTO getArticleById(@Param("articleId") Integer articleId);

    /**
     * 查询后台文章总量
     *
     * @param condition 条件
     * @return 文章总量
     */
    Long countArticlesBack(@Param("condition") ConditionVO condition);

    /**
     * 查询后台文章
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 文章列表
     */
    List<ArticleBackDTO> listArticlesBack(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

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
     * @return 文章列表
     */
    List<ArticleRecommendDTO> listArticleRecommends(@Param("articleId") Integer articleId);


    /**
     * 文章统计
     *
     * @return {@link List<ArticleStatisticsDTO>} 文章统计结果
     */
    List<ArticleStatisticsDTO> listArticleStatistics();

}

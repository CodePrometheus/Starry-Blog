package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.*;
import com.star.core.entity.Article;
import com.star.core.vo.ArticleVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.vo.TopVO;

import java.util.List;

/**
 * @author zzStar
 */
public interface ArticleService extends IService<Article> {

    /**
     * 查询文章归档
     *
     * @return 文章
     */
    PageData<ArchiveDTO> listArchives();

    /**
     * 查询后台文章
     *
     * @param condition 条件
     * @return 文章列表
     */
    PageData<ArticleBackDTO> listArticleBack(ConditionVO condition);

    /**
     * 查询首页文章
     *
     * @return 文章
     */
    List<ArticleHomeDTO> listArticles();

    /**
     * 根据条件查询文章列表
     *
     * @param condition 条件
     * @return 文章
     */
    ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition);

    /**
     * 搜索文章
     *
     * @param condition 条件
     * @return 文章
     */
    List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition);

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    ArticleVO getArticleBackById(Integer articleId);

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    ArticleDTO getArticleById(Integer articleId);

    /**
     * 点赞文章
     *
     * @param articleId 文章id
     */
    void saveArticleLike(Integer articleId);

    /**
     * 添加或修改文章
     *
     * @param articleVO 文章对象
     */
    void saveOrUpdateArticle(ArticleVO articleVO);

    /**
     * 修改文章置顶
     *
     * @param topVO
     */
    void updateArticleTop(TopVO topVO);

    /**
     * 删除或恢复文章
     *
     * @param deleteVO 逻辑删除对象
     */
    void updateArticleDelete(DeleteVO deleteVO);

    /**
     * 物理删除文章
     *
     * @param articleIdList 文章id集合
     */
    void deleteArticles(List<Integer> articleIdList);

}

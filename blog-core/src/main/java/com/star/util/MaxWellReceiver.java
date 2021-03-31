package com.star.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.star.constant.CommonConst;
import com.star.domain.entity.Article;
import com.star.domain.mapper.ElasticsearchMapper;
import com.star.service.dto.ArticleSearchDTO;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 同步es数据
 *
 * @Author: zzStar
 * @Date: 12-21-2020 21:55
 */
@Component
@RabbitListener(queues = "article")
public class MaxWellReceiver {

    @Autowired
    private ElasticsearchMapper elasticsearchMapper;

    @RabbitHandler
    public void process(byte[] data) {
        // 获取更改信息
        Map map = JSON.parseObject(new String(data), Map.class);
        // 获取文章数据
        Article article = JSONObject.toJavaObject((JSONObject) map.get("data"), Article.class);
        // 过滤markdown标签
        article.setArticleContent(HTMLUtil.deleteArticleTag(article.getArticleContent()));
        // 判断操作类型
        String type = map.get("type").toString();
        switch (type) {
            case "insert":
            case "update":
                // 发布文章后更新es文章
                if (article.getIsDraft().equals(CommonConst.FALSE)) {
                    elasticsearchMapper.save(convertArticleSearchDTO(article));
                }
                break;
            case "delete":
                // 物理删除文章
                if (article.getIsDraft().equals(CommonConst.FALSE)) {
                    elasticsearchMapper.deleteById(article.getId());
                }
            default:
                break;
        }
    }

    /**
     * 转换文章搜索DTO
     *
     * @param article
     * @return
     */
    private ArticleSearchDTO convertArticleSearchDTO(Article article) {
        return ArticleSearchDTO.builder()
                .id(article.getId())
                .articleTitle(article.getArticleTitle())
                .articleContent(article.getArticleContent())
                .isDelete(article.getIsDelete())
                .build();
    }

}

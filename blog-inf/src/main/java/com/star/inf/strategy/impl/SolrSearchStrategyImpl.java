package com.star.inf.strategy.impl;

import com.star.inf.dto.ArticleSearchDTO;
import com.star.inf.strategy.SearchStrategy;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2021/9/23
 * @Description: Solr
 */
// @Service("SolrSearchStrategyImpl")
public class SolrSearchStrategyImpl implements SearchStrategy {

    // private static final Logger log = LoggerFactory.getLogger(SolrSearchStrategyImpl.class);
    //
    // @BlogResource
    // private SolrTemplate solrTemplate;
    //
    @Override
    public List<ArticleSearchDTO> searchArticle(String keywords) {
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        //     HighlightOptions highlightOptions = new HighlightOptions()
        //             .addField("articleTitle").addField("articleContent");
        //     // 高亮前缀
        //     highlightOptions.setSimplePrefix(PRE_TAG);
        //     // 高亮后缀
        //     highlightOptions.setSimplePostfix(SPAN);
        //
        //     HighlightQuery query = new SimpleHighlightQuery();
        //     Criteria criteria = new Criteria("articleTitle").contains(keywords)
        //             .or("articleContent").contains(keywords)
        //             .and("isDelete").is(FALSE).and("status").is(PUBLIC.getStatus());
        //     query.addCriteria(criteria);
        //     query.setHighlightOptions(highlightOptions);
        //
        //     HighlightPage<ArticleSearchDTO> page = solrTemplate.queryForHighlightPage("", query, ArticleSearchDTO.class);
        //     try {
        //         return page.getHighlighted().stream().map(hit -> {
        //             ArticleSearchDTO search = hit.getEntity();
        //             if (hit.getHighlights().size() > 0 && hit.getHighlights().get(0).getSnipplets().size() > 0) {
        //                 // 设置高亮结果
        //                 search.setArticleTitle(hit.getHighlights().get(0).getSnipplets().get(0));
        //                 search.setArticleContent(hit.getHighlights().get(0).getSnipplets().get(0));
        //             }
        //             return search;
        //         }).collect(Collectors.toList());
        //     } catch (Exception e) {
        //         log.error(e.getMessage());
        //     }
        return new ArrayList<>();
    }

}

package com.star.core.search;

/**
 * @Author: zzStar
 * @Date: 2021/9/24
 * @Description:
 */
//@Component
public class SolrSearchUtil {

    // private static final Logger log = LoggerFactory.getLogger(SolrSearchUtil.class);
    //
    // @Value("${spring.solr.core}")
    // private String core;
    //
    // @Resource
    // private ArticleMapper articleMapper;
    // @Resource
    // private SolrClient solrClient;
    //
    // @Async
    // public void async() {
    //     List<Article> articles = articleMapper.selectList(null);
    //     articles.forEach(article -> {
    //         ArticleSearchDTO search = ArticleSearchDTO.builder()
    //                 .id(String.valueOf(article.getId()))
    //                 .articleContent(article.getArticleContent())
    //                 .articleTitle(article.getArticleTitle())
    //                 .isDelete(article.getIsDelete())
    //                 .status(article.getStatus()).build();
    //         try {
    //             solrClient.addBean(search);
    //             solrClient.commit();
    //         } catch (IOException | SolrServerException e) {
    //             log.error(e.getMessage());
    //         }
    //     });
    // }
    //
    // @Async
    // public void insertOrUpdateIdx(ArticleSearchDTO article) {
    //     ArticleSearchDTO search = ArticleSearchDTO.builder()
    //             .id(String.valueOf(article.getId()))
    //             .articleContent(article.getArticleContent())
    //             .articleTitle(article.getArticleTitle())
    //             .isDelete(article.getIsDelete())
    //             .status(article.getStatus()).build();
    //     try {
    //         solrClient.addBean(search);
    //         solrClient.commit();
    //     } catch (IOException | SolrServerException e) {
    //         log.error(e.getMessage());
    //     }
    // }
    //
    // @Async
    // public void deleteIdx(String isList) {
    //     try {
    //         solrClient.deleteById(isList);
    //         solrClient.commit();
    //     } catch (SolrServerException | IOException e) {
    //         log.error(e.getMessage());
    //     }
    // }

}

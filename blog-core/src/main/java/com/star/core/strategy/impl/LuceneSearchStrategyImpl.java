package com.star.core.strategy.impl;

import com.star.core.dto.ArticleSearchDTO;
import com.star.core.strategy.SearchStrategy;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.PRE_TAG;
import static com.star.common.constant.CommonConst.SPAN;

/**
 * @Author: zzStar
 * @Date: 09-24-2021 22:20
 */
@Service("LuceneSearchStrategyImpl")
public class LuceneSearchStrategyImpl implements SearchStrategy {

    private static final Logger log = LoggerFactory.getLogger(LuceneSearchStrategyImpl.class);

    @Value("${spring.lucene.dir}")
    private String indexDir;

    private Directory directory;

    @Override
    public List<ArticleSearchDTO> searchArticle(String keywords) {
        try {
            directory = FSDirectory.open(Paths.get(indexDir));
            // 获取读索引对象
            IndexReader idxReader = DirectoryReader.open(directory);
            // 获取索引查询对象
            IndexSearcher idxSearch = new IndexSearcher(idxReader);
            // 组合查询对象
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            // 中文分析器
            SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();

            // 标题查询分析器
            QueryParser articleTitle = new QueryParser("articleTitle", analyzer);
            // 标题查询器
            Query titleQuery = articleTitle.parse(keywords);

            // 内容查询分析器
            QueryParser articleContent = new QueryParser("articleContent", analyzer);
            // 内容查询器
            Query contentQuery = articleContent.parse(keywords);

            // 添加标题查询器 逻辑关系为或者
            builder.add(titleQuery, BooleanClause.Occur.SHOULD);
            // 添加内容查询器 逻辑关系为或者
            builder.add(contentQuery, BooleanClause.Occur.SHOULD);
            // 查询前10条记录在hit中
            TopDocs hits = idxSearch.search(builder.build(), 10);
            // 将title得分高的放在前面
            QueryScorer queryScorer = new QueryScorer(titleQuery);
            // 得分高的片段
            Fragmenter fragmenter = new SimpleSpanFragmenter(queryScorer);
            // 格式化得分高片段
            SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
                    PRE_TAG, SPAN);
            // 高亮显示
            Highlighter highlighter = new Highlighter(formatter, queryScorer);
            // 将得分高的片段设置进去
            highlighter.setTextFragmenter(fragmenter);
            return Arrays.stream(hits.scoreDocs).map(scoreDoc -> {
                ArticleSearchDTO search = new ArticleSearchDTO();
                try {
                    Document doc = idxSearch.doc(scoreDoc.doc);
                    search.setId(doc.get("id"));
                    search.setStatus(Integer.parseInt(doc.get("status")));
                    search.setIsDelete(Integer.valueOf(doc.get("isDelete")));
                    String title = doc.getField("articleTitle").stringValue();
                    String content = doc.getField("articleContent").stringValue();

                    if (Objects.nonNull(title)) {
                        TokenStream tokenStream = analyzer.tokenStream("articleTitle", new StringReader(title));
                        String highTitle = highlighter.getBestFragment(tokenStream, title);
                        if (StringUtils.isNotEmpty(highTitle)) {
                            search.setArticleTitle(highTitle);
                        } else {
                            search.setArticleTitle(title);
                        }
                    }
                    if (Objects.nonNull(content)) {
                        TokenStream tokenStream = analyzer.tokenStream("articleContent", new StringReader(content));
                        String highContent = highlighter.getBestFragment(tokenStream, content);
                        if (StringUtils.isEmpty(highContent)) {
                            if (content.length() > 200) {
                                search.setArticleContent(content.substring(0, 200));
                            } else {
                                search.setArticleContent(content);
                            }
                        } else {
                            search.setArticleContent(highContent);
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                return search;
            }).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ArrayList<>();
    }

}

package com.star.core.search;

import com.star.core.dto.ArticleSearchDTO;
import com.star.core.entity.Article;
import com.star.core.mapper.ArticleMapper;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 09-28-2021 15:15
 */
@Component
public class LuceneSearchUtil {

    private static final Logger log = LoggerFactory.getLogger(LuceneSearchUtil.class);

    @Value("${spring.lucene.dir}")
    private String indexDir;

    @Resource
    private ArticleMapper articleMapper;
    private Directory directory;


    @Async
    public void async() throws IOException {
        deleteAllIdx();
        List<Article> articles = articleMapper.selectList(null);
        IndexWriter writer = getIndexWriter();
        Document document = new Document();
        try {
            articles.forEach(v -> {
                document.add(new StringField("id", String.valueOf(v.getId()), Field.Store.YES));
                document.add(new TextField("articleTitle", v.getArticleTitle(), Field.Store.YES));
                document.add(new TextField("articleContent", v.getArticleContent(), Field.Store.YES));
                document.add(new StringField("status", String.valueOf(v.getStatus()), Field.Store.YES));
                document.add(new StringField("isDelete", String.valueOf(v.getIsDelete()), Field.Store.YES));
            });
            writer.addDocument(document);
            writer.commit();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            writer.close();
            writer.getDirectory().close();
        }
    }

    /**
     * 删除索引索引
     *
     * @throws IOException
     */
    private void deleteAllIdx() throws IOException {
        IndexWriter writer = getIndexWriter();
        writer.deleteAll();
        writer.commit();
        writer.close();
        log.info("Lucene删除全部索引");
    }

    @Async
    public void deleteIdx(String id) {
        try {
            IndexWriter writer = getIndexWriter();
            writer.deleteDocuments(new Term("id", id));
            writer.flush();
            writer.commit();
            writer.close();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Async
    public void insertOrUpdateIdx(ArticleSearchDTO dto) {
        IndexWriter writer = null;
        try {
            writer = getIndexWriter();
            Document document = new Document();
            document.add(new StringField("id", String.valueOf(dto.getId()), Field.Store.YES));
            document.add(new TextField("articleTitle", dto.getArticleTitle(), Field.Store.YES));
            document.add(new TextField("articleContent", dto.getArticleContent(), Field.Store.YES));
            document.add(new StringField("status", String.valueOf(dto.getStatus()), Field.Store.YES));
            document.add(new StringField("isDelete", String.valueOf(dto.getIsDelete()), Field.Store.YES));
            writer.addDocument(document);
            writer.commit();
        } catch (IOException e) {
            log.error(e.getMessage());
        } finally {
            try {
                writer.close();
                writer.getDirectory().close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
    }

    /**
     * 获取写索引对象
     *
     * @return
     * @throws IOException
     */
    public IndexWriter getIndexWriter() throws IOException {
        // 实例化索引目录
        directory = FSDirectory.open(Paths.get(indexDir));
        // 得到中文解析器
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        // 注册中文解析器到写索引配置
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        // 实例化写索引对象
        return new IndexWriter(directory, config);
    }

}

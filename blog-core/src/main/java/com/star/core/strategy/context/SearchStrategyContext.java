package com.star.core.strategy.context;

import com.star.core.dto.ArticleSearchDTO;
import com.star.core.strategy.SearchStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.star.common.enums.SearchModeEnum.getStrategy;

/**
 * 搜索策略上下文
 *
 * @Author: zzStar
 * @Date: 08-19-2021 23:05
 */
@Service
public class SearchStrategyContext {

    @Value("${search.mode}")
    private String searchMode;

    @Resource
    private Map<String, SearchStrategy> searchStrategyMap;

    /**
     * 执行搜索策略
     *
     * @param keywords 关键字
     * @return {@link List <ArticleSearchDTO>} 搜索文章
     */
    public List<ArticleSearchDTO> executeSearchStrategy(String keywords) {
        return searchStrategyMap.get(getStrategy(searchMode)).searchArticle(keywords);
    }

}

package com.star.admin.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.common.tool.*;
import com.star.inf.dto.PageData;
import com.star.inf.dto.VisitLogDTO;
import com.star.inf.entity.Article;
import com.star.inf.entity.BlogResource;
import com.star.inf.entity.UserInfo;
import com.star.inf.entity.VisitLog;
import com.star.inf.mapper.ArticleMapper;
import com.star.inf.mapper.VisitLogMapper;
import com.star.inf.vo.ConditionVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: zzStar
 * @Date: 01-04-2022 01:41
 */
@Service
public class VisitLogService extends ServiceImpl<VisitLogMapper, VisitLog> {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserAgentUtil userAgentUtil;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * 查询访问日志
     *
     * @param condition 条件
     * @return 访问日志
     */
    public PageData<VisitLogDTO> listVisitLogs(ConditionVO condition) {
        Page<VisitLog> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<VisitLog> visitLog = this.page(page, new LambdaQueryWrapper<VisitLog>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), VisitLog::getRequestMethod, condition.getKeywords())
                .or().like(StringUtils.isNotBlank(condition.getKeywords()), VisitLog::getVisitDesc, condition.getKeywords())
                .or().like(StringUtils.isNotBlank(condition.getKeywords()), VisitLog::getContent, condition.getKeywords())
                .orderByDesc(VisitLog::getCreateTime));
        List<VisitLogDTO> visitLogList = BeanCopyUtils.copyList(visitLog.getRecords(), VisitLogDTO.class);
        return new PageData<>(visitLogList, visitLog.getTotal());
    }

    /**
     * 埋点访问日志
     *
     * @param request
     * @param response
     * @param user
     * @param blogResource
     * @throws Exception
     */
    public void pointVisitLog(HttpServletRequest request, HttpServletResponse response, UserInfo user, BlogResource blogResource) throws Exception {
        VisitLog log = new VisitLog();
        log.setUserId(user.getId());
        log.setNickname(user.getNickname());
        String ip = IpUtils.getIpAddr(request);
        log.setIpAddr(ip);
        log.setIpSource(IpUtils.getIpSource(ip));
        Map<String, String> map = userAgentUtil.parseOsAndBrowser(request);
        log.setOs(map.get("os"));
        log.setBrowser(map.get("browser"));
        log.setContent(blogResource.getResourceName());

        String url = request.getRequestURI();
        String param = objectMapper.writeValueAsString(request.getParameterMap());
        log.setVisitUrl(url);
        log.setRequestParam(param);
        if (url.contains("search")) {
            Map parseMap = JSON.parseObject(param, Map.class);
            log.setVisitDesc("Keywords: " + parseMap.get("keywords"));
        }
        if (url.equals("/comments")) {
            Map json = JSON.parseObject(param, Map.class);
            String res = String.valueOf(json.get("articleId"));
            String articleId = res.replace("[\"", "").replace("\"]", "");
            Article article = articleMapper.selectById(articleId);
            String builder = "ArticleTitle: " + article.getArticleTitle() +
                    "; ArticleContent: " + HtmlUtils.deleteCommentTag(article.getArticleContent().substring(0, 10));
            log.setVisitDesc(builder);
        }

        log.setRequestMethod(Objects.requireNonNull(request).getMethod());
        log.setResponseData(String.valueOf(response.getStatus()));
        log.setCreateTime(LocalDateTime.now());
        this.save(log);
    }

}

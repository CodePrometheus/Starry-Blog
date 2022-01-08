package com.star.core.consumer;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.star.core.config.RabbitConfig;
import com.star.core.dto.ArticleSearchDTO;
import com.star.core.dto.CanalDTO;
import com.star.core.search.LuceneSearchUtil;
import com.star.core.search.SolrSearchUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @Author: zzStar
 * @Date: 12-10-2021 15:18
 */
@Component
@RabbitListener(queues = RabbitConfig.CANAL_SEARCH_QUEUE)
public class CanalSend {

    private static final Logger logger = LoggerFactory.getLogger(CanalSend.class);
    private static final String TABLE_NAME = "tb_article";

    @Resource
    private SolrSearchUtil solrSearchUtil;
    @Resource
    private LuceneSearchUtil luceneSearchUtil;

    @RabbitHandler
    public void doSearchChange(byte[] message, Channel channel, Message msg) throws IOException {
        String res = new String(message, StandardCharsets.UTF_8);
        CanalDTO searchDTO = JSONObject.parseObject(res, CanalDTO.class);
        List<Map<String, String>> data = searchDTO.getData();
        String type = searchDTO.getType();
        String table = searchDTO.getTable();
        if (!table.equals(TABLE_NAME)) {
            channel.basicReject(msg.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        ArticleSearchDTO dto = new ArticleSearchDTO();
        data.forEach(v -> {
            dto.setArticleContent(v.get("article_content"));
            dto.setArticleTitle(v.get("article_title"));
            dto.setId(v.get("id"));
            dto.setStatus(Integer.valueOf(v.get("status")));
            dto.setIsDelete(Integer.valueOf(v.get("is_delete")));
        });

        switch (type) {
            case "INSERT":
            case "UPDATE":
                solrSearchUtil.insertOrUpdateIdx(dto);
                luceneSearchUtil.insertOrUpdateIdx(dto);
                break;
            case "DELETE":
                solrSearchUtil.deleteIdx(dto.getId());
                luceneSearchUtil.deleteIdx(dto.getId());
                break;
            default:
                logger.error("Unexpected value: " + type);
        }
    }

}

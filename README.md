# Starry-Blog

前后端分离的博客项目

### Server

|      技术      |           说明            |  版本  |                         官网                             |
| :------------: | :-----------------------: | :-----------------------: | :----------------------------------------------------------: |
|   SpringBoot   |          MVC框架          | 2.5.5 |  [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| SpringSecurity |      认证和授权框架       | 2.5.5 |      [https://spring.io/projects/spring-security](https://spring.io/projects/spring-security)          |
|  MyBatis-Plus  |          ORM框架          | 3.4.3.4|                   [https://mp.baomidou.com/](https://mp.baomidou.com/)                   |
|   Swagger-UI   |       文档生产工具        | 3.0.0 | [ https://github.com/swagger-api/swagger-ui](https://github.com/swagger-api/swagger-ui) |
|     Kibana     |     分析和可视化平台      | 7.12.0 |              [https://www.elastic.co/cn/kibana](https://www.elastic.co/cn/kibana)               |
| Elasticsearch  |         搜索引擎          | 7.12.0 | [ https://github.com/elastic/elasticsearch](https://github.com/elastic/elasticsearch) |
|     Beats      |     轻量型数据采集器      | 7.12.0 |               [https://www.elastic.co/cn/beats/](https://www.elastic.co/cn/beats/)               |
|    Logstash    | 用于接收Beats的数据并处理 | 7.12.0 |              [https://www.elastic.co/cn/logstash](https://www.elastic.co/cn/logstash)              |
|      Solr      |         搜索引擎          | 8.9.0 |                [https://solr.apache.org/](https://solr.apache.org/)                |
|      Lucene    |       搜索引擎          | 8.9.0 |                [https://lucene.apache.org/](https://lucene.apache.org/)                |
|    RabbitMQ    |         消息队列          | 3.9.7 |   [ https://www.rabbitmq.com/](https://www.rabbitmq.com/)    |
|     Redis      |        分布式缓存         | 6.x |                     [https://redis.io/](https://redis.io/)                       |
|     Druid      |       数据库连接池        | 1.2.7 | [ https://github.com/alibaba/druid](https://github.com/alibaba/druid) |
|     Lombok     |     简化对象封装工具      | 1.8.20 | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      |  HTTP和反向代理web服务器  |  1.20.1 |                    [http://nginx.org/](http://nginx.org/)                       |
|     Hutool     |      Java工具包类库       |  5.7.13 |                [https://hutool.cn/docs/](https://hutool.cn/docs/)                   |
|   Ip2region    |     离线IP地址定位库      | 1.7.2 |         [https://github.com/lionsoul2014/ip2region](https://github.com/lionsoul2014/ip2region)           |

### Front

|         技术          |                  说明                   |                             官网                             |
| :-------------------: | :-------------------------------------: | :----------------------------------------------------------: |
|        Vue.js         |                前端框架                 |                      [https://vuejs.org/](https://vuejs.org/)                      |
|      Vue-router       |                路由框架                 |                  [https://router.vuejs.org/](https://router.vuejs.org/)                   |
|         Vuex          |            全局状态管理框架             |                   [https://vuex.vuejs.org/](https://vuex.vuejs.org/)                    |
|        Element        |               后台ui框架                |    [ https://element.eleme.io](https://element.eleme.io/)    |
|        vuetify        |               前台ui框架                |    [ https://vuetifyjs.com/](https://vuetifyjs.com/)    |
|         Axios         |              前端HTTP框架               | [ https://github.com/axios/axios](https://github.com/axios/axios) |
|        Echarts        |                图表框架                 |                      [www.echartsjs.com](www.echartsjs.com)                       |
|     Highlight.js      |            代码语法高亮插件             |         [https://github.com/highlightjs/highlight.js](https://github.com/highlightjs/highlight.js)          |
|     clipboard.js      |            现代化的拷贝文字             |                  [http://www.clipboardjs.cn/](http://www.clipboardjs.cn/)                  |
|     animate.css      |            炫酷的CSS动画库            |         [https://animate.style/](https://animate.style/)          |
|     live2d      |            看板娘            |         [https://www.live2d.com/](https://www.live2d.com/)          |

### Builder

- 2021/1/1 v1.0 项目初始

- 2021/3/28 v1.5 项目正式发布

- 2021/8/14 v3.0 前后台重构

- 2021/09/30 v4.0 搜索服务

### Feature

- Redis存储点赞、浏览量，score天然排序，epoll底层之快

- RabbitMQ异步更新Elasticsearch、发生评论通知邮件，死信队列自动审核机制

- 策略模式搜索服务自由切换，Elasticsearch、Solr、Lucene、MySQL

- 自定义注解，日志落库、流量控制



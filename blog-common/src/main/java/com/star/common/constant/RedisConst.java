package com.star.common.constant;

/**
 * redis常量
 *
 * @Author: zzStar
 * @Date: 03-30-2021 16:41
 */
public class RedisConst {

    /**
     * 验证码过期时间
     */
    public static final long CODE_EXPIRE_TIME = 5 * 60 * 1000;

    /**
     * 验证码
     */
    public static final String CODE_KEY = "code_";

    /**
     * 博客浏览量
     * string
     * key: blog_views_count | value: views_count
     */
    public static final String BLOG_VIEWS_COUNT = "blog_views_count";

    /**
     * 文章浏览量
     * zset
     * key: article_views_count | score: views_count | member: article_id
     */
    public static final String ARTICLE_VIEWS_COUNT = "article_views_count";

    /**
     * 文章点赞量
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * 评论点赞量
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * 动态点赞量
     */
    public static final String MOMENT_LIKE_COUNT = "moment_like_count";

    /**
     * 用户点赞评论
     * zset
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like";

    /**
     * 用户点赞动态
     * zset
     */
    public static final String MOMENT_USER_LIKE = "moment_user_like";

    /**
     * 用户点赞文章
     * zset
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like";

    /**
     * 关于我信息
     */
    public static final String ABOUT = "about";

    /**
     * ip集合
     * string
     * key: ip_set | value: list(ip)
     */
    public static final String IP_SET = "ip_set";

    /**
     * ip
     */
    public static final String IP = "ip";

    /**
     * 网站配置
     * string
     * key: website_config | value: json
     */
    public static final String WEBSITE_CONFIG = "website_config";

    /**
     * 页面封面
     * string
     * key: page_cover | value: json
     */
    public static final String PAGE_COVER = "page_cover";

    /**
     * 访客
     * zet
     * key: unique_visitor | value: md5
     */
    public static final String UNIQUE_VISITOR = "unique_visitor";

    /**
     * 用户地区
     */
    public static final String USER_AREA = "user_area";

    /**
     * 访客地区
     * hash
     * key: visitor_area | hashKey: ipSource | value: count
     */
    public static final String VISITOR_AREA = "visitor_area";

    /**
     * token
     * hash
     * login_user | user_id | user_info_dto | expireTime
     */
    public static final String LOGIN_USER = "login_user";

}

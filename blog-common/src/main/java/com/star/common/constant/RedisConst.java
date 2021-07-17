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
     */
    public static final String BLOG_VIEWS_COUNT = "blog_views_count";

    /**
     * 文章浏览量
     */
    public static final String ARTICLE_VIEWS_COUNT = "article_views_count";

    /**
     * 文章点赞量
     */
    public static final String ARTICLE_LIKE_COUNT = "article_like_count";

    /**
     * 用户点赞文章
     */
    public static final String ARTICLE_USER_LIKE = "article_user_like";

    /**
     * 评论点赞量
     */
    public static final String COMMENT_LIKE_COUNT = "comment_like_count";

    /**
     * 用户点赞评论
     */
    public static final String COMMENT_USER_LIKE = "comment_user_like";

    /**
     * 关于我信息
     */
    public static final String ABOUT = "about";

    /**
     * 公告
     */
    public static final String NOTICE = "notice";

    /**
     * ip集合
     */
    public static final String IP_SET = "ip_set";

    /**
     * ip
     */
    public static final String IP = "ip";

    /**
     * 发布你的第一篇公告吧
     */
    public static final String PUSH = "发布你的第一篇公告吧";


}

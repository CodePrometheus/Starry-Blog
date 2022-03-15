package com.star.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * @Author: zzStar
 * @Date: 2022/2/14 11:51 PM
 */
@Getter
@AllArgsConstructor
public enum CommentTypeEnum {

    /**
     * 文章评论
     */
    ARTICLE(1, "文章评论", "/articles/"),
    /**
     * 友链评论
     */
    LINK(2, "友链评论", "/links/"),
    /**
     * 说说评论
     */
    MOMENT(3, "动态评论", "/moments/");

    /**
     * 状态
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 路径
     */
    private final String path;

    public static String getCommentPath(Integer type) {
        Arrays.stream(CommentTypeEnum.values()).map(v -> {
            if (v.getType().equals(type)) {
                return v.getPath();
            }
            return null;
        });
        return null;
    }

    public static CommentTypeEnum getCommentEnum(Integer type) {
        Arrays.stream(CommentTypeEnum.values()).map(v -> {
            if (v.getType().equals((type))) {
                return v;
            }
            return null;
        });
        return null;
    }
}

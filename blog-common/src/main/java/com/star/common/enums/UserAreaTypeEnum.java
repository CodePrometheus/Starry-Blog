package com.star.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: zzStar
 * @Date: 08-25-2021 20:03
 */
@Getter
@AllArgsConstructor
public enum UserAreaTypeEnum {

    /**
     * 用户
     */
    USER(1, "用户"),

    /**
     * 游客
     */
    VISITOR(2, "游客");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 获取用户区域类型
     *
     * @param type 类型
     * @return {@link UserAreaTypeEnum} 用户区域类型枚举
     */
    public static UserAreaTypeEnum getUserAreaType(Integer type) {
        for (UserAreaTypeEnum value : UserAreaTypeEnum.values()) {
            if (value.getType().equals(type)) {
                return value;
            }
        }
        return null;
    }

}

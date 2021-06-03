package com.star.core.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author: zzStar
 * @Date: 06-03-2021 13:17
 */
@Data
@AllArgsConstructor
public class ArticleMqMessage implements Serializable {

    public static final String CREATE_OR_UPDATE = "create_update";
    public static final String REMOVE = "remove";

    private int articleId;

    private String type;

}

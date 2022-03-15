package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 2022/2/14 11:44 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCountDTO {

    /**
     * id
     */
    private Long id;

    /**
     * 评论数量
     */
    private Long commentCount;

}

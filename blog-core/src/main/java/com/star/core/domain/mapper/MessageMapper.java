package com.star.core.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.domain.entity.Message;
import org.springframework.stereotype.Repository;

/**
 * @Description: 留言
 * @Author: zzStar
 * @Date: 12-19-2020 21:58
 */
@Repository
public interface MessageMapper extends BaseMapper<Message> {
}

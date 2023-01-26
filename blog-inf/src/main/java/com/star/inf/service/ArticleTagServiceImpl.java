package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.inf.entity.ArticleTag;
import com.star.inf.mapper.ArticleTagMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: zzStar
 * @Date: 12-19-2020 15:54
 */
@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements IService<ArticleTag> {
}

package com.star.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.RedisUtil;
import com.star.core.entity.Page;
import com.star.core.mapper.PageMapper;
import com.star.core.service.PageService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.vo.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.star.common.constant.RedisConst.PAGE_COVER;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 20:44
 */
@Service
public class PageServiceImpl extends ServiceImpl<PageMapper, Page> implements PageService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private PageMapper pageMapper;

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdatePage(PageVO pageVO) {
        Page page = BeanCopyUtil.copyObject(pageVO, Page.class);
        this.saveOrUpdate(page);
        // 删除缓存
        redisUtil.del(PAGE_COVER);
    }

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void deletePage(Integer pageId) {
        pageMapper.deleteById(pageId);
        redisUtil.del(PAGE_COVER);
    }

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public List<PageVO> listPages() {
        List<PageVO> pageVOList;
        Object pageList = redisUtil.get(PAGE_COVER);
        if (Objects.nonNull(pageList)) {
            pageVOList = JSON.parseObject(pageList.toString(), List.class);
        } else {
            // 否则查数据库
            pageVOList = BeanCopyUtil.copyList(pageMapper.selectList(null), PageVO.class);
            redisUtil.set(PAGE_COVER, JSON.toJSONString(pageVOList));
        }
        return pageVOList;
    }

}

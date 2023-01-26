package com.star.admin.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.BeanCopyUtil;
import com.star.common.tool.RedisUtils;
import com.star.inf.entity.BlogPage;
import com.star.inf.mapper.PageMapper;
import com.star.inf.vo.PageVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.star.common.constant.RedisConst.PAGE_COVER;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminPageService extends ServiceImpl<PageMapper, BlogPage> {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private PageMapper pageMapper;

    /**
     * 保存或更新页面
     *
     * @param pageVO 页面信息
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdatePage(PageVO pageVO) {
        BlogPage page = BeanCopyUtil.copyObject(pageVO, BlogPage.class);
        this.saveOrUpdate(page);
        // 删除缓存
        redisUtils.del(PAGE_COVER);
    }

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void deletePage(Integer pageId) {
        pageMapper.deleteById(pageId);
        redisUtils.del(PAGE_COVER);
    }

    /**
     * 获取页面列表
     *
     * @return {@link List <PageVO>} 页面列表
     */
    @Transactional(rollbackFor = SecurityException.class)
    public List<PageVO> listPages() {
        List<PageVO> pageVOList;
        Object pageList = redisUtils.get(PAGE_COVER);
        if (Objects.nonNull(pageList)) {
            pageVOList = JSON.parseObject(pageList.toString(), List.class);
        } else {
            // 否则查数据库
            pageVOList = BeanCopyUtil.copyList(pageMapper.selectList(null), PageVO.class);
            redisUtils.set(PAGE_COVER, JSON.toJSONString(pageVOList));
        }
        return pageVOList;
    }

}

package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.entity.Page;
import com.star.core.vo.PageVO;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 20:44
 */
public interface PageService extends IService<Page> {

    /**
     * 保存或更新页面
     *
     * @param pageVO 页面信息
     */
    void saveOrUpdatePage(PageVO pageVO);

    /**
     * 删除页面
     *
     * @param pageId 页面id
     */
    void deletePage(Integer pageId);

    /**
     * 获取页面列表
     *
     * @return {@link List <PageVO>} 页面列表
     */
    List<PageVO> listPages();

}

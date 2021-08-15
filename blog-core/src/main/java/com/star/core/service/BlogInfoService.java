package com.star.core.service;


import com.star.core.dto.BlogBackInfoDTO;
import com.star.core.dto.BlogHomeInfoDTO;
import com.star.core.vo.BlogInfoVo;
import com.star.core.vo.WebsiteConfigVO;

/**
 * @author zzStar
 */
public interface BlogInfoService {

    /**
     * 获取前台首页数据
     *
     * @return 博客首页信息
     */
    BlogHomeInfoDTO getBlogInfo();

    /**
     * 获取后台首页数据
     *
     * @return 博客后台信息
     */
    BlogBackInfoDTO getBlogBackInfo();

    /**
     * 获取关于我内容
     *
     * @return 关于我内容
     */
    String getAbout();

    /**
     * 修改关于我内容
     *
     * @param blogInfoVo 博客信息 (关于我内容)
     */
    void updateAbout(BlogInfoVo blogInfoVo);

    /**
     * 获取网站配置
     *
     * @return {@link WebsiteConfigVO} 网站配置
     */
    WebsiteConfigVO getWebsiteConfig();

    /**
     * 保存或更新网站配置
     *
     * @param websiteConfigVO 网站配置
     */
    void updateWebsiteConfig(WebsiteConfigVO websiteConfigVO);

}

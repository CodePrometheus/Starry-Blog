package com.star.common.tool;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

/**
 * 码云图床的配置
 *
 * @Author: zzStar
 * @Date: 12-21-2020 17:31
 */
public interface GiteeImgBedConfig {

    /**
     * 码云分配的私人令牌Token
     */
    @Value("${ACCESS_TOKEN}")
    String ACCESS_TOKEN = null;

    /**
     * 码云用户名
     */
    @Value("${OWNER}")
    String OWNER = null;

    /**
     * 仓库名称
     */
    @Value("${REPO_NAME}")
    String REPO_NAME = null;

    /**
     * 上传图片的message
     */
    @Value("${CREATE_REPOS_MESSAGE}")
    String CREATE_REPOS_MESSAGE = null;

    /**
     * 文件前缀
     */
    String IMG_FILE_DEST_PATH = DateUtil.format(new Date(), "yyyy-MM-dd") + "/";

    /**
     * 新建文件URL
     * 第一个 %s =>仓库所属空间地址(owner)
     * 第二个 %s => 仓库路径(repo)
     * 第三个 %s => 文件的路径(path)
     */
    @Value("${CREATE_REPOS_URL}")
    String CREATE_REPOS_URL = null;

    /**
     * GitPage请求路径
     */
    String GITPAGE_REQUEST_URL = "https://gitee.com/" + OWNER + "/" + REPO_NAME + "/raw/master/";
}

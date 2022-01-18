package com.star.core.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.star.common.constant.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 图片上传工具类
 *
 * @Author: zzStar
 * @Date: 12-21-2020 17:34
 */
@Service
public class ImageUtil {

    /**
     * 码云分配的私人令牌Token
     */
    @Value("${ACCESS_TOKEN}")
    private String ACCESS_TOKEN;

    /**
     * 码云用户名
     */
    @Value("${OWNER}")
    private String OWNER;

    /**
     * 仓库名称
     */
    @Value("${REPO_NAME}")
    private String REPO_NAME;

    /**
     * 上传图片的message
     */
    @Value("${CREATE_REPOS_MESSAGE}")
    private String CREATE_REPOS_MESSAGE;

    /**
     * 文件前缀
     */
    private String IMG_FILE_DEST_PATH = DateUtil.format(new Date(), "yyyy-MM-dd") + "/";

    /**
     * 新建文件URL
     * 第一个 %s => 仓库所属空间地址(owner)
     * 第二个 %s => 仓库路径(repo)
     * 第三个 %s => 文件的路径(path)
     */
    @Value("${CREATE_REPOS_URL}")
    private String CREATE_REPOS_URL;

    /**
     * GitPage请求路径
     */
    @Value("${GITPAGE_REQUEST_URL}")
    private String GITPAGE_REQUEST_URL;

    private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * 上传图片
     *
     * @param file       图片
     * @param targetAddr 目标路径
     * @return
     */
    public String upload(MultipartFile file, String targetAddr) {
        Result<?> result = new Result<>();
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        String fileName = IdUtil.simpleUUID() + suffix;
        String paramImgFile = null;
        try {
            paramImgFile = Base64.encode(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 存储
        Map<String, Object> imgMap = new HashMap<>();
        imgMap.put("access_token", ACCESS_TOKEN);
        imgMap.put("content", paramImgFile);
        imgMap.put("message", CREATE_REPOS_MESSAGE);

        String targetDir = targetAddr + IMG_FILE_DEST_PATH + fileName;
        String requestUrl = String.format(CREATE_REPOS_URL, OWNER, REPO_NAME, targetDir);
        String resultJson = HttpUtil.post(requestUrl, imgMap);
        JSONObject jsonObject = JSONUtil.parseObj(resultJson);
        String resultImgUrl = GITPAGE_REQUEST_URL + targetDir;
        if (jsonObject.getObj("commit") != null) {
            logger.info("上传成功");
        } else {
            result.setMessage("上传失败");
        }
        return resultImgUrl;
    }
}

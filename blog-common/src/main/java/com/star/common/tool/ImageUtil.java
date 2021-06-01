package com.star.common.tool;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.star.common.constant.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

/**
 * 图片上传工具类
 *
 * @Author: zzStar
 * @Date: 12-21-2020 17:34
 */
public class ImageUtil {

    /**
     * 上传图片
     *
     * @param file       图片
     * @param targetAddr 目标路径
     * @return
     */
    public static String upload(MultipartFile file, String targetAddr) {
        Result result = new Result();
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
        HashMap<String, Object> imgMap = new HashMap<>();
        imgMap.put("access_token", GiteeImgBedConfig.ACCESS_TOKEN);
        imgMap.put("content", paramImgFile);
        imgMap.put("message", GiteeImgBedConfig.CREATE_REPOS_MESSAGE);

        String targetDir = targetAddr + GiteeImgBedConfig.IMG_FILE_DEST_PATH + fileName;
        String requestUrl = String.format(GiteeImgBedConfig.CREATE_REPOS_URL, GiteeImgBedConfig.OWNER, GiteeImgBedConfig.REPO_NAME, targetDir);

        System.out.println(requestUrl);
        String resultJson = HttpUtil.post(requestUrl, imgMap);
        JSONObject jsonObject = JSONUtil.parseObj(resultJson);
        String resultImgUrl = GiteeImgBedConfig.GITPAGE_REQUEST_URL + targetDir;
        if (jsonObject.getObj("commit") != null) {
            System.out.println("上传成功");
        } else {
            result.setMessage("上传失败");
        }
        return resultImgUrl;
    }
}

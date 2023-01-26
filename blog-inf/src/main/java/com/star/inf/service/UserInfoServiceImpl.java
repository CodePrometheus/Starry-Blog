package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.constant.PathConst;
import com.star.common.exception.StarryException;
import com.star.common.tool.ImageUtil;
import com.star.common.tool.RedisUtils;
import com.star.inf.entity.UserInfo;
import com.star.inf.mapper.UserInfoMapper;
import com.star.inf.utils.UserUtils;
import com.star.inf.vo.EmailVO;
import com.star.inf.vo.UserInfoVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.star.common.constant.RedisConst.CODE_KEY;

/**
 * 用户信息业务
 *
 * @Author: zzStar
 * @Date: 12-20-2020 19:46
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IService<UserInfo> {

    @Resource
    private ImageUtil imageUtil;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * @param userInfoVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .webSite(userInfoVO.getWebSite()).build();
        userInfoMapper.updateById(userInfo);
    }

    /**
     * @param file
     * @return
     */
    @Transactional(rollbackFor = StarryException.class)
    public String updateUserAvatar(MultipartFile file) {
        String avatar = imageUtil.upload(file, PathConst.AVATAR);
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .avatar(avatar).build();
        userInfoMapper.updateById(userInfo);
        return avatar;
    }

    /**
     * @param emailVO
     */
    @Transactional(rollbackFor = StarryException.class)
    public void saveUserEmail(EmailVO emailVO) {
        if (!emailVO.getCode().equals(redisUtils.get(CODE_KEY + emailVO.getEmail()).toString())) {
            throw new StarryException("验证码错误");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtils.getLoginUser().getUserInfoId())
                .email(emailVO.getEmail()).build();
        userInfoMapper.updateById(userInfo);
    }

}

package com.star.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.constant.PathConst;
import com.star.core.domain.entity.UserInfo;
import com.star.core.domain.mapper.UserInfoMapper;
import com.star.core.domain.vo.UserInfoVO;
import com.star.core.domain.vo.UserRoleVO;
import com.star.common.exception.StarryException;
import com.star.core.service.UserInfoService;
import com.star.common.tool.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户信息业务
 *
 * @Author: zzStar
 * @Date: 12-20-2020 19:46
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public void updateUserInfo(UserInfoVO userInfoVO) {
        userInfoMapper.updateById(new UserInfo(userInfoVO));
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public String updateUserAvatar(MultipartFile file) {
        String avatar = ImageUtil.upload(file, PathConst.AVATAR);
        userInfoMapper.updateById(new UserInfo(avatar));
        return avatar;
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void updateUserRole(UserRoleVO userRoleVO) {
        userInfoMapper.updateById(new UserInfo(userRoleVO));
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void updateUserSilence(Integer userInfoId, Integer isSilence) {
        userInfoMapper.updateById(new UserInfo(userInfoId, isSilence));
    }

}

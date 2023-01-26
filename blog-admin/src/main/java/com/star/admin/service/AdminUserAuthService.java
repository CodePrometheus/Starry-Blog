package com.star.admin.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.exception.StarryException;
import com.star.common.tool.PageUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.PageData;
import com.star.inf.dto.UserAreaDTO;
import com.star.inf.dto.UserBackDTO;
import com.star.inf.entity.UserAuth;
import com.star.inf.mapper.UserAuthMapper;
import com.star.inf.utils.UserUtils;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.PasswordVO;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.RedisConst.USER_AREA;
import static com.star.common.constant.RedisConst.VISITOR_AREA;
import static com.star.common.enums.UserAreaTypeEnum.getUserAreaType;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminUserAuthService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserAuthMapper userAuthMapper;

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    public PageData<UserBackDTO> listUserBack(ConditionVO condition) {
        // 获取后台用户数量
        Long count = userAuthMapper.countUser(condition);
        if (count == 0) {
            return new PageData<>();
        }
        // 获取后台用户列表
        List<UserBackDTO> userBackList = userAuthMapper.listUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageData<>(userBackList, count);
    }

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码对象
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateAdminPassword(PasswordVO passwordVO) {
        // 查询旧密码是否正确
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getId, UserUtils.getLoginUser().getId()));
        if (Objects.nonNull(userAuth) && BCrypt.checkpw(passwordVO.getOldPassword(), userAuth.getPassword())) {
            UserAuth user = UserAuth.builder()
                    .id(UserUtils.getLoginUser().getId())
                    .password(BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt())).build();
            userAuthMapper.updateById(user);
        } else {
            throw new StarryException("原密码不正确");
        }
    }

    /**
     * 获取用户区域分布
     *
     * @param condition 条件签证官
     * @return {@link List <UserAreaDTO>} 用户区域分布
     */
    public List<UserAreaDTO> listUserAreas(ConditionVO condition) {
        List<UserAreaDTO> userAreaList = new ArrayList<>();
        switch (Objects.requireNonNull(getUserAreaType(condition.getType()))) {
            case USER:
                // 查询注册用户区域分布
                Object userArea = redisUtils.get(USER_AREA);
                if (Objects.nonNull(userArea)) {
                    userAreaList = JSON.parseObject(userArea.toString(), List.class);
                }
                return userAreaList;
            case VISITOR:
                // 查询游客区域分布
                Map<String, Object> visitorArea = redisUtils.hGetAll(VISITOR_AREA);
                if (Objects.nonNull(visitorArea)) {
                    userAreaList = visitorArea.entrySet().stream()
                            .map(v -> UserAreaDTO.builder()
                                    .name(v.getKey())
                                    .value(Long.valueOf(v.getValue().toString())).build())
                            .collect(Collectors.toList());
                }
                return userAreaList;
            default:
                break;
        }
        return userAreaList;
    }

}

package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.entity.Resource;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.ResourceVO;
import com.star.core.dto.LabelOptionDTO;
import com.star.core.dto.ResourceDTO;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:01
 */
public interface ResourceService extends IService<Resource> {

    /**
     * 导入swagger权限
     */
    void importSwagger();

    /**
     * 添加或修改资源
     *
     * @param resourceVO 资源对象
     */
    void saveOrUpdateResource(ResourceVO resourceVO);

    /**
     * 删除资源
     *
     * @param resourceId 资源id列表
     */
    void deleteResource(Integer resourceId);

    /**
     * 查看资源列表
     *
     * @param condition 条件
     * @return 资源列表
     */
    List<ResourceDTO> listResources(ConditionVO condition);

    /**
     * 查看资源选项
     *
     * @return 资源选项
     */
    List<LabelOptionDTO> listResourceOption();

}

package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.Resource;
import com.star.core.domain.vo.ResourceVO;
import com.star.core.service.dto.LabelOptionDTO;
import com.star.core.service.dto.ResourceDTO;

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
     * @param resourceIdList 资源id列表
     */
    void deleteResources(List<Integer> resourceIdList);

    /**
     * 查看资源列表
     *
     * @return 资源列表
     */
    List<ResourceDTO> listResources();

    /**
     * 查看资源选项
     *
     * @return 资源选项
     */
    List<LabelOptionDTO> listResourceOption();

}

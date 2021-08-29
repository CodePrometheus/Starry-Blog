package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.core.dto.LabelOptionDTO;
import com.star.core.dto.ResourceDTO;
import com.star.core.entity.Resource;
import com.star.core.entity.RoleResource;
import com.star.core.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.star.core.mapper.ResourceMapper;
import com.star.core.mapper.RoleResourceMapper;
import com.star.core.service.ResourceService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.ResourceVO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:04
 */
@Service
@RequiredArgsConstructor
@SuppressWarnings("unchecked")
public class ResourceServiceImpl extends ServiceImpl<ResourceMapper, Resource> implements ResourceService {

    private final ResourceMapper resourceMapper;

    private final FilterInvocationSecurityMetadataSourceImpl metadataSource;

    private final RestTemplate restTemplate;

    private final RoleResourceMapper roleResourceMapper;

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void importSwagger() {
        // delete all
        this.remove(null);
        roleResourceMapper.delete(null);
        List<Resource> resourceList = new ArrayList<>();
        Map<String, Object> data = restTemplate.getForObject("http://localhost:8989/v2/api-docs", Map.class);
        // 获取所有的模块
        List<Map<String, String>> tagList = (List<Map<String, String>>) data.get("tags");
        tagList.forEach(item -> {
            Resource resource = Resource.builder()
                    .resourceName(item.get("name"))
                    .isAnonymous(FALSE).build();
            resourceList.add(resource);
        });
        this.saveBatch(resourceList);

        Map<String, Integer> permissionMap = resourceList.stream()
                .collect(Collectors.toMap(Resource::getResourceName, Resource::getId));
        resourceList.clear();

        // 获取所有接口
        Map<String, Map<String, Map<String, Object>>> path = (Map<String, Map<String, Map<String, Object>>>) data.get("paths");
        path.forEach((url, value) -> value.forEach((requestMethod, info) -> {
            String permissionName = info.get("summary").toString();
            List<String> tag = (List<String>) info.get("tags");

            Integer parentId = permissionMap.get(tag.get(0));
            Resource resource = Resource.builder()
                    .requestMethod(requestMethod.toLowerCase())
                    .resourceName(permissionName)
                    .url(url.replaceAll("\\{[^}]*\\}", "*"))
                    .parentId(parentId)
                    .isAnonymous(FALSE).build();
            resourceList.add(resource);
        }));
        this.saveBatch(resourceList);
    }

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdateResource(ResourceVO resourceVO) {
        // 更新资源信息
        Resource resource = BeanCopyUtil.copyObject(resourceVO, Resource.class);
        this.saveOrUpdate(resource);
        // 重新加载角色资源信息
        metadataSource.clearDataSource();
    }

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void deleteResource(Integer resourceId) {
        // 查询是否有角色关联
        Integer count = roleResourceMapper.selectCount(new LambdaQueryWrapper<RoleResource>()
                .eq(RoleResource::getResourceId, resourceId));
        if (count > 0) {
            throw new StarryException("该资源下存在角色");
        }
        // 删除子资源
        List<Integer> resourceIdList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                        .select(Resource::getId).eq(Resource::getParentId, resourceId))
                .stream().map(Resource::getId)
                .collect(Collectors.toList());
        resourceIdList.add(resourceId);
        resourceMapper.deleteBatchIds(resourceIdList);
    }

    @Override
    public List<ResourceDTO> listResources(ConditionVO condition) {
        // 查询资源列表
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .like(StringUtils.isNotBlank(condition.getKeywords()),
                        Resource::getResourceName, condition.getKeywords()));
        // 获取所有模块
        List<Resource> parentIdList = listResourceParent(resourceList);
        // 根据parentIdList获取模块下的资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 绑定模块下的所有接口
        List<ResourceDTO> resourceDTOList = parentIdList.stream().map(v -> {
            ResourceDTO resourceDTO = BeanCopyUtil.copyObject(v, ResourceDTO.class);
            List<ResourceDTO> childrenList = BeanCopyUtil.copyList(childrenMap.get(v.getId()), ResourceDTO.class);
            resourceDTO.setChildren(childrenList);
            childrenMap.remove(v.getId());
            return resourceDTO;
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childrenMap)) {
            List<Resource> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<ResourceDTO> collect = childrenList.stream().map(v -> BeanCopyUtil.copyObject(v, ResourceDTO.class))
                    .collect(Collectors.toList());
            resourceDTOList.addAll(collect);
        }
        return resourceDTOList;
    }

    /**
     * 获取parentId下的所有资源
     *
     * @param parentId 父ID
     * @return 模块资源
     */
    private Map<Integer, List<Resource>> listResourceChildren(List<Resource> parentId) {
        return parentId.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Resource::getParentId));
    }

    /**
     * 获取所有资源模块，即parent_id
     *
     * @param resourceList 资源列表
     * @return 资源模块列表
     */
    private List<Resource> listResourceParent(List<Resource> resourceList) {
        return resourceList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelOptionDTO> listResourceOption() {
        // 查询资源列表
        List<Resource> resourceList = resourceMapper.selectList(new LambdaQueryWrapper<Resource>()
                .select(Resource::getId, Resource::getResourceName, Resource::getParentId)
                .eq(Resource::getIsAnonymous, FALSE));
        // 获取所有模块
        List<Resource> parentList = listResourceParent(resourceList);
        // 根据父id分组获取模块下的资源
        Map<Integer, List<Resource>> childrenMap = listResourceChildren(resourceList);
        // 组装父子数据
        return parentList.stream().map(item -> {
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Resource> children = childrenMap.get(item.getId());
            if (Objects.nonNull(children)) {
                list = children.stream()
                        .map(resource -> LabelOptionDTO.builder()
                                .id(resource.getId())
                                .label(resource.getResourceName()).build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .label(item.getResourceName())
                    .children(list).build();
        }).collect(Collectors.toList());
    }

}

package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtil;
import com.star.inf.dto.LabelOptionDTO;
import com.star.inf.dto.ResourceDTO;
import com.star.inf.entity.BlogResource;
import com.star.inf.entity.RoleResource;
import com.star.inf.mapper.ResourceMapper;
import com.star.inf.mapper.RoleResourceMapper;
import com.star.inf.vo.ConditionVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminResourceService extends ServiceImpl<ResourceMapper, BlogResource> {

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private RoleResourceMapper roleResourceMapper;

    @Resource
    private ResourceMapper resourceMapper;

    /**
     * 导入swagger权限
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void importSwagger() {
        // delete all
        this.remove(null);
        roleResourceMapper.delete(null);
        List<BlogResource> blogResourceList = new ArrayList<>();
        Map<String, Object> data = restTemplate.getForObject("http://localhost:8000/v2/api-docs", Map.class);
        // 获取所有的模块
        List<Map<String, String>> tagList = (List<Map<String, String>>) data.get("tags");
        tagList.forEach(item -> {
            BlogResource blogResource = BlogResource.builder()
                    .resourceName(item.get("name"))
                    .isAnonymous(FALSE).build();
            blogResourceList.add(blogResource);
        });
        this.saveBatch(blogResourceList);

        Map<String, Integer> permissionMap = blogResourceList.stream()
                .collect(Collectors.toMap(BlogResource::getResourceName, BlogResource::getId));
        blogResourceList.clear();

        // 获取所有接口
        Map<String, Map<String, Map<String, Object>>> path = (Map<String, Map<String, Map<String, Object>>>) data.get("paths");
        path.forEach((url, value) -> value.forEach((requestMethod, info) -> {
            String permissionName = info.get("summary").toString();
            List<String> tag = (List<String>) info.get("tags");
            Integer parentId = permissionMap.get(tag.get(0));
            BlogResource blogResource = BlogResource.builder()
                    .requestMethod(requestMethod.toLowerCase())
                    .resourceName(permissionName)
                    .url(url.replaceAll("\\{[^}]*\\}", "*"))
                    .parentId(parentId)
                    .isAnonymous(FALSE).build();
            blogResourceList.add(blogResource);
        }));
        this.saveBatch(blogResourceList);
    }

    /**
     * 删除资源
     *
     * @param resourceId 资源id列表
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void deleteResource(Integer resourceId) {
        // 查询是否有角色关联
        Long count = roleResourceMapper.selectCount(new LambdaQueryWrapper<RoleResource>()
                .eq(RoleResource::getResourceId, resourceId));
        if (count > 0) {
            throw new StarryException("该资源下存在角色");
        }
        // 删除子资源
        List<Integer> resourceIdList = resourceMapper.selectList(new LambdaQueryWrapper<BlogResource>()
                        .select(BlogResource::getId).eq(BlogResource::getParentId, resourceId))
                .stream().map(BlogResource::getId)
                .collect(Collectors.toList());
        resourceIdList.add(resourceId);
        resourceMapper.deleteBatchIds(resourceIdList);
    }

    /**
     * 查看资源列表
     *
     * @param condition 条件
     * @return 资源列表
     */
    public List<ResourceDTO> listResources(ConditionVO condition) {
        // 查询资源列表
        List<BlogResource> blogResourceListAll = resourceMapper.selectList(new LambdaQueryWrapper<BlogResource>()
                .like(StringUtils.isNotBlank(condition.getKeywords()),
                        BlogResource::getResourceName, condition.getKeywords()));
        // 获取所有模块
        List<BlogResource> parentIdList = listResourceParent(blogResourceListAll);
        // 根据parentIdList获取模块下的资源
        Map<Integer, List<BlogResource>> childrenMap = listResourceChildren(blogResourceListAll);
        // 绑定模块下的所有接口
        List<ResourceDTO> resourceList = parentIdList.stream().map(v -> {
            ResourceDTO resourceDTO = BeanCopyUtil.copyObject(v, ResourceDTO.class);
            List<ResourceDTO> childrenList = BeanCopyUtil.copyList(childrenMap.get(v.getId()), ResourceDTO.class);
            resourceDTO.setChildren(childrenList);
            childrenMap.remove(v.getId());
            return resourceDTO;
        }).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(childrenMap)) {
            List<BlogResource> childrenList = new ArrayList<>();
            childrenMap.values().forEach(childrenList::addAll);
            List<ResourceDTO> collect = childrenList.stream().map(v -> BeanCopyUtil.copyObject(v, ResourceDTO.class))
                    .collect(Collectors.toList());
            resourceList.addAll(collect);
        }
        return resourceList;
    }

    /**
     * 获取所有资源模块，即parent_id
     *
     * @param blogResourceList 资源列表
     * @return 资源模块列表
     */
    private List<BlogResource> listResourceParent(List<BlogResource> blogResourceList) {
        return blogResourceList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .collect(Collectors.toList());
    }

    /**
     * 查看资源选项
     *
     * @return 资源选项
     */
    public List<LabelOptionDTO> listResourceOption() {
        // 查询资源列表
        List<BlogResource> blogResourceList = resourceMapper.selectList(new LambdaQueryWrapper<BlogResource>()
                .select(BlogResource::getId, BlogResource::getResourceName, BlogResource::getParentId)
                .eq(BlogResource::getIsAnonymous, FALSE));
        // 获取所有模块
        List<BlogResource> parentList = listResourceParent(blogResourceList);
        // 根据父id分组获取模块下的资源
        Map<Integer, List<BlogResource>> childrenMap = listResourceChildren(blogResourceList);
        // 组装父子数据
        return parentList.stream().map(item -> {
            List<LabelOptionDTO> list = new ArrayList<>();
            List<BlogResource> children = childrenMap.get(item.getId());
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

    /**
     * 获取parentId下的所有资源
     *
     * @param parentId 父ID
     * @return 模块资源
     */
    private Map<Integer, List<BlogResource>> listResourceChildren(List<BlogResource> parentId) {
        return parentId.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(BlogResource::getParentId));
    }

}

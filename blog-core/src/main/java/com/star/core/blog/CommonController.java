package com.star.core.blog;

import com.star.admin.service.AdminBlogInfoService;
import com.star.common.constant.Result;
import com.star.inf.dto.BlogBackInfoDTO;
import com.star.inf.vo.ResourceVO;
import com.star.inf.vo.RoleVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
public class CommonController {

    @Resource
    private CommonService commonService;

    @Resource
    private AdminBlogInfoService adminBlogInfoService;

    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin/")
    private Result<BlogBackInfoDTO> getBlogBackInfo() {
        return Result.success(adminBlogInfoService.getBlogBackInfo());
    }

    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/admin/resources")
    public Result<?> saveOrUpdateResources(@RequestBody @Valid ResourceVO resourceVO) {
        commonService.saveOrUpdateResource(resourceVO);
        return Result.success();
    }

    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/admin/role")
    public Result<?> listRoles(@RequestBody @Valid RoleVO roleVO) {
        commonService.saveOrUpdateRole(roleVO);
        return Result.success();
    }

    @ApiOperation("用户登出")
    @PostMapping("/users/logout")
    public Result<?> logout() {
        commonService.logout();
        return Result.success();
    }

}

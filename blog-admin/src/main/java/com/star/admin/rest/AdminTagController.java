package com.star.admin.rest;

import com.star.admin.service.AdminTagService;
import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import com.star.inf.dto.TagBackDTO;
import com.star.inf.dto.TagDTO;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.TagVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminTagController {

    @Resource
    private AdminTagService adminTagService;

    @ApiOperation(value = "查看后台标签列表")
    @GetMapping("/tags")
    public Result<PageData<TagBackDTO>> listTagBack(ConditionVO condition) {
        return Result.success(adminTagService.listTagBack(condition));
    }

    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/tags")
    public Result<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        adminTagService.saveOrUpdateTag(tagVO);
        return Result.success();
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/tags")
    public Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        adminTagService.deleteTag(tagIdList);
        return Result.success();
    }

    @ApiOperation(value = "搜索文章标签")
    @GetMapping("/tags/search")
    public Result<List<TagDTO>> listTagsBySearch(ConditionVO condition) {
        return Result.success(adminTagService.listTagsBySearch(condition));
    }

}

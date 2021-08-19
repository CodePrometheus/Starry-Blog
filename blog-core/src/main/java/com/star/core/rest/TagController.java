package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.ArticlePreviewListDTO;
import com.star.core.dto.PageData;
import com.star.core.dto.TagBackDTO;
import com.star.core.dto.TagDTO;
import com.star.core.entity.Tag;
import com.star.core.service.ArticleService;
import com.star.core.service.TagService;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.TagVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 标签逻辑
 *
 * @Author: zzStar
 * @Date: 12-20-2020 11:02
 */
@Api(tags = "标签模块")
@RestController
public class TagController {

    @Resource
    private TagService tagService;

    @ApiOperation(value = "查看标签列表")
    @GetMapping("/tags")
    private Result<PageData<TagDTO>> listTags() {
        return Result.success(tagService.listTags());
    }

    @ApiOperation(value = "查看后台标签列表")
    @GetMapping("/admin/tags")
    private Result<PageData<TagBackDTO>> listTagBack(ConditionVO condition) {
        return Result.success(tagService.listTagBack(condition));
    }

    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/admin/tags")
    private Result<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return Result.success();
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    private Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return Result.success();
    }

    @ApiOperation(value = "搜索文章标签")
    @GetMapping("/admin/tags/search")
    public Result<List<TagDTO>> listTagsBySearch(ConditionVO condition) {
        return Result.success(tagService.listTagsBySearch(condition));
    }

}


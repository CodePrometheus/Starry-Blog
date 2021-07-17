package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.domain.entity.Tag;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.TagVO;
import com.star.core.service.ArticleService;
import com.star.core.service.TagService;
import com.star.core.service.dto.ArticlePreviewListDTO;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.TagDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.star.common.constant.MessageConst.*;
import static com.star.common.constant.StatusConst.OK;

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

    @Resource
    private ArticleService articleService;

    @ApiOperation(value = "查看标签列表")
    @GetMapping("/tags")
    private Result<PageDTO<TagDTO>> listTags() {
        return new Result<>(true, OK, QUERY, tagService.listTags());
    }

    @ApiOperation(value = "查看分类下对应的文章")
    @GetMapping("/tags/{tagId}")
    private Result<ArticlePreviewListDTO> listArticlesByCategoryId(@PathVariable("tagId") Integer tagId, Long current) {
        ConditionVO conditionVO = ConditionVO.builder()
                .tagId(tagId)
                .current(current).build();
        return new Result<>(true, OK, QUERY, articleService.listArticlesByCondition(conditionVO));
    }

    @ApiOperation(value = "查看后台标签列表")
    @GetMapping("/admin/tags")
    private Result<PageDTO<Tag>> listTagBackDTO(ConditionVO condition) {
        return new Result<>(true, OK, QUERY, tagService.listTagBackDTO(condition));
    }

    @ApiOperation(value = "添加或修改标签")
    @PostMapping("/admin/tags")
    private Result<?> saveOrUpdateTag(@Valid @RequestBody TagVO tagVO) {
        tagService.saveOrUpdateTag(tagVO);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "删除标签")
    @DeleteMapping("/admin/tags")
    private Result<?> deleteTag(@RequestBody List<Integer> tagIdList) {
        tagService.deleteTag(tagIdList);
        return new Result<>(true, OK, DELETE);
    }


}


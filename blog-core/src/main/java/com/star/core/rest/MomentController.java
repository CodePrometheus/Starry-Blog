package com.star.core.rest;

import com.star.common.constant.PathConst;
import com.star.common.constant.Result;
import com.star.core.dto.MomentDTO;
import com.star.core.dto.PageData;
import com.star.core.service.MomentService;
import com.star.core.util.ImageUtil;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.vo.MomentVO;
import com.star.core.vo.TopVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:55 PM
 */
@Api(tags = "动态模块")
@RestController
public class MomentController {

    @Resource
    private ImageUtil imageUtil;
    @Resource
    private MomentService momentService;

    @ApiOperation(value = "查询动态")
    @GetMapping("/moments")
    public Result<PageData<MomentDTO>> listMoments() {
        return Result.success(momentService.listMoments());
    }

    @ApiOperation(value = "查看后台动态")
    @GetMapping("/admin/moments")
    private Result<PageData<MomentDTO>> listMomentBack(ConditionVO conditionVO) {
        return Result.success(momentService.listMomentBack(conditionVO));
    }

    @ApiOperation(value = "添加或修改动态")
    @PostMapping("/admin/moments")
    public Result<?> saveOrUpdateMoment(@Valid @RequestBody MomentVO momentVO) {
        momentService.saveOrUpdateMoment(momentVO);
        return Result.success();
    }

    @ApiOperation(value = "修改动态置顶状态")
    @PutMapping("/admin/moments/top")
    public Result<?> updateMomentTop(@Valid @RequestBody TopVO top) {
        momentService.updateMomentTop(top);
        return Result.success();
    }

    @ApiOperation(value = "上传动态图片")
    @PostMapping("/admin/moments/images")
    public Result<String> uploadMomentImages(MultipartFile file) {
        return Result.success(imageUtil.upload(file, PathConst.MOMENT));
    }

    @ApiOperation(value = "恢复或删除动态")
    @PutMapping("/admin/moments")
    public Result<?> updateMomentDelete(@Valid @RequestBody DeleteVO deleteVO) {
        momentService.updateMomentDelete(deleteVO);
        return Result.success();
    }

    @ApiOperation(value = "物理删除动态")
    @DeleteMapping("/admin/moments")
    public Result<?> deleteMoment(@RequestBody List<Integer> momentIdList) {
        momentService.deleteMoment(momentIdList);
        return Result.success();
    }

    @ApiOperation(value = "根据Id查看动态")
    @GetMapping("/admin/moments/{momentId}")
    public Result<MomentDTO> getMomentById(@PathVariable("momentId") Integer momentId) {
        return Result.success(momentService.getMomentById(momentId));
    }

    @ApiOperation(value = "点赞动态")
    @PostMapping("/moments/{momentId}/like")
    public Result<?> saveMomentLike(@PathVariable("momentId") Integer momentId) {
        momentService.saveMomentLike(momentId);
        return Result.success();
    }

}

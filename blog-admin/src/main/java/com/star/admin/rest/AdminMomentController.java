package com.star.admin.rest;

import com.star.admin.service.AdminMomentService;
import com.star.common.constant.PathConst;
import com.star.common.constant.Result;
import com.star.common.tool.ImageUtil;
import com.star.inf.dto.MomentDTO;
import com.star.inf.dto.PageData;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.DeleteVO;
import com.star.inf.vo.MomentVO;
import com.star.inf.vo.TopVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminMomentController {

    @Resource
    private ImageUtil imageUtil;

    @Resource
    private AdminMomentService adminMomentService;

    @ApiOperation(value = "查看后台动态")
    @GetMapping("/moments")
    public Result<PageData<MomentDTO>> listMomentBack(ConditionVO conditionVO) {
        return Result.success(adminMomentService.listMomentBack(conditionVO));
    }

    @ApiOperation(value = "添加或修改动态")
    @PostMapping("/moments")
    public Result<?> saveOrUpdateMoment(@Valid @RequestBody MomentVO momentVO) {
        adminMomentService.saveOrUpdateMoment(momentVO);
        return Result.success();
    }

    @ApiOperation(value = "修改动态置顶状态")
    @PutMapping("/moments/top")
    public Result<?> updateMomentTop(@Valid @RequestBody TopVO top) {
        adminMomentService.updateMomentTop(top);
        return Result.success();
    }

    @ApiOperation(value = "上传动态图片")
    @PostMapping("/moments/images")
    public Result<String> uploadMomentImages(MultipartFile file) {
        return Result.success(imageUtil.upload(file, PathConst.MOMENT));
    }

    @ApiOperation(value = "恢复或删除动态")
    @PutMapping("/moments")
    public Result<?> updateMomentDelete(@Valid @RequestBody DeleteVO deleteVO) {
        adminMomentService.updateMomentDelete(deleteVO);
        return Result.success();
    }

    @ApiOperation(value = "物理删除动态")
    @DeleteMapping("/moments")
    public Result<?> deleteMoment(@RequestBody List<Integer> momentIdList) {
        adminMomentService.deleteMoment(momentIdList);
        return Result.success();
    }

    @ApiOperation(value = "根据Id查看动态")
    @GetMapping("/moments/{momentId}")
    public Result<MomentDTO> getBackMomentById(@PathVariable("momentId") Integer momentId) {
        return Result.success(adminMomentService.getBackMomentById(momentId));
    }

}
